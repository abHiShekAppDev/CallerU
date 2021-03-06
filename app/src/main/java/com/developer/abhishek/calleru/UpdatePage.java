package com.developer.abhishek.calleru;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.abhishek.calleru.background.NotifyIntentService;
import com.developer.abhishek.calleru.viewModels.ContactsVM;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdatePage extends AppCompatActivity {

    private static final String NUMBER_SAVED_INST_KEY = "saved_number";
    private static final String CURRENT_STEP_SAVED_KEY = "current_step";

    @BindView(R.id.newNumberTv)
    TextView newNumberTv;
    @BindView(R.id.nextBtnAtUpdate)
    ImageButton nextBtn;

    @BindString(R.string.enterValidNumber)
    String enterNumberStr;
    @BindString(R.string.networkError)
    String networkErr;

    private int currentStep = 1;
    private String newNumber = "";
    private String currentNumber;

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_page);
        ButterKnife.bind(this);

        currentNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        if(currentNumber == null){
            startActivity(new Intent(UpdatePage.this, LoginPage.class));
            finish();
        }

        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(NUMBER_SAVED_INST_KEY)){
                newNumber = savedInstanceState.getString(NUMBER_SAVED_INST_KEY);
            }
            if(savedInstanceState.containsKey(CURRENT_STEP_SAVED_KEY)){
                currentStep = savedInstanceState.getInt(CURRENT_STEP_SAVED_KEY);
            }

            if(newNumber != null){
                if(currentStep == 1){
                    newNumberTv.setText(newNumber);
                }else if(currentStep == 2){
                    String updateStr = String.format(getResources().getString(R.string.updateYourNumberTo), newNumber);
                    newNumberTv.setText(updateStr);
                    nextBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_black_24dp));
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(NUMBER_SAVED_INST_KEY,newNumber);
        outState.putInt(CURRENT_STEP_SAVED_KEY,currentStep);
    }

    @OnClick(R.id.nextBtnAtUpdate)
    void next(){
        if(currentStep == 1){
            if(!newNumber.isEmpty() && newNumber.length() == 10){
                currentStep++;
                String updateStr = String.format(getResources().getString(R.string.updateYourNumberTo), newNumber);
                newNumberTv.setText(updateStr);
                nextBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_black_24dp));
            }else{
                showError(enterNumberStr);
            }

        }else{
            if(networkStatus()){
                if(!newNumber.isEmpty()){
                    ContactsVM contactsVM = ViewModelProviders.of(this).get(ContactsVM.class);
                    contactsVM.getAllContactListLiveData().observe(this, new Observer<ArrayList<String>>() {
                        @Override
                        public void onChanged(@Nullable ArrayList<String> strings) {
                            if(strings != null){
                                Intent intent = new Intent(UpdatePage.this,NotifyIntentService.class);
                                intent.setAction(NotifyIntentService.ACTION_NOTIFY_OTHER_USER);
                                intent.putExtra(NotifyIntentService.NEW_NUMBER_PASS_INTENT,newNumber);
                                intent.putExtra(NotifyIntentService.CURRENT_NUMBER_PASS_INTENT,currentNumber);
                                intent.putStringArrayListExtra(NotifyIntentService.ALL_CONTACT_PASS_INTENT,strings);
                                startService(intent);
                                currentStep = 1;
                                onBackPressed();
                            }
                        }
                    });
                }
            }else {
                showError(networkErr);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(currentStep == 1){
            super.onBackPressed();
        }else{
            currentStep--;
            newNumberTv.setText(newNumber);
            nextBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_forward_color_24dp));
        }

    }

    @OnClick(R.id.numOne)
    void one(){
        if(currentStep == 1){
            newNumber += "1";
            newNumberTv.append("1");
        }
    }

    @OnClick(R.id.numTwo)
    void two(){
        if(currentStep == 1){
            newNumber += "2";
            newNumberTv.append("2");
        }
    }

    @OnClick(R.id.numThree)
    void three(){
        if(currentStep == 1){
            newNumber += "3";
            newNumberTv.append("3");
        }
    }

    @OnClick(R.id.numFour)
    void four(){
        if(currentStep == 1){
            newNumber += "4";
            newNumberTv.append("4");
        }
    }

    @OnClick(R.id.numFive)
    void five(){
        if(currentStep == 1){
            newNumber += "5";
            newNumberTv.append("5");
        }
    }

    @OnClick(R.id.numSix)
    void six(){
        if(currentStep == 1){
            newNumber += "6";
            newNumberTv.append("6");
        }
    }

    @OnClick(R.id.numSeven)
    void seven(){
        if(currentStep == 1){
            newNumber += "7";
            newNumberTv.append("7");
        }
    }

    @OnClick(R.id.numEight)
    void eight(){
        if(currentStep == 1){
            newNumber += "8";
            newNumberTv.append("8");
        }
    }

    @OnClick(R.id.numNine)
    void nine(){
        if(currentStep == 1){
            newNumber += "9";
            newNumberTv.append("9");
        }
    }

    @OnClick(R.id.numZero)
    void zero(){
        if(currentStep == 1){
            newNumber += "0";
            newNumberTv.append("0");
        }
    }

    @OnClick(R.id.numDoubleZero)
    void doubleZero(){
        if(currentStep == 1){
            newNumber += "00";
            newNumberTv.append("00");
        }
    }

    @OnClick(R.id.numClearLast)
    void clearLastNum(){
        if(currentStep == 1){
            if(newNumber.length() != 0){
                newNumber = newNumber.substring(0,newNumber.length()-1);
                newNumberTv.setText(newNumber);
            }
        }
    }

    private boolean networkStatus(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isAvailable() && connectivityManager.getActiveNetworkInfo().isConnected());
    }

    private void showError(String message){
        if(toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(UpdatePage.this,message,Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
