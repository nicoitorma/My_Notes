package com.nicanoritorma.mynotes;

public class DataModel {
    int id;
    String title;
    String note;
    String tag;

    public DataModel(int id, String title, String note, String tag) {
        this.id = id;
        this.title = title;
        this.note = note;
        this.tag = tag;
    }

    public DataModel() {
        //Required empty constructor
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
