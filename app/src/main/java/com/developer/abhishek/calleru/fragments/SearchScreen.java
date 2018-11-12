package com.developer.abhishek.calleru.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.abhishek.calleru.R;
import com.developer.abhishek.calleru.adapters.SearchAdapter;
import com.developer.abhishek.calleru.listener.RecyclerItemClickListener;
import com.developer.abhishek.calleru.models.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchScreen extends Fragment {

    @BindView(R.id.searchRv)
    RecyclerView searchRv;
    @BindView(R.id.searchEt)
    EditText searchEt;
    @BindView(R.id.noNewNotiError)
    TextView noContactsFound;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private List<Users> usersList = new ArrayList<>();
    public SearchScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_screen, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchRv.setHasFixedSize(true);
        searchRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        searchRv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), searchRv ,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String dial = "tel:" + usersList.get(position).getMobileNumber();
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                    }
                })
        );
    }

    @OnClick(R.id.searchBtn)
    void searchByName() {
        String searchText = searchEt.getText().toString();
        noContactsFound.setVisibility(View.GONE);
        searchRv.setVisibility(View.VISIBLE);
        usersList.clear();

        if (searchText != null && !searchText.isEmpty()) {
            searchText = searchText.toLowerCase();
            progressBar.setVisibility(View.VISIBLE);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("USERS");
            Query firebaseSearchQuery = databaseReference.orderByChild("Name").startAt(searchText).endAt(searchText + "\uf8ff");
            firebaseSearchQuery.addValueEventListener(valueEventListener);
        }else{
            Toast.makeText(getActivity(),"Enter text to search",Toast.LENGTH_SHORT).show();
        }
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users user = snapshot.getValue(Users.class);
                    usersList.add(user);
                }
                SearchAdapter searchAdapter = new SearchAdapter(usersList);
                searchRv.setAdapter(searchAdapter);
            }else{
                noContactsFound.setVisibility(View.VISIBLE);
                searchRv.setVisibility(View.GONE);
            }
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };



}
