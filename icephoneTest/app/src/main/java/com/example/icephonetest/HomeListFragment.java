package com.example.icephonetest;

import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class HomeListFragment extends Fragment implements HomeListRecyclerAdapter.recyclerItemTouchHelper {

    public RecyclerView homeRecyclerview;
    public List<String> mDataList;
    public HomeListRecyclerAdapter homeListRecyclerAdapter;

    String[] spinnerList;
    public Spinner kindSpinner;
    public ImageButton addbutton;
    public View viewHL;
    FragmentManager manager;
    FragmentTransaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewHL = inflater.inflate(R.layout.fragment_home_list, container, false);
        initHomeListView();
        initHomeListData();
        initHomeListListener();
        return viewHL;
    }

    public void initHomeListView() {
        homeRecyclerview = viewHL.findViewById(R.id.homelist_recyclerview);
        addbutton = viewHL.findViewById(R.id.homelist_addbutton);
        kindSpinner = viewHL.findViewById(R.id.homelist_spinner);
    }

    public void initHomeListData() {
        //定义manager以及trasaction
        manager = getParentFragmentManager();
        transaction = manager.beginTransaction();

        //Spinner设置
        setupSpinner();
        //RecyclerView设置
        setupRecyclerView();
        //itemTouchHelper

    }

    public void initHomeListListener() {
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //替换fragment
                NoteEditFragment noteEditFragment = new NoteEditFragment();
                transaction.replace(R.id.fragment_container, noteEditFragment);
                transaction.commit();
            }
        });

    }

    //设置下拉框
    private void setupSpinner() {
        //给下拉框设置适配器等
        spinnerList = new String[]{"select item", "item1", "item2", "item3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kindSpinner.setAdapter(adapter);
    }

    //设置recyclerview
    private void setupRecyclerView() {
        mDataList = new ArrayList<>();
        mDataList.add("1");
        mDataList.add("1");
        mDataList.add("1");
        mDataList.add("1");
        mDataList.add("1");
        homeRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        homeListRecyclerAdapter = new HomeListRecyclerAdapter(mDataList, getContext(), this);

        homeRecyclerview.setAdapter(homeListRecyclerAdapter);
    }

    //实现recyclerview的监听
    @Override
    public void onRecyclerItemClick(int position) {
        // 处理点击事件
        NoteDetailFragment fragment = new NoteDetailFragment();
//       Bundle bundle = new Bundle();
//       bundle.putInt("item_position", position);
//       fragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}