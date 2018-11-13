package com.developer.abhishek.calleru;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.developer.abhishek.calleru.fragments.ContactScreen;
import com.developer.abhishek.calleru.fragments.DiallingScreen;
import com.developer.abhishek.calleru.fragments.NotificationScreen;
import com.developer.abhishek.calleru.fragments.SearchScreen;
import com.developer.abhishek.calleru.fragments.UpdateScreen;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DiallingScreen.changeInFragment {

    private final static String PACKAGE_NAME = "com.developer.abhishek.calleru";
    private static final String CURRENT_FRAG_CODE_SAVED_KEY = "current_fragment";

    @BindView(R.id.toolbarAtHP)
    Toolbar toolbar;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navViewHeader)
    NavigationView navigationView;
    @BindView(R.id.bottomNavViewAtHP)
    BottomNavigationView bottomNavigationView;

    private boolean isToShowDialPad = false;
    private boolean isToDial = false;
    private String dialledNumber = null;

    private MenuItem menuItem;

    private int currentFragmentCode = 1;    /* 1 = Update Screen
                                               2 = Search Screen
                                               3 = Dial Pad Screen
                                               4 = Contacts Screen
                                               5 = Notification Screen  */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigationDrawerOpen, R.string.navigationDrawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavViewListener);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentFLAtHP,new UpdateScreen()).commit();
        }else if(savedInstanceState.containsKey(CURRENT_FRAG_CODE_SAVED_KEY)){
            currentFragmentCode = savedInstanceState.getInt(CURRENT_FRAG_CODE_SAVED_KEY);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_FRAG_CODE_SAVED_KEY,currentFragmentCode);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            resetDialPad();
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

    private final BottomNavigationView.OnNavigationItemSelectedListener bottomNavViewListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottomNavDial:
                    if (isToDial && dialledNumber != null && !dialledNumber.isEmpty()) {
                        String dial = "tel:" + dialledNumber;
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                    }else{
                        isToShowDialPad = !isToShowDialPad;

                        DiallingScreen diallingScreen = new DiallingScreen();
                        diallingScreen.setToShowDialPad(isToShowDialPad);
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentFLAtHP,diallingScreen).commit();
                    }
                    currentFragmentCode = 3;
                    break;

                case R.id.bottomNavContacts:
                    if(currentFragmentCode != 4){
                        if(currentFragmentCode == 3){
                            resetDialPad();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentFLAtHP,new ContactScreen()).commit();
                        currentFragmentCode = 4;
                    }
                    break;

                case R.id.bottomNavSearch:
                    if(currentFragmentCode != 2){
                        if(currentFragmentCode == 3){
                            resetDialPad();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentFLAtHP,new SearchScreen()).commit();
                        currentFragmentCode = 2;
                    }
                    break;

                case R.id.bottomNavUpdate:
                    if(currentFragmentCode != 1){
                        if(currentFragmentCode == 3){
                            resetDialPad();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentFLAtHP,new UpdateScreen()).commit();
                        currentFragmentCode = 1;
                    }
                    break;

                case R.id.bottomNavNotification:
                    if(currentFragmentCode != 5){
                        if(currentFragmentCode == 3){
                            resetDialPad();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentFLAtHP,new NotificationScreen()).commit();
                        currentFragmentCode = 5;
                    }
                    break;
            }
            menuItem = item;
            return true;
        }

    };

    @Override
    public void onDialScreenChange(boolean isToDial, boolean isToShowDialPad,String dialledNumber) {
        if(this.isToDial && !isToDial){
            if(menuItem != null && menuItem.getItemId() == R.id.bottomNavDial){
                    menuItem.setIcon(getResources().getDrawable(R.drawable.ic_dialpad_black_24dp));
            }
        }else if(!this.isToDial && isToDial){
            if(menuItem != null && menuItem.getItemId() == R.id.bottomNavDial){
                menuItem.setIcon(getResources().getDrawable(R.drawable.ic_call_black_24dp));
            }
        }

        this.isToShowDialPad = isToShowDialPad;
        this.isToDial = isToDial;
        this.dialledNumber = dialledNumber;
    }

    private void resetDialPad(){
        isToDial = false;
        isToShowDialPad = false;
        dialledNumber = null;
        if(menuItem != null && menuItem.getItemId() == R.id.bottomNavDial){
            menuItem.setIcon(getResources().getDrawable(R.drawable.ic_dialpad_black_24dp));
        }

    }

    private void rateApp() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PACKAGE_NAME)));
    }

    private void shareApp(){
        String toBeShare = "*MegaShow*\nFollow the link to download\n\n";
        toBeShare += Uri.parse("https://play.google.com/store/apps/details?id=" + PACKAGE_NAME);
        Intent sharingIntent = new Intent();
        sharingIntent.setAction(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, toBeShare);
        startActivity(sharingIntent);
    }
}
