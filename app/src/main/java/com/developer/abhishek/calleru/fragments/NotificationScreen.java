package com.developer.abhishek.calleru.fragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.abhishek.calleru.R;
import com.developer.abhishek.calleru.adapters.NotificationAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationScreen extends Fragment {

    @BindView(R.id.notificationRv)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.noNewNotiError)
    TextView noNewNotifTv;

    @BindString(R.string.networkError)
    String networkErr;

    private List<String> notificationList = new ArrayList<>();

    private Toast toast;

    public NotificationScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_screen, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(!networkStatus()){
            showError(networkErr);
            progressBar.setVisibility(View.GONE);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NOTIFICATIONS").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String notification = snapshot.getValue(String.class);
                    notificationList.add(notification);
                }

                progressBar.setVisibility(View.GONE);

                if(notificationList == null || notificationList.size() == 0){
                    noNewNotifTv.setVisibility(View.VISIBLE);
                }else{
                    noNewNotifTv.setVisibility(View.GONE);

                    recyclerView.setVisibility(View.VISIBLE);
                    NotificationAdapter notificationAdapter = new NotificationAdapter(notificationList);
                    recyclerView.setAdapter(notificationAdapter);
                }
            }else{
                progressBar.setVisibility(View.GONE);

                if(notificationList == null || notificationList.size() == 0){
                    noNewNotifTv.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private boolean networkStatus(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isAvailable() && connectivityManager.getActiveNetworkInfo().isConnected());
    }

    private void showError(String message){
        progressBar.setVisibility(View.GONE);
        if(toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT);
        toast.show();
    }
}
