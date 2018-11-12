package com.developer.abhishek.calleru;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.abhishek.calleru.background.NotifyIntentService;
import com.developer.abhishek.calleru.models.Contacts;
import com.developer.abhishek.calleru.utils.NotificationUtils;
import com.developer.abhishek.calleru.viewModels.ContactsVM;
import com.google.firebase.auth.FirebaseAuth;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdatePage extends AppCompatActivity {

    @BindView(R.id.newNumberTv)
    TextView newNumberTv;
    @BindView(R.id.nextBtnAtUpdate)
    ImageButton nextBtn;

    private int currentStep = 1;
    private String newNumber = "";
    private String currentNumber;

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

        OneSignal.sendTag("User_ID", currentNumber);
    }

    @OnClick(R.id.nextBtnAtUpdate)
    void next(){
        if(currentStep == 1){
            if(!newNumber.isEmpty() && newNumber.length() == 10){
                currentStep++;
                newNumberTv.setText("Update to\n"+newNumber);
                nextBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_black_24dp));
            }else{
                Toast.makeText(UpdatePage.this,"Enter valid number !!!",Toast.LENGTH_SHORT).show();
            }

        }else{
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
}
