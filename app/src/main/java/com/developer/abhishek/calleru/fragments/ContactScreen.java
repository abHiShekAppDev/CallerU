package com.developer.abhishek.calleru.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.abhishek.calleru.R;
import com.developer.abhishek.calleru.adapters.ContactAdapter;
import com.developer.abhishek.calleru.listener.RecyclerItemClickListener;
import com.developer.abhishek.calleru.models.Contacts;
import com.developer.abhishek.calleru.viewModels.ContactsVM;

import java.util.List;
import java.util.Observable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactScreen extends Fragment {

    @BindView(R.id.recyclerViewAtContactScreen)
    RecyclerView recyclerView;

    private List<Contacts> contactsList;

    public ContactScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_screen, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadContacts();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView ,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String dial = "tel:" + contactsList.get(position).getNumber();
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                    }
                })
        );
    }

    private void loadContacts(){
        ContactsVM contactsVM = ViewModelProviders.of(this).get(ContactsVM.class);
        contactsVM.getListLiveData().observe(this, new Observer<List<Contacts>>() {
            @Override
            public void onChanged(@Nullable List<Contacts> contacts) {
                if(contacts != null){
                    contactsList = contacts;
                    setContactToRv();
                }
            }
        });
    }

    private void setContactToRv(){
        ContactAdapter contactAdapter = new ContactAdapter(contactsList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setAdapter(contactAdapter);
    }
}
