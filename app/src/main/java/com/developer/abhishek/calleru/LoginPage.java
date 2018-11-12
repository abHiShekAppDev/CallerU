package com.developer.abhishek.calleru;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginPage extends AppCompatActivity {

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

    private String mobileNumber;
    private String verificationCode;
    private String name;
    private int currentStep = 1;

    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        ButterKnife.bind(this);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(LoginPage.this,HomePage.class));
            finish();
        }
    }

    @OnClick(R.id.nextBtn)
    void next(){
        if(currentStep == 1){
            mobileNumber = mobileNoEt.getText().toString().trim();
            if(mobileNumber.length() == 10){
                mobileNumber = "+91"+mobileNumber;
            }else{
                Toast.makeText(LoginPage.this,"Enter valid Mobile Number !!!",Toast.LENGTH_SHORT).show();
            }

            if(mobileNumber.length() == 13){
                progressBar.setVisibility(View.VISIBLE);
                verificationCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        progressBar.setVisibility(View.GONE);
                       startActivity(new Intent(LoginPage.this,HomePage.class));
                       finish();
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        progressBar.setVisibility(View.GONE);
                        if (e instanceof FirebaseTooManyRequestsException) {
                            Toast.makeText(LoginPage.this, "Too many request !! please try again later ...", Toast.LENGTH_SHORT).show();
                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(LoginPage.this, "Invalid Authentication ... ", Toast.LENGTH_SHORT).show();
                        } else if (e instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(LoginPage.this, "User collision ...", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginPage.this, "Something went wrong ...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        progressBar.setVisibility(View.GONE);
                        phoneVerificationId = s;
                        currentStep++;
                        mobileNoLayout.setVisibility(View.GONE);
                        verifyCodeLayout.setVisibility(View.VISIBLE);
                        infoText.setText("Enter the code Received");
                        nextBtn.setText("Next");

                    }
                };
                PhoneAuthProvider.getInstance().verifyPhoneNumber(mobileNumber, 60, TimeUnit.SECONDS, LoginPage.this, verificationCallBacks);
            }else{
                Toast.makeText(LoginPage.this,"Enter valid Mobile Number !!!",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }else if(currentStep == 2){
            verificationCode = verifyCodeEt.getText().toString().trim();
            if(verificationCode.length() == 6){
                progressBar.setVisibility(View.VISIBLE);
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId, verificationCode);
                firebaseAuth.signInWithCredential(credential).addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            currentStep++;
                            infoText.setText("Enter your Name");
                            nextBtn.setText("Done");
                            nameLayout.setVisibility(View.VISIBLE);
                            verifyCodeLayout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }).addOnFailureListener(LoginPage.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginPage.this, "Please Enter valid code ...", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }else{
                Toast.makeText(LoginPage.this,"Enter valid Code !!!",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

        }else if(currentStep == 3){
            currentStep++;
            name = nameEt.getText().toString().trim();
            if(name != null && !name.isEmpty()){
                progressBar.setVisibility(View.VISIBLE);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("USERS").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                databaseReference.child("Name").setValue(name);
                databaseReference.child("MobileNumber").setValue(mobileNumber);
                databaseReference.child("Id").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(LoginPage.this,HomePage.class));
                finish();
            }else{
                Toast.makeText(LoginPage.this,"Enter your name",Toast.LENGTH_SHORT).show();
            }

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
            infoText.setText("Enter Your Mobile Number");
        }
    }
}
