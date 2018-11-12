package com.developer.abhishek.calleru;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;

import java.util.concurrent.TimeUnit;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginPage extends AppCompatActivity {

    private static final String NUMBER_SAVED_INST_KEY = "saved_number";
    private static final String CODE_SAVED_INST_KEY = "saved_code";
    private static final String NAME_SAVED_INST_KEY = "saved_name";
    private static final String CURRENT_STEP_SAVED_INST_KEY = "saved_current_step";
    private static final String PHONE_VERIFICATION_ID_SAVED_INST_KEY = "phone_verification_id";

    @BindView(R.id.displayInfoTv)
    TextView infoText;
    @BindView(R.id.mobileNoLayout)
    LinearLayout mobileNoLayout;
    @BindView(R.id.verifyCodeLayout)
    LinearLayout verifyCodeLayout;
    @BindView(R.id.nameLayout)
    LinearLayout nameLayout;
    @BindView(R.id.mobileNoEt)
    EditText mobileNoEt;
    @BindView(R.id.verificationCodeEt)
    EditText verifyCodeEt;
    @BindView(R.id.nameEt)
    EditText nameEt;
    @BindView(R.id.nextBtn)
    Button nextBtn;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindString(R.string.enterNumber)
    String enterNumberStr;
    @BindString(R.string.enterCode)
    String enterCodeStr;
    @BindString(R.string.enterName)
    String enterNameStr;
    @BindString(R.string.sendCode)
    String sendCodeStr;
    @BindString(R.string.next)
    String nextStr;
    @BindString(R.string.done)
    String doneStr;
    @BindString(R.string.networkError)
    String networkErrorErr;
    @BindString(R.string.allFieldReq)
    String allFieldReqErr;
    @BindString(R.string.enterValidNumber)
    String enterValidNumberErr;
    @BindString(R.string.enterValidCode)
    String enterValidCodeErr;

    private String mobileNumber;
    private String verificationCode;
    private String name;
    private String phoneVerificationId;
    private int currentStep = 1;

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        ButterKnife.bind(this);

        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(NUMBER_SAVED_INST_KEY)){
                mobileNumber = savedInstanceState.getString(NUMBER_SAVED_INST_KEY);
            }
            if(savedInstanceState.containsKey(CODE_SAVED_INST_KEY)){
                verificationCode = savedInstanceState.getString(CODE_SAVED_INST_KEY);
            }
            if(savedInstanceState.containsKey(NAME_SAVED_INST_KEY)){
                name = savedInstanceState.getString(NAME_SAVED_INST_KEY);
            }
            if(savedInstanceState.containsKey(CURRENT_STEP_SAVED_INST_KEY)){
                currentStep = savedInstanceState.getInt(CURRENT_STEP_SAVED_INST_KEY);
            }
            if(savedInstanceState.containsKey(PHONE_VERIFICATION_ID_SAVED_INST_KEY)){
                phoneVerificationId = savedInstanceState.getString(PHONE_VERIFICATION_ID_SAVED_INST_KEY);
            }

        }else if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(LoginPage.this,HomePage.class));
            finish();
        }

        if(currentStep == 2){
            mobileNoLayout.setVisibility(View.GONE);
            verifyCodeLayout.setVisibility(View.VISIBLE);
            nameLayout.setVisibility(View.GONE);
            infoText.setText(enterCodeStr);
            nextBtn.setText(nextStr);
        }else if(currentStep == 3){
            mobileNoLayout.setVisibility(View.GONE);
            verifyCodeLayout.setVisibility(View.GONE);
            nameLayout.setVisibility(View.VISIBLE);
            infoText.setText(enterNameStr);
            nextBtn.setText(doneStr);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mobileNumber != null){
            outState.putString(NAME_SAVED_INST_KEY,mobileNumber);
        }
        if(verificationCode != null){
            outState.putString(NAME_SAVED_INST_KEY,verificationCode);
        }
        if(name != null){
            outState.putString(NAME_SAVED_INST_KEY,name);
        }
        if(phoneVerificationId != null){
            outState.putString(PHONE_VERIFICATION_ID_SAVED_INST_KEY,phoneVerificationId);
        }
        outState.putInt(CURRENT_STEP_SAVED_INST_KEY,currentStep);
    }

    @OnClick(R.id.nextBtn)
    void next(){
        if(networkStatus()){
            progressBar.setVisibility(View.VISIBLE);
            if(currentStep == 1){
                mobileNumber = mobileNoEt.getText().toString().trim();
                handleStepOne();
            }else if(currentStep == 2){
                verificationCode = verifyCodeEt.getText().toString().trim();
                handleStepTwo();
            }else if(currentStep == 3){
                name = nameEt.getText().toString().trim();
                handleStepThird();
            }
        }else{
            showError(networkErrorErr);
        }
    }

    @Override
    public void onBackPressed() {
        if(currentStep == 1){
            super.onBackPressed();
        }else {
            currentStep--;
            nameLayout.setVisibility(View.GONE);
            mobileNoLayout.setVisibility(View.VISIBLE);
            infoText.setText(enterNumberStr);
            nextBtn.setText(sendCodeStr);
        }
    }

    private void handleStepOne(){
        if(mobileNumber.length() == 10){
            mobileNumber = "+91"+mobileNumber;
        }else{
            showError(enterValidNumberErr);
            return;
        }

        PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(LoginPage.this,HomePage.class));
                finish();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                showError(e.getMessage());
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                progressBar.setVisibility(View.GONE);
                phoneVerificationId = s;
                currentStep++;

                mobileNoLayout.setVisibility(View.GONE);
                verifyCodeLayout.setVisibility(View.VISIBLE);
                infoText.setText(enterCodeStr);
                nextBtn.setText(nextStr);

            }
        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber(mobileNumber, 60, TimeUnit.SECONDS, LoginPage.this, verificationCallBacks);
    }

    private void handleStepTwo(){
        if(verificationCode.length() == 6){
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId, verificationCode);
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        currentStep++;
                        infoText.setText(enterNameStr);
                        nextBtn.setText(doneStr);
                        nameLayout.setVisibility(View.VISIBLE);
                        verifyCodeLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }).addOnFailureListener(LoginPage.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showError(enterValidCodeErr);
                }
            });
        }else{
            showError(enterValidCodeErr);
        }
    }

    private void handleStepThird(){
        if(name != null && !name.isEmpty()){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("USERS").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            databaseReference.child("Name").setValue(name);
            databaseReference.child("MobileNumber").setValue(mobileNumber);
            databaseReference.child("Id").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
            OneSignal.sendTag("User_ID", mobileNumber);

            progressBar.setVisibility(View.GONE);
            startActivity(new Intent(LoginPage.this,HomePage.class));
            finish();
        }else{
            showError(allFieldReqErr);
        }
    }

    private boolean networkStatus(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isAvailable() && connectivityManager.getActiveNetworkInfo().isConnected());
    }

    private void showError(String message){
        progressBar.setVisibility(View.GONE);
        if(toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(LoginPage.this,message,Toast.LENGTH_SHORT);
        toast.show();
    }
}
