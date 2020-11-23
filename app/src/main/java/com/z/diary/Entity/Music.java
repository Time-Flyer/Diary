package com.z.diary.Entity;

import com.z.diary.Utils.Cn2Spell;

public class Music implements Comparable<Music> {
    private String path;
    private String music;
    private String singer;
    private int duration;
    private long size;
    private String headerWord;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
        this.headerWord = Cn2Spell.getPinYinFirstLetter(music).toUpperCase();
        char ch = this.headerWord.charAt(0);
        if(!(ch >= 'A' && ch <= 'Z')){
            this.headerWord = "#";
        }
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getHeaderWord() {
        return headerWord;
    }

    @Override
    public int compareTo(Music music) {
        return this.headerWord.compareTo(music.getHeaderWord());
    }
}
