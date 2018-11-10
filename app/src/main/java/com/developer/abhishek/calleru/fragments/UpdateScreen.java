package com.developer.abhishek.calleru.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.abhishek.calleru.R;
import com.developer.abhishek.calleru.UpdatePage;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateScreen extends Fragment {


    public UpdateScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_screen, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick(R.id.updateBtn)
    void showUpdateAct(){
        startActivity(new Intent(getActivity(), UpdatePage.class));
    }

}
