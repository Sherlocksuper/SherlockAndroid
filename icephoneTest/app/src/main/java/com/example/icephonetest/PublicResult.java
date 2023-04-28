package com.example.icephonetest;

import java.io.Serializable;

public class PublicResult implements Serializable {
    Integer id;
    String title;
    String content;
    String statues;
    String date;
    String postScript;
    String kind;

    public PublicResult(String title, String content, String date,String kind) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.kind = kind;
    }
}
