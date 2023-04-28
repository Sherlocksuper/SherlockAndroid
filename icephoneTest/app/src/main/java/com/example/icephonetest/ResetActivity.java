package com.example.icephonetest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.BaseKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ResetActivity extends AppCompatActivity {

    public Button button_resetPasswords;

    public ImageView backrows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        initView();
        initClickListener();
    }

    public void initView() {
        backrows = findViewById(R.id.reset_backrows);
    }

    public void initClickListener() {
        backrows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}