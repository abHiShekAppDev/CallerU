package com.developer.abhishek.calleru.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import com.developer.abhishek.calleru.HomePage;
import com.developer.abhishek.calleru.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationWidget extends AppWidgetProvider {

    private static RemoteViews views;
    private static String notification = null;

    private static Context context;
    private static int[] appWidgets;
    private static AppWidgetManager appWidgetManager;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle bundle = appWidgetManager.getAppWidgetOptions(appWidgetId);
        views = new RemoteViews(context.getPackageName(), R.layout.notification_widget);

        loadWidget();

        Intent startIntent = new Intent(context, HomePage.class);
        PendingIntent startPendingIntent = PendingIntent.getActivity(context, 0, startIntent, 0);
        views.setOnClickPendingIntent(R.id.startApp, startPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        NotificationWidget.context = context;
        this.appWidgets = appWidgetIds;
        this.appWidgetManager = appWidgetManager;
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        updateAppWidget(context, appWidgetManager, appWidgetId);
    }

    public static void loadWidget() {
        loadRecentNotification();
        try {
            if (notification == null) {
                views.setViewVisibility(R.id.notificationLayout, View.GONE);
                views.setViewVisibility(R.id.infoLayout, View.VISIBLE);
                views.setTextViewText(R.id.errorText,context.getResources().getString(R.string.wait));
            }else if(notification.equalsIgnoreCase(context.getResources().getString(R.string.noNewNotification))){
                views.setViewVisibility(R.id.notificationLayout, View.GONE);
                views.setViewVisibility(R.id.infoLayout, View.VISIBLE);
                views.setTextViewText(R.id.errorText,context.getResources().getString(R.string.noNewNotification));
            } else {
                views.setViewVisibility(R.id.notificationLayout, View.VISIBLE);
                views.setViewVisibility(R.id.infoLayout, View.GONE);

                String currentNumber = "+91 " + notification.substring(0, 11);
                String newNumber = "+91 " + notification.substring(11);
                views.setTextViewText(R.id.currenNumberInNoti, currentNumber);
                views.setTextViewText(R.id.newNumberInNotif, newNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadRecentNotification() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NOTIFICATIONS").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        notification = snapshot.getValue(String.class);
                    }
                    for (int appWidgetId : appWidgets) {
                        updateAppWidget(context, appWidgetManager, appWidgetId);
                    }
                } else {
                    notification = context.getResources().getString(R.string.noNewNotification);
                    for (int appWidgetId : appWidgets) {
                        updateAppWidget(context, appWidgetManager, appWidgetId);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

