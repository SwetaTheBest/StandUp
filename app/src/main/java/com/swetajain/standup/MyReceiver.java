package com.swetajain.standup;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.swetajain.standup.MainActivity.CHANNEL_ID;
import static com.swetajain.standup.MainActivity.NOTIFICATION_ID;

public class MyReceiver extends BroadcastReceiver {
    private NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        deliverStandUpNotification(context);
    }

    private void deliverStandUpNotification(Context context) {

        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingContentIntent =
                PendingIntent.getActivity(context,
                        NOTIFICATION_ID,
                        contentIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_walk)
                .setContentTitle("Stand Up Alarm")
                .setContentText("Please stand up, you have been sitting for 15 mins.")
                .setContentIntent(pendingContentIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());

    }
}
