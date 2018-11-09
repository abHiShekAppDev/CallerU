package com.developer.abhishek.calleru.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.developer.abhishek.calleru.models.Contacts;
import com.developer.abhishek.calleru.repository.ContactsRepo;

import java.util.List;

public class ContactsVM extends AndroidViewModel{

    private LiveData<List<Contacts>> listLiveData;

    public ContactsVM(@NonNull Application application) {
        super(application);

        ContactsRepo contactsRepo = new ContactsRepo();
        listLiveData = contactsRepo.getAllContacts(application.getApplicationContext());
    }

    public LiveData<List<Contacts>> getListLiveData() {
        return listLiveData;
    }
}
