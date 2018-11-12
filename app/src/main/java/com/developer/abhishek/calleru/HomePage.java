package com.developer.abhishek.calleru;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.abhishek.calleru.fragments.ContactScreen;
import com.developer.abhishek.calleru.fragments.DiallingScreen;
import com.developer.abhishek.calleru.fragments.NotificationScreen;
import com.developer.abhishek.calleru.fragments.SearchScreen;
import com.developer.abhishek.calleru.fragments.UpdateScreen;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DiallingScreen.changeInFragment {

    @BindView(R.id.toolbarAtHP)
    Toolbar toolbar;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navViewHeader)
    NavigationView navigationView;
    @BindView(R.id.bottomNavViewAtHP)
    BottomNavigationView bottomNavigationView;
    private TextView headerTextView;

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    private boolean isToShowDialPad = false;
    private boolean isToDial = false;
    private String dialledNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ButterKnife.bind(this);

      //  headerTextView = findViewById(R.id.headerTv);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        if (checkPermission(android.Manifest.permission.CALL_PHONE)) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavViewListener);
        navigationView.setNavigationItemSelectedListener(this);

        String myNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

       // headerTextView.setText(myNumber.substring(0,4)+" "+myNumber.substring(5));

        getSupportFragmentManager().beginTransaction().add(R.id.contentFLAtHP,new UpdateScreen()).commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavViewListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottomNavDial:
                    if (isToDial && dialledNumber != null && !dialledNumber.isEmpty() && checkPermission(Manifest.permission.CALL_PHONE)) {
                        String dial = "tel:" + dialledNumber;
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                    }else{
                        if(isToShowDialPad){
                            isToShowDialPad = false;
                        }else{
                            isToShowDialPad = true;
                        }

                        DiallingScreen diallingScreen = new DiallingScreen();
                        diallingScreen.setToShowDialPad(isToShowDialPad);
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentFLAtHP,diallingScreen).commit();
                    }
                    break;

                case R.id.bottomNavContacts:
                    // Resetting DialPad Screen
                    isToDial = false;
                    isToShowDialPad = true;
                    dialledNumber = null;

                    getSupportFragmentManager().beginTransaction().replace(R.id.contentFLAtHP,new ContactScreen()).commit();
                    break;

                case R.id.bottomNavSearch:
                    // Resetting DialPad Screen
                    isToDial = false;
                    isToShowDialPad = true;
                    dialledNumber = null;

                    getSupportFragmentManager().beginTransaction().replace(R.id.contentFLAtHP,new SearchScreen()).commit();
                    break;

                case R.id.bottomNavUpdate:
                    // Resetting DialPad Screen
                    isToDial = false;
                    isToShowDialPad = true;
                    dialledNumber = null;

                    getSupportFragmentManager().beginTransaction().replace(R.id.contentFLAtHP,new UpdateScreen()).commit();
                    break;

                case R.id.bottomNavNotification:
                    // Resetting DialPad Screen
                    isToDial = false;
                    isToShowDialPad = true;
                    dialledNumber = null;

                    getSupportFragmentManager().beginTransaction().replace(R.id.contentFLAtHP,new NotificationScreen()).commit();
                    break;


            }
            return true;
        }

    };

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            isToDial = false;
            isToShowDialPad = true;
            dialledNumber = null;

            getSupportFragmentManager().beginTransaction().replace(R.id.contentFLAtHP,new UpdateScreen()).commit();
        } else if (id == R.id.nav_rate) {
                rateApp();
        } else if (id == R.id.nav_share) {
                shareApp();
        } else if (id == R.id.nav_Exit) {
            finish();
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomePage.this,LoginPage.class));
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE :
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                }
                return;
        }
    }

    @Override
    public void onDialScreenChange(boolean isToDial, boolean isToShowDialPad,String dialledNumber) {
        this.isToShowDialPad = isToShowDialPad;
        this.isToDial = isToDial;
        this.dialledNumber = dialledNumber;
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.dialPad);
        if(isToDial){
            //            menu.findItem(R.id.dialPad).setTitle("Call Now");
            Log.d("Dial : ","true");
        }else{
            //            menu.findItem(R.id.dialPad).setTitle("Dial");
            Log.d("Dial : ","false");
        }
    }

    public void rateApp(){

    }

    public void shareApp(){

    }
}
