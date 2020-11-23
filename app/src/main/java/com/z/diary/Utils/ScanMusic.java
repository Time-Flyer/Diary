package com.z.diary.Utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.z.diary.Entity.Music;

import java.util.ArrayList;
import java.util.List;

public class ScanMusic {
    public static List<Music> getMusic(Context context){
        List<Music> list = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if(cursor != null){
            while (cursor.moveToNext()){
                Music music = new Music();
                music.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                //music.setMusic(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                //music.setSinger(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                music.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                if(music.getPath().contains(".mp3")) {
                    if (music.getSize() > 1000 * 800) {
                        String[] strings = music.getPath().split("/");
                        music.setMusic(strings[strings.length - 1]);
                        list.add(music);
                    }
                }
            }
            cursor.close();
        }
        return list;
    }

    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;
        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }
    }

}
