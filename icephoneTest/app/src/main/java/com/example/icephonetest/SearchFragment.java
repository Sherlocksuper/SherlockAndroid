package com.example.icephonetest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SearchFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 使用inflater.inflate方法加载布局文件，并返回一个View对象
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;

    }
}