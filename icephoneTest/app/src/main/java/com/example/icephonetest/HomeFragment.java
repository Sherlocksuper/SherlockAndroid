package com.example.icephonetest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * created by 刘炳阳
 * description：首页
 */
public class HomeFragment extends Fragment {
    public View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        initHomeView();
        initHomeData();
        initHomeListener();
        return view;

    }

    public void initHomeData() {
        //把初始碎片设置成homelistfragment
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        HomeListFragment homeListFragment = new HomeListFragment();
        transaction.replace(R.id.fragment_container, homeListFragment);
        transaction.commit();

    }


    public void initHomeView() {
    }

    public void initHomeListener() {
    }

}