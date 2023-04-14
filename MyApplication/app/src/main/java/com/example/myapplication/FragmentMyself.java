package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class FragmentMyself extends Fragment {
    public FragmentMyself() {

    }

    private BottomNavigationView bottomNavigationView;
    public ViewPager viewPager;
    public List<Fragment> fragmentList = new ArrayList<>();
    boolean hasLogged = false;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //获取登录状态
        SharedPreferences pref = getContext().getSharedPreferences("data", MODE_PRIVATE);
        hasLogged = pref.getBoolean("hasLogged", false);

        if (hasLogged) {
            view = inflater.inflate(R.layout.fragment_myself_logged, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_myself_notlog, container, false);
        }
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        if (hasLogged) {
            haslogged_outLog();
        } else {
            notlog_toLogin();
        }

    }

    //未登录界面点击前往登录界面
    public void notlog_toLogin() {
        TextView tv_notlog = view.findViewById(R.id.myself_notlogged);

        tv_notlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void haslogged_outLog() {
        Button bu_outlog = view.findViewById(R.id.my_logout);

        bu_outlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putBoolean("hasLogged", false);
                editor.apply();
                Toast.makeText(view.getContext(), "已经退出登录", Toast.LENGTH_SHORT).show();




                Intent intent = new Intent(view.getContext(),MainActivity.class);
                startActivity(intent);
                MainActivity.instance.finish();
            }
        });
    }
}
