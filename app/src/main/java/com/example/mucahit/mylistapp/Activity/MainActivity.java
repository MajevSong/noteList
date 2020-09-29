package com.example.mucahit.mylistapp.Activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.mucahit.mylistapp.Data.dataBase;
import com.example.mucahit.mylistapp.R;
import com.example.mucahit.mylistapp.Utility.CustomAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<String> results = new ArrayList<String>();
    private FloatingActionButton mFab;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    dataBase db ;
    int mNotificationCount;
    static final String NOTIFICATION_COUNT = "notificationCount";
    private static final String TAG = "ALARM";
    public AlarmManager alarmManager;
    Intent alarmIntent;
    PendingIntent pendingIntent;
    TextView alarmTitle;

    int i = 0;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        db = new dataBase(MainActivity.this);

        // recyclerView oluşturularak
        recyclerView = (RecyclerView)
                findViewById(R.id.my_recycler_view);
        // recyclerView'in boyutunun değiştirilmeyeceği anlarda kullanılır optimizasyonu sağlar
        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        db.open();

        adapter = new CustomAdapter(db.listele(),getApplicationContext());
        recyclerView.setAdapter(adapter);

        mFab = (FloatingActionButton) findViewById(R.id.FAButton);
        mFab.setOnClickListener(this);

    }


    // main layout'da ki Button ve CardView Click Olayı...
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.FAButton:

                startActivity(new Intent(this,SecondActivity.class));

            default:
                break;
        }
    }

}


