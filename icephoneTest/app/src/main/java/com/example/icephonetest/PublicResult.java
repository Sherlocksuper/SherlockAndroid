package com.example.icephonetest;

import java.io.Serializable;

public class PublicResult  implements Serializable {
    public PublicResult( String title, String content,  String date,String kind) {

        this.title = title;
        this.content = content;
        this.date = date;
        this.kind = kind;
        //this.postScript = postScript;
    }

    Integer id;
    String title;
    String content;
    String kind;
    String date;
    //String postScript;
}
