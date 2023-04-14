package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    public ViewPager viewPager;
    public List<Fragment> fragmentList = new ArrayList<>();
    public static MainActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        //getSupportActionBar().hide();
        initInfo();

    }

    public void initInfo() {


        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setItemIconTintList(null);

        viewPager = findViewById(R.id.view_pager);

        fragmentList.add(new FragmentCinema());
        fragmentList.add(new FragmentMyself());


        FragmentPagAdapter adapter = new FragmentPagAdapter(fragmentList, getSupportFragmentManager());
        viewPager.setAdapter(adapter);


        //emnu文字颜色
        Resources resources = getResources();
        @SuppressLint("ResourceType")
        ColorStateList csl = resources.getColorStateList(R.drawable.menu_item_color);
        bottomNavigationView.setItemTextColor(csl);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int selectedID = item.getItemId();
                switch (selectedID) {
                    case R.id.tab_Cinema:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.tab_Myself:
                        viewPager.setCurrentItem(1);
                        break;
                }
                return true;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {bottomNavigationView.getMenu().getItem(position).setChecked(true);}
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }
}