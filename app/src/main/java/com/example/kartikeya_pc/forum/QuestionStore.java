package com.example.kartikeya_pc.forum;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;


public class QuestionStore extends SQLiteOpenHelper {



    private static final String TAG = QuestionStore.class.getSimpleName();

    private static final String DATABASE_NAME = "questions";
    private static final int    DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "ask";
    private static final String KEY_ID = "id";
    private static final String KEY_Question = "question";
    private static final String KEY_ANSWER = "answer"; // correct answer

    public QuestionStore(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_Question + " TEXT," +  KEY_ANSWER + " TEXT);";
        db.execSQL(CREATE_TABLE);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXIST " + TABLE_NAME);
        onCreate(db);

    }
    public void addQuestion(String question){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_Question,question);

        db.insert(TABLE_NAME,null,values);
        db.close();

    }
    public HashMap<String,String> getQuestion() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM" + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            user.put("question", cursor.getString(1));
        }
        cursor.close();
        db.close();
        Log.d(TAG, "Fetching user information" + user.toString());
        return user;
    }


}
