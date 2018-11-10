package com.developer.abhishek.calleru.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.developer.abhishek.calleru.models.Contacts;
import com.developer.abhishek.calleru.repository.ContactsRepo;

import java.util.ArrayList;
import java.util.List;

public class ContactsVM extends AndroidViewModel{

    private LiveData<List<Contacts>> listLiveData;
    private LiveData<ArrayList<String>> allContactListLiveData;

    public ContactsVM(@NonNull Application application) {
        super(application);

        ContactsRepo contactsRepo = new ContactsRepo(application.getApplicationContext());
        listLiveData = contactsRepo.getContactsWithoutRepetation();
        allContactListLiveData = contactsRepo.getAllContacts();
    }

    public LiveData<List<Contacts>> getListLiveData() {
        return listLiveData;
    }

    public LiveData<ArrayList<String>> getAllContactListLiveData() {
        return allContactListLiveData;
    }
}
