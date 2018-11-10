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
    @BindView(R.id.altNumberTv)
    TextView alternativeNumberTv;
    @BindView(R.id.nextBtnAtUpdate)
    ImageButton nextBtn;

    private int currentStep = 1;
    private String newNumber = "";
    private String currentNumber;
    private String altNumber = "";

    //

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
            if(!newNumber.isEmpty()){
                currentStep++;
               // Picasso.get().load(R.drawable.ic_menu_slideshow).into(nextBtn);
                nextBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_slideshow));
            }

        }else{
            if(!newNumber.isEmpty()){
                Toast.makeText(UpdatePage.this,"Done",Toast.LENGTH_SHORT).show();

                ContactsVM contactsVM = ViewModelProviders.of(this).get(ContactsVM.class);
                contactsVM.getAllContactListLiveData().observe(this, new Observer<ArrayList<String>>() {
                    @Override
                    public void onChanged(@Nullable ArrayList<String> strings) {
                        if(strings != null){
                            Intent intent = new Intent(UpdatePage.this,NotifyIntentService.class);
                            intent.setAction(NotifyIntentService.ACTION_NOTIFY_OTHER_USER);
                            intent.putExtra(NotifyIntentService.NEW_NUMBER_PASS_INTENT,newNumber);
                            intent.putExtra(NotifyIntentService.ALT_NUMBER_PASS_INTENT,altNumber);
                            intent.putExtra(NotifyIntentService.CURRENT_NUMBER_PASS_INTENT,currentNumber);
                            intent.putStringArrayListExtra(NotifyIntentService.ALL_CONTACT_PASS_INTENT,strings);
                            startService(intent);
                        }
                    }
                });

            }
        }
    }

    @OnClick(R.id.newNumberTv)
    void setCursorOnNewNumberTv(){
        currentStep = 1;
       // Picasso.get().load(R.drawable.ic_menu_camera).into(nextBtn);
        nextBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_camera));
    }

    @OnClick(R.id.altNumberTv)
    void setCursorOnAltNumberTv(){
        currentStep = 2;
       // Picasso.get().load(R.drawable.ic_menu_slideshow).into(nextBtn);
        nextBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_slideshow));
    }

    @Override
    public void onBackPressed() {
        if(currentStep == 1){
            super.onBackPressed();
        }else{
            currentStep--;
            //Picasso.get().load(R.drawable.ic_menu_camera).into(nextBtn);
            nextBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_camera));
        }

    }

    @OnClick(R.id.numOne)
    void one(){
        if(currentStep == 1){
            newNumber += "1";
            newNumberTv.append("1");
        }else{
            altNumber += "1";
            alternativeNumberTv.append("1");
        }
    }

    @OnClick(R.id.numTwo)
    void two(){
        if(currentStep == 1){
            newNumber += "2";
            newNumberTv.append("2");
        }else{
            altNumber += "2";
            alternativeNumberTv.append("2");
        }
    }

    @OnClick(R.id.numThree)
    void three(){
        if(currentStep == 1){
            newNumber += "3";
            newNumberTv.append("3");
        }else{
            altNumber += "3";
            alternativeNumberTv.append("3");
        }
    }

    @OnClick(R.id.numFour)
    void four(){
        if(currentStep == 1){
            newNumber += "4";
            newNumberTv.append("4");
        }else{
            altNumber += "4";
            alternativeNumberTv.append("4");
        }
    }

    @OnClick(R.id.numFive)
    void five(){
        if(currentStep == 1){
            newNumber += "5";
            newNumberTv.append("5");
        }else{
            altNumber += "5";
            alternativeNumberTv.append("5");
        }
    }

    @OnClick(R.id.numSix)
    void six(){
        if(currentStep == 1){
            newNumber += "6";
            newNumberTv.append("6");
        }else{
            altNumber += "6";
            alternativeNumberTv.append("6");
        }
    }

    @OnClick(R.id.numSeven)
    void seven(){
        if(currentStep == 1){
            newNumber += "7";
            newNumberTv.append("7");
        }else{
            altNumber += "7";
            alternativeNumberTv.append("7");
        }
    }

    @OnClick(R.id.numEight)
    void eight(){
        if(currentStep == 1){
            newNumber += "8";
            newNumberTv.append("8");
        }else{
            altNumber += "8";
            alternativeNumberTv.append("8");
        }
    }

    @OnClick(R.id.numNine)
    void nine(){
        if(currentStep == 1){
            newNumber += "9";
            newNumberTv.append("9");
        }else{
            altNumber += "9";
            alternativeNumberTv.append("9");
        }
    }

    @OnClick(R.id.numZero)
    void zero(){
        if(currentStep == 1){
            newNumber += "0";
            newNumberTv.append("0");
        }else{
            altNumber += "0";
            alternativeNumberTv.append("0");
        }
    }

    @OnClick(R.id.numDoubleZero)
    void doubleZero(){
        if(currentStep == 1){
            newNumber += "00";
            newNumberTv.append("00");
        }else{
            altNumber += "00";
            alternativeNumberTv.append("00");
        }
    }

    @OnClick(R.id.numClearLast)
    void clearLastNum(){
        if(currentStep == 1){
            if(newNumber.length() != 0){
                newNumber = newNumber.substring(0,newNumber.length()-1);
                newNumberTv.setText(newNumber);
            }
        }else{
            if(altNumber.length() != 0){
                altNumber = altNumber.substring(0,altNumber.length()-1);
                alternativeNumberTv.setText(altNumber);
            }
        }
    }
}
