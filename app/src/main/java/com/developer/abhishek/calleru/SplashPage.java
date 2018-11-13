package com.developer.abhishek.calleru;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;

public class SplashPage extends AppCompatActivity {

    private final int ALL_PERMISSION_GRANT = 1;
    private boolean isPermissionAllow = false;

    private final String[] allPermissions = {Manifest.permission.CALL_PHONE,Manifest.permission.READ_CALL_LOG,Manifest.permission.WRITE_CALL_LOG,Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);
        hideBar();

        if(!checkPermission(allPermissions)){
            ActivityCompat.requestPermissions(this, allPermissions,ALL_PERMISSION_GRANT);
            isPermissionAllow = false;
        }else {
            isPermissionAllow = true;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isPermissionAllow){
                    if(FirebaseAuth.getInstance().getCurrentUser() == null){
                        startActivity(new Intent(SplashPage.this,LoginPage.class));
                        finish();
                    }else{
                        startActivity(new Intent(SplashPage.this,HomePage.class));
                        finish();
                    }
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashPage.this);
                    builder.setMessage(getResources().getString(R.string.permissionErr))
                            .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                   finish();
                                }
                            });
                }
            }
        },3000);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case ALL_PERMISSION_GRANT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(checkPermission(permissions)){
                        if(FirebaseAuth.getInstance().getCurrentUser() == null){
                            startActivity(new Intent(SplashPage.this,LoginPage.class));
                            finish();
                        }else{
                            startActivity(new Intent(SplashPage.this,HomePage.class));
                            finish();
                        }
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashPage.this);
                    builder.setMessage(getResources().getString(R.string.permissionErr))
                            .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });
                }
                return;
            }
        }
    }

    private boolean checkPermission(String... permissions){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            for(String permission : permissions){
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void hideBar(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
