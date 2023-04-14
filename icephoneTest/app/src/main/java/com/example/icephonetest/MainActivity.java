package com.example.icephonetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

/**
 * created by 刘炳阳
 * description：首页
 */
public class MainActivity extends AppCompatActivity {
    String[] data = {"Apple", "Banana", "Cherry", "Durian"};
    public List<String> spinnerlist = new ArrayList<>();

    public Spinner spinner;

    public BottomNavigationView bottomNavigationView;
    public ViewPager viewPager;
    public MyFragment myFragment;
    public HomeFragment homeFragment;
    public SearchFragment searchFragment;
    public List<Fragment> fragmentList;
    public HomeVpAdapter homeVpAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMainView();
        initMainData();
        initMainListener();
    }

    private void initMainView() {
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        viewPager = findViewById(R.id.view_pager);

        myFragment = new MyFragment();
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        fragmentList = new ArrayList<>();
        fragmentList.add(homeFragment);
        fragmentList.add(searchFragment);
        fragmentList.add(myFragment);

        //定义viewpager适配器
        homeVpAdapter = new HomeVpAdapter(getSupportFragmentManager());
        homeVpAdapter = new HomeVpAdapter(fragmentList,getSupportFragmentManager());

    }

    private void initMainData() {

        bottomNavigationView.setItemIconTintList(null);
        Resources resources = getResources();
        @SuppressLint("ResourceType")
        ColorStateList csl = resources.getColorStateList(R.drawable.selected_itemcolor);
        bottomNavigationView.setItemTextColor(csl);

        //设置viewpager适配器
        viewPager.setAdapter(homeVpAdapter);
        viewPager.setOffscreenPageLimit(3);

    }



    private void initMainListener() {
        //bottomNavigationView点击监听器
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.tab_Main:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.tab_Search:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.tab_My:
                        viewPager.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });

        //设置viewPager的滑动监听器
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //根据页面的位置来切换菜单项
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}