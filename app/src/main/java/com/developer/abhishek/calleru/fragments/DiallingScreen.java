package com.developer.abhishek.calleru.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeScroll;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.abhishek.calleru.HomePage;
import com.developer.abhishek.calleru.R;
import com.developer.abhishek.calleru.adapters.CallLogsAdapter;
import com.developer.abhishek.calleru.listener.RecyclerItemClickListener;
import com.developer.abhishek.calleru.models.CallLogs;
import com.developer.abhishek.calleru.viewModels.CallLogVM;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DiallingScreen extends Fragment {

    @BindView(R.id.recyclerViewAtDialScreen)
    RecyclerView recyclerView;
    @BindView(R.id.dialPad)
    LinearLayout dialPad;
    @BindView(R.id.numViewTv)
    TextView numView;
    @BindView(R.id.numViewLayout)
    LinearLayout numViewLayout;

    private boolean isToShowDialPad;
    private boolean isToDial = false;
    private String dialledNumber = null;

    private List<CallLogs> callLogsList;

    changeInFragment changeInFragment;

    public interface changeInFragment{
        void onDialScreenChange(boolean isToDial,boolean isToShowDialPad,String dialledNumber);
    }

    public DiallingScreen() {
        // Required empty public constructor
    }

    public void setToShowDialPad(boolean toShowDialPad) {
        isToShowDialPad = toShowDialPad;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            changeInFragment = (changeInFragment) context;
        }catch (Exception e){
            throw new ClassCastException(context.toString() + " not implemented onStepSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialling_screen, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(isToShowDialPad){
            dialPad.setVisibility(View.VISIBLE);
        }else{
            dialPad.setVisibility(View.GONE);
        }

        loadRecentCallLogs();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView ,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String dial = "tel:" + callLogsList.get(position).getCallNumber();
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                    }
                })
        );

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_DRAGGING){
                    dialPad.setVisibility(View.GONE);
                    isToShowDialPad = false;
                    changeInFragment.onDialScreenChange(isToDial,isToShowDialPad,dialledNumber);
                }
            }
        });
    }

    private void loadRecentCallLogs(){
        CallLogVM callLogVM = ViewModelProviders.of(this).get(CallLogVM.class);
        callLogVM.getCallLogs().observe(this, new Observer<List<CallLogs>>() {
            @Override
            public void onChanged(@Nullable List<CallLogs> callLogs) {
                if(callLogs != null){
                    callLogsList = callLogs;
                    setCallLogToRv();
                }
            }
        });
    }

    private void setCallLogToRv(){
        if(callLogsList != null){
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
            CallLogsAdapter callLogsAdapter = new CallLogsAdapter(callLogsList,getContext());
            recyclerView.setAdapter(callLogsAdapter);
        }
    }

    @OnClick(R.id.numOne)
    void one(){
        if(dialledNumber == null){
            dialledNumber = "1";
        }else {
            dialledNumber += "1";
        }

        if(!isToDial){
            isToDial = true;
            numViewLayout.setVisibility(View.VISIBLE);
        }

        changeInFragment.onDialScreenChange(isToDial,isToShowDialPad,dialledNumber);
        numView.setText(dialledNumber);
    }

    @OnClick(R.id.numTwo)
    void two(){
        if(dialledNumber == null){
            dialledNumber = "2";
        }else {
            dialledNumber += "2";
        }

        if(!isToDial){
            isToDial = true;
            numViewLayout.setVisibility(View.VISIBLE);
        }

        changeInFragment.onDialScreenChange(isToDial,isToShowDialPad,dialledNumber);
        numView.setText(dialledNumber);
    }

    @OnClick(R.id.numThree)
    void three(){
        if(dialledNumber == null){
            dialledNumber = "3";
        }else {
            dialledNumber += "3";
        }

        if(!isToDial){
            isToDial = true;
            numViewLayout.setVisibility(View.VISIBLE);
        }

        changeInFragment.onDialScreenChange(isToDial,isToShowDialPad,dialledNumber);
        numView.setText(dialledNumber);
    }

    @OnClick(R.id.numFour)
    void four(){
        if(dialledNumber == null){
            dialledNumber = "4";
        }else {
            dialledNumber += "4";
        }

        if(!isToDial){
            isToDial = true;
            numViewLayout.setVisibility(View.VISIBLE);
        }

        changeInFragment.onDialScreenChange(isToDial,isToShowDialPad,dialledNumber);
        numView.setText(dialledNumber);
    }

    @OnClick(R.id.numFive)
    void five(){
        if(dialledNumber == null){
            dialledNumber = "5";
        }else {
            dialledNumber += "5";
        }

        if(!isToDial){
            isToDial = true;
            numViewLayout.setVisibility(View.VISIBLE);
        }

        changeInFragment.onDialScreenChange(isToDial,isToShowDialPad,dialledNumber);
        numView.setText(dialledNumber);
    }

    @OnClick(R.id.numSix)
    void six(){
        if(dialledNumber == null){
            dialledNumber = "6";
        }else {
            dialledNumber += "6";
        }

        if(!isToDial){
            isToDial = true;
            numViewLayout.setVisibility(View.VISIBLE);
        }

        changeInFragment.onDialScreenChange(isToDial,isToShowDialPad,dialledNumber);
        numView.setText(dialledNumber);
    }

    @OnClick(R.id.numSeven)
    void seven(){
        if(dialledNumber == null){
            dialledNumber = "7";
        }else {
            dialledNumber += "7";
        }

        if(!isToDial){
            isToDial = true;
            numViewLayout.setVisibility(View.VISIBLE);
        }

        changeInFragment.onDialScreenChange(isToDial,isToShowDialPad,dialledNumber);
        numView.setText(dialledNumber);
    }

    @OnClick(R.id.numEight)
    void eight(){
        if(dialledNumber == null){
            dialledNumber = "8";
        }else {
            dialledNumber += "8";
        }

        if(!isToDial){
            isToDial = true;
            numViewLayout.setVisibility(View.VISIBLE);
        }

        changeInFragment.onDialScreenChange(isToDial,isToShowDialPad,dialledNumber);
        numView.setText(dialledNumber);
    }

    @OnClick(R.id.numNine)
    void nine(){
        if(dialledNumber == null){
            dialledNumber = "9";
        }else {
            dialledNumber += "9";
        }

        if(!isToDial){
            isToDial = true;
            numViewLayout.setVisibility(View.VISIBLE);
        }

        changeInFragment.onDialScreenChange(isToDial,isToShowDialPad,dialledNumber);
        numView.setText(dialledNumber);
    }

    @OnClick(R.id.numZero)
    void zero(){
        if(dialledNumber == null){
            dialledNumber = "0";
        }else {
            dialledNumber += "0";
        }

        if(!isToDial){
            isToDial = true;
            numViewLayout.setVisibility(View.VISIBLE);
        }

        changeInFragment.onDialScreenChange(isToDial,isToShowDialPad,dialledNumber);
        numView.setText(dialledNumber);
    }

    @OnClick(R.id.numHash)
    void hash(){
        if(dialledNumber == null){
            dialledNumber = "#";
        }else {
            dialledNumber += "#";
        }

        if(!isToDial){
            isToDial = true;
            numViewLayout.setVisibility(View.VISIBLE);
        }

        changeInFragment.onDialScreenChange(isToDial,isToShowDialPad,dialledNumber);
        numView.setText(dialledNumber);
    }

    @OnClick(R.id.numStar)
    void star(){
        if(dialledNumber == null){
            dialledNumber = "*";
        }else {
            dialledNumber += "*";
        }

        if(!isToDial){
            isToDial = true;
            numViewLayout.setVisibility(View.VISIBLE);
        }

        changeInFragment.onDialScreenChange(isToDial,isToShowDialPad,dialledNumber);
        numView.setText(dialledNumber);
    }

    @OnClick(R.id.clearLastNumBtn)
    void clearLastNum(){
        if(dialledNumber != null && dialledNumber.length() != 0){
            dialledNumber = dialledNumber.substring(0,dialledNumber.length()-1);
        }

        if(dialledNumber == null || dialledNumber.isEmpty() || dialledNumber.length() == 0){
            isToDial = false;
            numViewLayout.setVisibility(View.GONE);
        }else{
            numView.setText(dialledNumber);
        }
        changeInFragment.onDialScreenChange(isToDial,isToShowDialPad,dialledNumber);
    }
}
