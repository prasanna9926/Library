package com.testapp.lib;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "libDB";
    private static final int DB_VERSION = 5;

    public DBHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        // This line will ensure the database is created
        getWritableDatabase();
    }

    public void onCreate(SQLiteDatabase db) {
        String recordsQuery = "CREATE TABLE records ("
                + "rec_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "title TEXT,"
                + "author TEXT,"
                + "description TEXT)"; // Fixed typo here

        String memberQuery = "CREATE TABLE members ("
                + "mem_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT,"
                + "date TEXT)";

        db.execSQL(recordsQuery);
        db.execSQL(memberQuery);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS records");
        db.execSQL("DROP TABLE IF EXISTS members");
        onCreate(db);
    }

    public void addRecord(String title, String author, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("author", author);
        values.put("description", desc); // Fixed typo here
        db.insert("records", null, values);
        db.close();
    }

    public void addMembers(String name, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("date", date);
        db.insert("members", null, values);
        db.close();
    }
}
