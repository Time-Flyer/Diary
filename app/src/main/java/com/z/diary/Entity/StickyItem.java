package com.z.diary.Entity;

public class StickyItem extends Diary {
    private String year;
    private String month;

    public StickyItem(String date, String weather, String title, String content) {
        super(date, weather, title, content);
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
