package com.z.diary.Entity;

import android.app.Application;

import java.util.List;

public class MusicList extends Application {
    private List<Music> list;

    public void onCreate(){
        super.onCreate();
    }

    public List<Music> getList() {
        return list;
    }

    public void setList(List<Music> list) {
        this.list = list;
    }
}
