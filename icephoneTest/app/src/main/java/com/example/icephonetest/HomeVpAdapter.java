package com.example.icephonetest;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class HomeVpAdapter extends FragmentPagerAdapter {

    private  FragmentManager fragmentManager;
    private List<Fragment> list;

    private HomeFragment homeFragment;
    private NoteDetailFragment noteDetailFragment;


    public HomeVpAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public HomeVpAdapter(List<Fragment> fragmentList,@NonNull FragmentManager fm) {
        super(fm);
        this.list = fragmentList;
        homeFragment = new HomeFragment();
        noteDetailFragment = new NoteDetailFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }


}
