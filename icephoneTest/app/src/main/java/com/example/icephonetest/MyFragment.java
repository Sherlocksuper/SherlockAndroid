package com.example.icephonetest;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyFragment extends Fragment {
    public View view;
    public TextView tv_toManageKinds;

    public ImageView iv_settings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my, container, false);
        initView();
        setOnClickListener();
        return view;
    }

    public void initView() {
        tv_toManageKinds = view.findViewById(R.id.my_toManageKinds);
        iv_settings = view.findViewById(R.id.my_settings);
    }

    public void setOnClickListener() {
        tv_toManageKinds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ManagerKindsActivity.class);
                startActivity(intent);
            }
        });

        iv_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AccountsManageActivity.class);
                startActivity(intent);
            }
        });
    }
}