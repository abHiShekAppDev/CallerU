package com.developer.abhishek.calleru.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.developer.abhishek.calleru.R;

public class NotificationUtils {

    private static final int UPDATED_CONTACT_NOTIFICATION_ID = 1138;
    private static final int UPDATED_CONTACT_PENDING_INTENT_ID = 3417;
    private static final String UPDATED_CONTACT_NOTIFICATION_CHANNEL_ID = "update_notification_channel";

    private static final String NOTIFICATION_TITLE_UPDATED = "Successfully updated your contact details";
    private static final String NOTIFICATION_TITLE_UPDATING = "Please wait ...";
    private static final String NOTIFICATION_MESSAGE_UPDATING = "Updating your contact details";

    public static void showUpdatedNotification(final String message, final Context context){
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    UPDATED_CONTACT_NOTIFICATION_CHANNEL_ID,
                    "my_channel",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        AsyncTask notifyBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                @SuppressLint("IconColors")
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, UPDATED_CONTACT_NOTIFICATION_CHANNEL_ID)
                        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setSmallIcon(R.drawable.ic_done_black_24dp)
                        .setContentTitle(NOTIFICATION_TITLE_UPDATED)
                        .setContentText(message)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setContentIntent(contentIntent(context))
                        .setAutoCancel(true);

                notificationManager.notify(UPDATED_CONTACT_NOTIFICATION_ID, notificationBuilder.build());

                return null;
            }
        };
        notifyBackgroundTask.execute();
    }

    public static void showUpdatingNotification(final Context context){
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    UPDATED_CONTACT_NOTIFICATION_CHANNEL_ID,
                    "my_channel",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        AsyncTask notifyBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                @SuppressLint("IconColors")
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, UPDATED_CONTACT_NOTIFICATION_CHANNEL_ID)
                        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setSmallIcon(R.drawable.ic_swap_vert_black_24dp)
                        .setContentTitle(NOTIFICATION_TITLE_UPDATING)
                        .setContentText(NOTIFICATION_MESSAGE_UPDATING)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setContentIntent(contentIntent(context))
                        .setAutoCancel(false)
                        .setOngoing(true);

                notificationManager.notify(UPDATED_CONTACT_NOTIFICATION_ID, notificationBuilder.build());
                return null;
            }
        };
        notifyBackgroundTask.execute();
    }

    private static PendingIntent contentIntent(Context context) {
        //  TODO -> 4 Implement PendingIntent
        Intent startActivityIntent = new Intent(Intent.ACTION_VIEW);
        return PendingIntent.getActivity(context, UPDATED_CONTACT_PENDING_INTENT_ID, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
