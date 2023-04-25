package com.example.icephonetest;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

public class MyFragment extends Fragment {
    public View view;
    public TextView testToLogin;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my, container, false);
        initView();
        setOnClickListener();
        return view;
    }
    public void initView(){
        testToLogin = view.findViewById(R.id.myfragment_turnToLogin);
    }

    public void setOnClickListener(){
        testToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //前往 LoginActivity进行测试
                Intent intent = new Intent(getContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}