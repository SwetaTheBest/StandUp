package com.swetajain.standup;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ToggleButton alarmToggle;
    private String mStringToast;
    private NotificationManager mNotificationManager;
    public static final int NOTIFICATION_ID = 301;
    public static final String CHANNEL_ID = "com.swetajain.standup.ALARM_CHANNEL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmToggle = findViewById(R.id.alarm_toggle_button);
        Intent notifyIntent = new Intent(this, MyReceiver.class);
        final PendingIntent pendingNotifyIntent =
                PendingIntent.getActivity(this,
                        NOTIFICATION_ID,
                        notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        //alarm manager
        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel();

        boolean alarmUp = (PendingIntent.getBroadcast(this,
                NOTIFICATION_ID,
                notifyIntent, PendingIntent.FLAG_NO_CREATE) != null);
        alarmToggle.setChecked(alarmUp);
        alarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alarmToggle.setBackgroundColor(Color.GREEN);
                    //     deliverStandUpNotification(MainActivity.this);

                    long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
                    long triggerTime = SystemClock.elapsedRealtime() + repeatInterval;
                    if (alarmManager != null) {
                        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                triggerTime,
                                repeatInterval,
                                pendingNotifyIntent);
                    }

                    mStringToast = "Alarm is On!";
                } else {
                    alarmToggle.setBackgroundColor(Color.RED);
                    mStringToast = "Alarm is Off";
                    if (alarmManager != null) {
                        alarmManager.cancel(pendingNotifyIntent);
                    }
                    mNotificationManager.cancelAll();
                }

                Toast.makeText(MainActivity.this, mStringToast, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    new NotificationChannel(CHANNEL_ID,
                            "Stand UP",
                            NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notifies user to stand Up every 15 minutes.");
            notificationChannel.getLockscreenVisibility();
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

    }

}
