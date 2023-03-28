package com.example.cloudnotetrack.Model;

public class Notee {
    String note_name;
    String note_detail;
    String id;

    public Notee(String id,String note_name, String note_detail ) {
        this.note_name = note_name;
        this.note_detail = note_detail;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getNote_detail() {
        return note_detail;
    }

    public String getNote_name() {
        return note_name;
    }
}
