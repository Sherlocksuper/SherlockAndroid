package com.example.icephonetest;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SearchFragment extends Fragment  {


    public  int b;
    public SearchView searchView;
    public View view;
    public RecyclerView resultRecyclerview;

    public HomeListRecyclerAdapter resultAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 使用inflater.inflate方法加载布局文件，并返回一个View对象
        view = inflater.inflate(R.layout.fragment_search, container, false);
        initSearchView();
        initListener();
        return view;
    }

    public void initSearchView() {
        searchView = view.findViewById(R.id.search_view);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        resultRecyclerview = view.findViewById(R.id.search_recycler);

    }

    public void initSearchData() {

    }

    public void initListener() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doSearch(query);
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.requestFocusFromTouch();
                int[] location = new int[2];
                searchView.getLocationInWindow(location);
                float startY = location[1];
                if (startY > 200) {
                    ObjectAnimator animatorSearch = ObjectAnimator.ofFloat(searchView, "translationY", -startY + 80);
                    animatorSearch.setDuration(500);
                    animatorSearch.start();
                }
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                ObjectAnimator animator = ObjectAnimator.ofFloat(searchView, "translationY", 0);
                animator.setDuration(500);
                animator.start();
                return false;
            }
        });
    }

    public void doSearch(String targets) {
        SharedPreferences preferences = getContext().getSharedPreferences(UsersCounts.usersCount, Context.MODE_PRIVATE);
        String json = preferences.getString("publicResultList", "");
        Gson gson = new Gson();
        Type type = new TypeToken<List<PublicResult>>() {
        }.getType();
        List<PublicResult> results = gson.fromJson(json, type);

        List<PublicResult> finalResult = new ArrayList<>();
        for (PublicResult result : results) {
            if (result.title.contains(targets)) finalResult.add(result);
        }

        resultRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        HomeFragment homeFragment = (HomeFragment) getParentFragmentManager().getFragments().get(0);
        FragmentManager manager = homeFragment.getChildFragmentManager();

        resultAdapter = new HomeListRecyclerAdapter(finalResult, getContext(), manager);

        resultRecyclerview.setAdapter(resultAdapter);

    }



}