package com.developer.abhishek.calleru.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.developer.abhishek.calleru.R;
import com.developer.abhishek.calleru.adapters.ContactAdapter;
import com.developer.abhishek.calleru.listener.RecyclerItemClickListener;
import com.developer.abhishek.calleru.models.Contacts;
import com.developer.abhishek.calleru.repository.ContactsRepo;
import com.developer.abhishek.calleru.viewModels.ContactsVM;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactScreen extends Fragment {

    private final String RECYCLER_VIEW_SAVED_STATE = "recycler_view_saved_state";
    private final String SWIPE_REFRESH_SAVED_STATE = "swipe_refresh_saved_state";

    @BindView(R.id.recyclerViewAtContactScreen)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private boolean flag = false;

    private List<Contacts> contactsList;

    private Parcelable parcelable;

    public ContactScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_screen, container, false);
        ButterKnife.bind(this,view);
        if(savedInstanceState != null && savedInstanceState.containsKey(RECYCLER_VIEW_SAVED_STATE)){
            flag = true;
            parcelable = ((Bundle) savedInstanceState).getParcelable(RECYCLER_VIEW_SAVED_STATE);
        }
        if(savedInstanceState != null && savedInstanceState.containsKey(SWIPE_REFRESH_SAVED_STATE)){
            swipeRefreshLayout.setRefreshing(savedInstanceState.getBoolean(SWIPE_REFRESH_SAVED_STATE));
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressBar.setVisibility(View.VISIBLE);
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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                flag = false;
                new ContactsRepo.BackgroundTask().execute();
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECYCLER_VIEW_SAVED_STATE,recyclerView.getLayoutManager().onSaveInstanceState());
        outState.putBoolean(SWIPE_REFRESH_SAVED_STATE,swipeRefreshLayout.isRefreshing());
    }

    private void loadContacts(){
        ContactsVM contactsVM = ViewModelProviders.of(getActivity()).get(ContactsVM.class);
        contactsVM.getListLiveData().observe(getActivity(), new Observer<List<Contacts>>() {
            @Override
            public void onChanged(@Nullable List<Contacts> contacts) {
                if(contacts != null){
                    contactsList = contacts;
                    setContactToRv();
                    if(swipeRefreshLayout.isRefreshing()){
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }
        });
    }

    private void setContactToRv(){
        progressBar.setVisibility(View.GONE);
        ContactAdapter contactAdapter = new ContactAdapter(contactsList);
        contactAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setAdapter(contactAdapter);
        if(parcelable != null && flag){
            recyclerView.getLayoutManager().onRestoreInstanceState(parcelable);
        }
    }
}
