package com.example.kartikeya_pc.forum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Category_Database extends SQLiteOpenHelper{

    private static final String TAG = Category_Database.class.getSimpleName();

    private static final String DATABASE_NAME = "register";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "categories";
    private String CAT_ID = "id";
    private String CAT_NAME = "category_name";
    private String CAT_DESCRIPTION = "category_description";



    public Category_Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + CAT_ID + " INTEGER PRIMARY KEY," + CAT_NAME + " VARCHAR," + CAT_DESCRIPTION + " VARCHAR);";
        db.execSQL(CREATE_TABLE);

        Log.d(TAG, "Database table created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXIST " + TABLE_NAME);
        onCreate(db);

    }
    public void addCategory(String category_name,String category_description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CAT_NAME,category_name);
        values.put(CAT_DESCRIPTION,category_description);
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        Log.d(TAG, "NEW CATEGORY IS ADDED INTO DATABASE" + id);
    }

    public List<HashMap<String, String>> getCategory(){
        HashMap<String,String> user = new HashMap<String,String>();
        ArrayList<HashMap<String, String>> recordsList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + CAT_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()) {
            do {
                // int productId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(fieldProductId)));
                String objectName = cursor.getString(cursor.getColumnIndex(CAT_NAME));
                user.put("topic",objectName);
                recordsList.add(user);
            }while (cursor.moveToNext());}
        cursor.close();
        db.close();
        Log.d(TAG,"Fetching category information" + user.toString());

        return recordsList;
    }

}
