package com.developer.abhishek.calleru.fragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

    private final String RECYCLER_VIEW_SAVED_STATE = "recycler_view_saved_state";
    private final String SWIPE_REFRESH_SAVED_STATE = "swipe_refresh_saved_state";

    @BindView(R.id.notificationRv)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.noNewNotiError)
    TextView noNewNotifTv;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindString(R.string.networkError)
    String networkErr;

    private final List<String> notificationList = new ArrayList<>();

    private Parcelable parcelable;
    private Toast toast;

    private boolean flag = false;

    public NotificationScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_screen, container, false);
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

        if(!networkStatus()){
            showError(networkErr);
            progressBar.setVisibility(View.GONE);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadNotification();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                flag = false;
                loadNotification();
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECYCLER_VIEW_SAVED_STATE,recyclerView.getLayoutManager().onSaveInstanceState());
        outState.putBoolean(SWIPE_REFRESH_SAVED_STATE,swipeRefreshLayout.isRefreshing());
    }

    private void loadNotification(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NOTIFICATIONS").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        databaseReference.addValueEventListener(valueEventListener);
    }

    final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                notificationList.clear();
                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
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

                    if(parcelable != null && flag){
                        recyclerView.getLayoutManager().onRestoreInstanceState(parcelable);
                    }
                }
            }else{
                progressBar.setVisibility(View.GONE);

                if(notificationList == null || notificationList.size() == 0){
                    noNewNotifTv.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
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
