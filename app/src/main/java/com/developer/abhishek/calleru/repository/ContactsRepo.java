package com.developer.abhishek.calleru.repository;

import android.arch.lifecycle.MutableLiveData;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.developer.abhishek.calleru.models.Contacts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsRepo {

    private final String TAG_PHONE = "Phone : ";
    private final String TAG_NAME = "Name : ";

    private MutableLiveData<List<Contacts>> contactsMutableLiveData = new MutableLiveData<>();
    private List<Contacts> contactsList = new ArrayList<>();

    public MutableLiveData<List<Contacts>> getAllContacts(Context context) {

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if ((cursor != null ? cursor.getCount() : 0) > 0) {
            while (cursor != null && cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                HashMap<String,String> numberHashMap = new HashMap<>();
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor contactCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (contactCursor.moveToNext()) {
                        String phoneNo = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        //  Converting all contact number to format +91 XXXXXXXXXX to remove duplication
                        if(phoneNo.length() == 10){
                            phoneNo = "+91 " + phoneNo;
                        }

                        //  Setting contact number as key that will remove duplicate number
                        numberHashMap.put(phoneNo,name);

                        Log.i(TAG_NAME,name);
                        Log.i(TAG_PHONE, phoneNo);
                    }
                    contactCursor.close();
                }
                addContactToList(numberHashMap);
            }
        }
        if(cursor != null){
            cursor.close();
        }

        sortListBasedOnAlphabet();
        contactsMutableLiveData.setValue(contactsList);
        return contactsMutableLiveData;
    }

    private void addContactToList(HashMap<String,String> numberNameHM){

        HashMap<String,String> nameNumberHM = new HashMap<>();
        for(Map.Entry entry:numberNameHM.entrySet()){
            nameNumberHM.put(String.valueOf(entry.getValue()),String.valueOf(entry.getKey()));
        }

        //  Finally adding all contacts to list
        for(Map.Entry<String,String> entry:nameNumberHM.entrySet()){
            contactsList.add(new Contacts(String.valueOf(entry.getKey()),String.valueOf(entry.getValue())));
        }
    }

    private void sortListBasedOnAlphabet(){
        if (contactsList.size() > 0) {
            Collections.sort(contactsList, new Comparator<Contacts>() {
                @Override
                public int compare(final Contacts object1, final Contacts object2) {
                    return object1.getName().compareTo(object2.getName());
                }
            });
        }
    }
}
