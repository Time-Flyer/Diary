package com.z.diary.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.z.diary.Entity.Diary;

import java.util.ArrayList;
import java.util.List;

public class DiaryHelper extends SQLiteOpenHelper {
    public DiaryHelper(@Nullable Context context) {
        super(context, "diary.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table diary(id integer primary key autoincrement," +
                "date varchar(10), weather varchar(8), title varchar(32), content text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long insert(Diary diary){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", diary.getDate());
        values.put("weather", diary.getWeather());
        values.put("title", diary.getTitle());
        values.put("content", diary.getContent());
        long id  = db.insert("diary", null, values);
        db.close();
        return id;
    }

    public long insert(String date, String weather, String title, String content) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("weather", weather);
        values.put("title", title);
        values.put("content", content);
        long id  = db.insert("diary", null, values);
        db.close();
        return id;
    }

    public int update(long id, String weather, String title, String content){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("weather", weather);
        values.put("content", content);
        int number =  db.update("diary", values, "id=?", new String[]{id + ""});
        db.close();
        return number;
    }

    public int delete(long id){
        SQLiteDatabase db = getWritableDatabase();
        int number = db.delete("diary", "id=?", new String[]{id + ""});
        db.close();
        return number;
    }

    public List<Diary> findAll(){
        List<Diary> diaryList = null;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query("diary", null, null, null, null, null, "id desc");
        if (cursor != null) {
            diaryList = new ArrayList<>();
            Diary diary;
            while (cursor.moveToNext()) {
                diary = new Diary();
                diary.setId(cursor.getLong(0));
                diary.setDate(cursor.getString(1));
                diary.setWeather(cursor.getString(2));
                diary.setTitle(cursor.getString(3));
                diary.setContent(cursor.getString(4));
                diaryList.add(diary);
            }
            cursor.close();
        }
        db.close();
        return diaryList;
    }

    public boolean find(String date){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from diary where date=?", new String[]{date});
        boolean result = cursor.moveToNext();
        cursor.close();
        db.close();
        return result;
    }
}
