package com.example.mucahit.mylistapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class dataBase extends SQLiteOpenHelper{

    private static final String database_name = "database_list";
    private static final String list_table = "note_table";
    private static final int database_version = 1;
    private String list_ID = "id";
    private String list_TITLE = "title";
    private String list_TIME = "time";
    private String list_DATE = "date";
    private String list_CONTENT = "content";
    private String list_IMAGE = "image";
    private String list_NOTIFICATION = "notification";
    public List<noteList>liste;

    int i = 0;

    private SQLiteDatabase db;

    public dataBase(Context context){

        super(context, database_name, null,database_version);
    }

    public void open(){
        db = this.getWritableDatabase();
    }

    public void closed(){

        this.close();
    }

    public void onCreate(SQLiteDatabase db){

        String sql_createNote = "CREATE TABLE " + list_table + "("
                + list_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + list_TITLE + " TEXT,"
                + list_CONTENT + " TEXT," + list_TIME + " TEXT," + list_DATE +  " TEXT,"
                + list_IMAGE + " BLOB," + list_NOTIFICATION + " INTEGER" + ")";

        db.execSQL(sql_createNote);
    }

    public void onUpgrade(SQLiteDatabase db, int OldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + list_table);
    }

    public long addNote(noteList note){
        SQLiteDatabase db = this.getWritableDatabase();
        long id = 0;

        try{
            ContentValues cv = new ContentValues();

            cv.put("title", note.getTitle());
            cv.put("content",note.getContent());
            cv.put("time", note.getTime());
            cv.put("date",note.getDate());
            cv.put("image",note.getImage());
            cv.put("notification",note.getNotification());

            id = db.insert(list_table, null, cv);
            return id;

        }catch (Exception e){
            Log.e("SqlException ---",e.getMessage());
            return id;
        }

    }

    public List<noteList> listele(){
        String kolonlar[] = {"id","title","content","time","date","image","notification"};

        liste= new ArrayList<noteList>();

        Cursor cur = db.query("note_table", kolonlar,null,null,null,null,null);
        cur.moveToFirst();

        try{
            while(!cur.isAfterLast()){
                int id = cur.getInt(0);
                String title = cur.getString(1);
                String content = cur.getString(2);
                String time = cur.getString(3);
                String date = cur.getString(4);
                byte[] image = cur.getBlob(5);
                int notification = cur.getInt(6);
                noteList k = new noteList(id,title,content,time,date,image,notification);
                liste.add(k);

                cur.moveToNext();

            }
        }catch (Exception e){
            Log.d("SQL Exception",e.getMessage());
        }

        cur.close();
        return liste;

    }

    public void noteUpdate(int id, String title, String content, String time, String date, byte[] image, int notification){

        db = this.getWritableDatabase(); // Bundan dolayı hata almıştım...
        ContentValues updatedValues = new ContentValues();
        String[] idArray = {String.valueOf(id)};

        updatedValues.put("title",title);
        updatedValues.put("content",content);
        updatedValues.put("time",time);
        updatedValues.put("date",date);
        updatedValues.put("image",image);
        updatedValues.put("notification",notification);
        db.update(list_table,updatedValues, "id = ?" ,idArray);

    }

    public void noteDelete(int id){

        db = this.getWritableDatabase();
        db.delete(list_table,list_ID + "=" + id,null);

    }

    public byte[] getPhoto(int id){

        String selectQuery = "SELECT image FROM " + list_table + " WHERE id="+id;
        db = this.getReadableDatabase();
        Cursor cur = db.rawQuery(selectQuery, null);

        cur.moveToFirst();
        byte[] image = null;

        image = cur.getBlob(0);

        return image;
    }

}
