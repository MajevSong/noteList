package com.example.mucahit.mylistapp.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.mucahit.mylistapp.Data.dataBase;
import com.example.mucahit.mylistapp.Data.noteList;
import com.example.mucahit.mylistapp.R;
import com.example.mucahit.mylistapp.Utility.AlarmReceiver;
import com.example.mucahit.mylistapp.Utility.TimePickerFragment;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    Calendar calendar;

    EditText date, time, title, content;
    FloatingActionButton add_btn, delete_btn;
    Button imageBtn;
    Switch notificationSw;
    ImageView imgView;
    int alarmID = 0;

    DatePickerDialog datePickerDialog;
    private static int RESULT_LOAD_IMAGE = 1;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static final int PICK_FROM_GALLERY = 1;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    private static final int PERMISSION_REQUEST_CODE = 260;
    private static final int PERMISSION_REQUEST_WRITE = 250;
    private static final int SELECT_PICTURE = 100;
    private static final int SELECT_CAMERA = 200;
    private static final int REQUEST_APP_SETTINGS = 168;
    private static final String TAG = "MainActivity";
    String dbTITLE;
    String dbCONTENT;
    String dbDATE;
    String dbTIME;
    private static RecyclerView.Adapter adapter;
    public int check = 0;
    int i = 0, viewNotification,c = 0;
    public int viewId;
    public String viewTitle, viewContent, viewTime, viewDate;
    byte[] viewImage;
    static final String NOTIFICATION_COUNT = "notificationCount";
    int[] getID;
    public AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Intent alarmIntent;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_layout_second);

        // CustomAdapter Classı ile gönderilen "id", "title" gibi verileri buradan alabiliriz ve kullanabiliriz.

        final dataBase db = new dataBase(this);
        db.open();

        viewId = getIntent().getIntExtra("id", 0);

        imgView = findViewById(R.id.imgView);
        // layout içierisindeki nesnelerimizi burada tanımlayarak kullanabiliriz.
        time = (EditText) findViewById(R.id.txtTIME);
        date = (EditText) findViewById(R.id.txtDATE);
        content = (EditText) findViewById(R.id.txtCONTENT);
        title = (EditText) findViewById(R.id.txtTITLE);
        delete_btn = (FloatingActionButton) findViewById(R.id.fdeleteButton);
        add_btn = (FloatingActionButton) findViewById(R.id.faddButton);
        imageBtn = (Button) findViewById(R.id.imageBtn);
        notificationSw = (Switch) findViewById(R.id.notificationID);

        // ID numarasına göre verileri second layoutuna doldurur.

        if(viewImage != null){
            Bitmap bitmap = ((BitmapDrawable)imgView.getDrawable()).getBitmap();
            imgView.setImageBitmap(bitmap);
        }

        calendar = Calendar.getInstance();
        Bundle bundle = new Bundle();

        // Düzenleme layout ekranı
        if (viewId > 0) {

            bundle.putInt("id",viewId);

            viewTitle = getIntent().getStringExtra("title");
            viewContent = getIntent().getStringExtra("content");
            viewTime = getIntent().getStringExtra("time");
            viewDate = getIntent().getStringExtra("date");
            viewNotification = getIntent().getIntExtra("notification",0);

            delete_btn.setVisibility(View.VISIBLE);
            byte[] items = db.getPhoto(viewId);
            if(items != null){
                imgView.setImageBitmap(BitmapFactory.decodeByteArray(items,0,items.length));
            }

            if(viewNotification > 0){
                notificationSw.setChecked(true);
            }

            title.setText(viewTitle);
            content.setText(viewContent);
            time.setText(viewTime);
            date.setText(viewDate);

        } else
            delete_btn.setVisibility(View.INVISIBLE);

        // Ekleme işlemi yapıldığında güncellemeye tıklanırken switchde hiç bir değişiklik yapılmadığı takdirde yine aktif kalması için uyarının bu koda ihtiyaç duydum.
        if(notificationSw.isChecked()){
            check = 1;
            getNotification();
        }

        // Click olaylarının atanması
        date.setOnClickListener(this);
        add_btn.setOnClickListener(this);
        delete_btn.setOnClickListener(this);
        notificationSw.setOnCheckedChangeListener(this);
        imageBtn.setOnClickListener(this);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmIntent = new Intent(this, AlarmReceiver.class);

    }


    @Override
    public void onClick(View view) {

        dataBase db = new dataBase(getApplicationContext());

        // Ekleme işlemi
        if (view.getId() == add_btn.getId()) {

            dbTITLE = title.getText().toString();
            dbCONTENT = content.getText().toString();
            dbDATE = date.getText().toString();
            dbTIME = time.getText().toString();

            if(!dbTITLE.equals("") && !dbCONTENT.equals("") && !dbTIME.equals("") && !dbDATE.equals("")){
                noteList notes;
                if(imgView.getDrawable() == null){
                    notes = new noteList(i,dbTITLE, dbCONTENT, dbTIME, dbDATE, getNotification());

                }else{
                    Bitmap bitmap = ((BitmapDrawable)imgView.getDrawable()).getBitmap();
                    notes = new noteList(i,dbTITLE, dbCONTENT, dbTIME, dbDATE, getBitmapAsByteArray(bitmap),getNotification());

                }

                if (viewId > 0) {

                    Toast.makeText(getApplicationContext(), "Kayıt güncellendi ID = " + viewId, Toast.LENGTH_LONG).show();
                    Bitmap bitmap = ((BitmapDrawable)imgView.getDrawable()).getBitmap();
                    db.noteUpdate(viewId, dbTITLE, dbCONTENT, dbTIME, dbDATE, getBitmapAsByteArray(bitmap),getNotification());

                    if(getNotification()>0){
                        alarmIntent.putExtra("id",viewId);
                        setNotification();
                    }

                    Intent mintent = new Intent(SecondActivity.this, MainActivity.class);
                    startActivity(mintent);


                } else {

                    Toast.makeText(getApplicationContext(), "Yeni Kayıt yapıldı ID = " + viewId, Toast.LENGTH_LONG).show();
                    long id = db.addNote(notes);

                    if(getNotification()>0){
                        alarmIntent.putExtra("id",id);
                        setNotification();
                    }else
                        cancelNotifications();

                    Intent mintent = new Intent(SecondActivity.this, MainActivity.class);
                    startActivity(mintent);
                }

            }else{
                Toast.makeText(getApplicationContext(),"Boş kalan alanlar var",Toast.LENGTH_LONG).show();
            }

            db.closed();
        }

        // Silme İşlemi
        if (view.getId() == delete_btn.getId()) {
            Toast.makeText(getApplicationContext(), "Kayıt Silindi = " + viewId, Toast.LENGTH_LONG).show();
            Intent mintent = new Intent(SecondActivity.this, MainActivity.class);
            startActivity(mintent);
            db.noteDelete(viewId);
            db.closed();
        }

        // Tarih İşlemi
        if (view.getId() == date.getId()) {

            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

            // date picker dialog

            datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // set day of month , month and year value in the edit text
                            date.setText(dayOfMonth + "/"
                                    + (monthOfYear + 1) + "/" + year);


                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
            datePickerDialog.show();

        }

        // Resim ekleme
        if (view.getId() == imageBtn.getId()) {

                    new AlertDialog.Builder(SecondActivity.this)
                            .setMessage("lütfen seçin")
                            .setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_LONG).show();
                                    if(checkGalleryPermission()){
                                        openGalleryChooser();
                                    }else
                                        requestGalleryPermission();
                                }
                            })
                            .setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(getApplicationContext(),"cancel",Toast.LENGTH_LONG).show();
                                    if(checkCameraPermission() && checkGalleryPermission()){
                                        openCameraChooser();
                                    }else
                                        requestCameraPermission();

                                }
                            })
                            .create()
                            .show();
                    //openImageChooser();


        }

    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date.setText(sdf.format(calendar.getTime()));
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

        if (isChecked) {
            Toast.makeText(getApplicationContext(), "Uyarı Açık", Toast.LENGTH_LONG).show();
            check = 1;
        }

    }

    public void openPickerDialog(boolean is24hour){

        Calendar calendar = Calendar.getInstance();


    }

    public int getNotification(){
        return check;
    }

    // Resim seçme penceresini açar
    public void openGalleryChooser() {

         Intent intent = new Intent();
         intent.setType("image/*");
         intent.setAction(Intent.ACTION_GET_CONTENT);
         startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

    }

    public void openCameraChooser(){

         Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         startActivityForResult(takePicture,SELECT_CAMERA);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data != null){
            String path;
            Uri selectedImageUri = data.getData();

            if (resultCode == RESULT_OK) {
                if (requestCode == SELECT_PICTURE) {
                    // Get the url from data

                    if (null != selectedImageUri) {
                        // Get the path from the Uri
                        path = getPathFromURI(selectedImageUri);
                        Log.i(TAG, "Image Path : " + selectedImageUri);
                        // Set the image in ImageView
                        imgView.setImageURI(selectedImageUri);

                    }
                }

                if(requestCode == SELECT_CAMERA){

                    if (null != selectedImageUri) {
                        // Get the path from the Uri
                        path = getPathFromURI(selectedImageUri);
                        Log.i(TAG, "Image Path : " + selectedImageUri);
                        // Set the image in ImageView
                        imgView.setImageURI(selectedImageUri);

                    }
                }
            }

        }

    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    // Kamera izini istenir..
    private boolean checkGalleryPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private boolean checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }


    private void requestGalleryPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);

    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA},
                PERMISSION_REQUEST_WRITE);
    }

    // İzin isteği sonucuna göre cevap verecek.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    openGalleryChooser();
                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestGalleryPermission();

                                            }
                                        }
                                    });
                        }
                    }

                }
                break;

            case PERMISSION_REQUEST_WRITE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    openCameraChooser();
                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestCameraPermission();

                                            }
                                        }
                                    });
                        }
                    }

                }
                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SecondActivity.this)
                .setMessage(message)
                .setPositiveButton("ok ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_LONG).show();
                        goToSettings();
                    }
                })
                .setNegativeButton("Cancel",null)
                .create()
                .show();
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(myAppSettings, REQUEST_APP_SETTINGS);
    }

    @Override
    protected void onNewIntent( Intent intent ) {
        Log.i( TAG, "onNewIntent(), intent = " + intent );
        if (intent.getExtras() != null)
        {
            Log.i(TAG, "in onNewIntent = " + intent.getExtras().getString("test"));
        }
        super.onNewIntent( intent );
        setIntent( intent );
    }

    public void setNotification(){

        Calendar alarmStartTime = Calendar.getInstance();

        int year,month,day,hour,minute;
        String calDate,calTime;
        calDate = dbDATE.toString();
        calTime = dbTIME.substring(0,5).toString();

        String[] partDate = calDate.split("/");
        String[] partTime = calTime.split(":");

        day = Integer.valueOf(partDate[0]);
        month = Integer.valueOf(partDate[1])-1;
        year = Integer.valueOf(partDate[2]);
        hour = Integer.valueOf(partTime[0]);
        minute = Integer.valueOf(partTime[1]);

        alarmStartTime.set(Calendar.YEAR,year);
        alarmStartTime.set(Calendar.MONTH,month);
        alarmStartTime.set(Calendar.DAY_OF_MONTH,day);
        alarmStartTime.set(Calendar.HOUR_OF_DAY,hour);
        alarmStartTime.set(Calendar.MINUTE,minute);
        alarmStartTime.set(Calendar.SECOND,0);

        alarmIntent.putExtra("notetitle",dbTITLE);
        alarmIntent.putExtra("content",dbCONTENT);

        pendingIntent = PendingIntent.getBroadcast(  this, 0, alarmIntent, 0);

        alarmManager.set(alarmManager.RTC,alarmStartTime.getTimeInMillis(),pendingIntent);

        Log.i(TAG,"Alarms set");

    }

    public void cancelNotifications(){
        Log.i(TAG,"All notifications cancelled.");
        alarmManager.cancel(pendingIntent);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(this.getIntent().getExtras() != null){
            Log.i(TAG,"extras: " + this.getIntent().getExtras());
            getIntent().setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        }
    }

}
