package com.example.mucahit.mylistapp.Utility;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.mucahit.mylistapp.Activity.SecondActivity;

import static android.content.Intent.getIntent;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "ALARM";
    private String get_your_title;
    private String get_your_content;

    @Override
    public void onReceive(Context context, Intent intent) {

        //notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Log.i(TAG, "BroadcastReceiver has received alarm intent.");

        Intent service1 = new Intent(context, AlarmService.class);

        get_your_title = intent.getExtras().getString("notetitle");
        get_your_content = intent.getExtras().getString("content");

        int id = intent.getExtras().getInt("id");

        Log.e("What is the your title?", get_your_title);
        Log.e("What is the your cont?", get_your_content);

        service1.putExtra("notetitle",get_your_title);
        service1.putExtra("content",get_your_content);
        service1.putExtra("id",id);

        context.startService(service1);

    }



}