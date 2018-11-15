package com.developer.abhishek.calleru.repository;

import android.arch.lifecycle.MutableLiveData;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.developer.abhishek.calleru.models.Contacts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsRepo {

    private static final MutableLiveData<List<Contacts>> contactsMutableLiveData = new MutableLiveData<>();
    private static final MutableLiveData<ArrayList<String>> allContactsMutableLiveData = new MutableLiveData<>();

    private static final HashMap<String,String> allContactsHM = new HashMap<>();

    private static final List<Contacts> contactWithoutRep = new ArrayList<>();
    private static final ArrayList<String> allContactList = new ArrayList<>();

    private static Context context;

    public ContactsRepo(Context context) {
        this.context = context;
        new BackgroundTask().execute();
    }

    public MutableLiveData<List<Contacts>> getContactsWithoutRepetation() {
        return contactsMutableLiveData;
    }

    public MutableLiveData<ArrayList<String>> getAllContacts() {
        return allContactsMutableLiveData;
    }

    private static void sortListBasedOnAlphabet(List<Contacts> list){
        if (list.size() > 0) {
            Collections.sort(list, new Comparator<Contacts>() {
                @Override
                public int compare(final Contacts object1, final Contacts object2) {
                    return object1.getName().compareTo(object2.getName());
                }
            });
        }
    }

    public static class BackgroundTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            allContactsHM.clear();
            contactWithoutRep.clear();
            allContactsHM.clear();
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            if ((cursor != null ? cursor.getCount() : 0) > 0) {
                while (cursor != null && cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor contactCursor = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (contactCursor.moveToNext()) {
                            String phoneNo = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            //  Converting all contact number to format +91XXXXXXXXXX to remove duplication
                            if(phoneNo.length() == 10){
                                phoneNo = "+91"+phoneNo;
                            }else if(phoneNo.length() == 11){
                                phoneNo = "+91"+phoneNo.substring(0,5)+phoneNo.substring(6);
                            }else if(phoneNo.length() == 14){
                                phoneNo = "+91"+phoneNo.substring(4);
                            }else if(phoneNo.length() == 15){
                                phoneNo = "+91"+phoneNo.substring(4,9)+phoneNo.substring(10);
                            }
                            //  Setting contact number as key that will remove duplicate number
                            allContactsHM.put(phoneNo,name);
                        }
                        contactCursor.close();
                    }
                }
            }
            if(cursor != null){
                cursor.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //  Contacts without repetation
            HashMap<String,String> nameNumberHM = new HashMap<>();
            for(Map.Entry entry: allContactsHM.entrySet()){
                nameNumberHM.put(String.valueOf(entry.getValue()),String.valueOf(entry.getKey()));
            }

            for(Map.Entry<String,String> entry:nameNumberHM.entrySet()){
                contactWithoutRep.add(new Contacts(String.valueOf(entry.getKey()),String.valueOf(entry.getValue())));
            }

            sortListBasedOnAlphabet(contactWithoutRep);
            contactsMutableLiveData.setValue(contactWithoutRep);

            //  all contacts
            for(Map.Entry<String,String> entry: allContactsHM.entrySet()){
                allContactList.add(String.valueOf(entry.getKey()));
            }

            allContactsMutableLiveData.setValue(allContactList);
        }
    }
}
