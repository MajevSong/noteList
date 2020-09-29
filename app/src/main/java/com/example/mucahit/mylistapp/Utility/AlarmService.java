package com.example.mucahit.mylistapp.Utility;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.mucahit.mylistapp.Activity.NotificationActivity;
import com.example.mucahit.mylistapp.R;


public class AlarmService extends IntentService {

    private static final int NOTIFICATION_ID = 1;
    private static final String TAG = "ALARM";
    private NotificationManager notificationManager;
    private PendingIntent pendingIntent;
    private String txtTitle, txtContent;
    Intent alarmIntent;

    public AlarmService() {
        super("Notification Service!");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onHandleIntent(Intent intent) {
        // don't notify if they've played in last 24 hr

        txtTitle = intent.getStringExtra("notetitle");
        txtContent = intent.getStringExtra("content");

        Log.i(TAG,"Alarm Service has started." );
        Context context = this.getApplicationContext();
        notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);

        Intent mIntent = new Intent(this, NotificationActivity.class);
        int id = intent.getExtras().getInt("id");

        mIntent.putExtra("notetitle",txtTitle);
        mIntent.putExtra("content", txtContent);

        pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Resources res = this.getResources();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Uyarı Ayarlanıyor.
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_done_white_24dp)
                .setTicker(res.getString(R.string.app_name))
                .setAutoCancel(true)
                .setContentTitle(txtTitle)
                .setContentText(res.getString(R.string.content))
                .build();


        notificationManager.notify(NOTIFICATION_ID, builder.build());
        Log.i(TAG,"Notifications sent.");

        // Alarm Ayarlanıyor.
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + this.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(this, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}