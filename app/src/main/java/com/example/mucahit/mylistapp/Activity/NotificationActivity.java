package com.example.mucahit.mylistapp.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.example.mucahit.mylistapp.R;

public class NotificationActivity extends AppCompatActivity {

    private Button mRemoveToDoButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        TextView txtReminder, txtContent;
        String title, content;

        txtReminder = (TextView) findViewById(R.id.toDoReminderTextViewBody);

        title = getIntent().getStringExtra("notetitle");
        content = getIntent().getStringExtra("content");
        txtContent = (TextView)findViewById(R.id.toDoReminderTextViewContent);

        txtReminder.setText(title);
        txtContent.setText(content);

    }

}
