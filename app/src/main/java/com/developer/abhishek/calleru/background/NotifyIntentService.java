package com.developer.abhishek.calleru.background;

import android.app.IntentService;
import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.Nullable;

import com.developer.abhishek.calleru.R;
import com.developer.abhishek.calleru.models.Users;
import com.developer.abhishek.calleru.utils.NotificationUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class NotifyIntentService extends IntentService {

    public static final String ACTION_NOTIFY_OTHER_USER = "com.developer.abhishek.calleru.background.action.notify_other_user";
    public static final String NEW_NUMBER_PASS_INTENT = "new_number";
    public static final String CURRENT_NUMBER_PASS_INTENT = "current_number";
    public static final String ALL_CONTACT_PASS_INTENT = "all_contact_list";

    private final String ONE_SIGNAL_URL = "https://onesignal.com/api/v1/notifications";

    private String newNumber;
    private String currentNumber;
    private int mainThreadTaskCount = 0;
    private int backgroundTaskCount = 0;

    private String oneSignalAuthkey;
    private String oneSignalApiKey;

    private ArrayList<String> allContacts;

    public NotifyIntentService() {
        super("NOTIFY");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            newNumber = intent.getStringExtra(NEW_NUMBER_PASS_INTENT);
            currentNumber = intent.getStringExtra(CURRENT_NUMBER_PASS_INTENT);
            allContacts = intent.getStringArrayListExtra(ALL_CONTACT_PASS_INTENT);

            oneSignalApiKey = getApplicationContext().getResources().getString(R.string.oneSignalApiKey);
            oneSignalAuthkey = getApplicationContext().getResources().getString(R.string.oneSignalAuthKey);

            String action = intent.getAction();
            if (ACTION_NOTIFY_OTHER_USER.equals(action)) {
                if(newNumber != null && !newNumber.isEmpty() && currentNumber != null && allContacts != null && allContacts.size() > 0){
                    if(oneSignalAuthkey != null || oneSignalApiKey != null){
                        NotificationUtils.showUpdatingNotification(getApplicationContext());
                        findMyActiveContacts();
                    }
                }
            }
        }
    }

    private void findMyActiveContacts(){
        for(int i=0;i<allContacts.size();i++){
            if(!allContacts.get(i).equalsIgnoreCase(currentNumber)){
                Query query = FirebaseDatabase.getInstance().getReference("USERS")
                        .orderByChild("MobileNumber")
                        .equalTo(allContacts.get(i));
                query.addValueEventListener(valueEventListener);
            }
        }

        FirebaseDatabase.getInstance().getReference("USERS").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MobileNumber").setValue("+91"+newNumber);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users user = snapshot.getValue(Users.class);
                    sendNotification(user.getMobileNumber());
                    mainThreadTaskCount++;
                }
            }

            backgroundTaskCount++;
            if(backgroundTaskCount == allContacts.size()-1){
                String message = String.valueOf(mainThreadTaskCount)+getApplicationContext().getResources().getString(R.string.notificationToShow);
                NotificationUtils.showUpdatedNotification(message,getApplicationContext());
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void sendNotification(final String userTag) {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
                String jsonResponse;

                URL url = new URL(ONE_SIGNAL_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                httpURLConnection.setRequestProperty("Authorization", "Basic "+oneSignalApiKey);
                httpURLConnection.setRequestMethod("POST");

                String strJsonBody = "{"
                        + "\"app_id\": \""+oneSignalAuthkey+"\","

                        + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + userTag + "\"}],"

                        + "\"data\": {\"foo\": \"bar\"},"
                        + "\"contents\": {\"en\": \"" + "Hey, "+currentNumber.substring(currentNumber.length()-10)+" have updated their number to "+newNumber + "\"}"
                        + "}";


                System.out.println("strJsonBody:\n" + strJsonBody);

                byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                httpURLConnection.setFixedLengthStreamingMode(sendBytes.length);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(sendBytes);

                int httpResponse = httpURLConnection.getResponseCode();
                System.out.println("httpResponse: " + httpResponse);

                if (httpResponse >= HttpURLConnection.HTTP_OK
                        && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                    Scanner scanner = new Scanner(httpURLConnection.getInputStream(), "UTF-8");
                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                    scanner.close();
                } else {
                    Scanner scanner = new Scanner(httpURLConnection.getErrorStream(), "UTF-8");
                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                    scanner.close();
                }
                System.out.println("jsonResponse:\n" + jsonResponse);

            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NOTIFICATIONS").child(userTag);
        String notification = currentNumber.substring(3)+" "+newNumber;
        databaseReference.child(currentNumber).setValue(notification);
    }

}
