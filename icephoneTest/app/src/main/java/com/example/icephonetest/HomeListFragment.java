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

import android.util.Log;
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
        mDataList.add("2");
        mDataList.add("3");
        mDataList.add("4");
        mDataList.add("5");
        homeRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        homeListRecyclerAdapter = new HomeListRecyclerAdapter(mDataList, getContext(), this);

        homeRecyclerview.setAdapter(homeListRecyclerAdapter);
        // 创建一个 ItemTouchHelper.Callback 对象
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return Integer.MAX_VALUE;
            }

            @Override
            public float getSwipeEscapeVelocity(float defaultValue) {
                return Integer.MAX_VALUE;
            }
            // 重写 onMove() 方法，返回 false 表示不支持拖动
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                int mCurrentScrollX = 0;
                boolean mFirstInactive = false;
                int mDefaultScrollX = 180; //最大向左滑动阈值
                //dx是指改变的距离
                // 首次滑动时，记录下ItemView当前滑动的距离
                if (dX == 0) {
                    mCurrentScrollX = viewHolder.itemView.getScrollX();
                    mFirstInactive = true;
                }

                if (dX < 0) { // 向左滑动
                    //手指滑动
                    if (isCurrentlyActive) {
                        viewHolder.itemView.scrollTo(mCurrentScrollX + (int) -dX, 0);
                    } else { // 动画滑动
                        int mCurrentScrollXWhenInactive = 0;
                        float mInitXWhenInactive = 0;
                        if (mFirstInactive) {
                            mFirstInactive = false;
                            mCurrentScrollXWhenInactive = viewHolder.itemView.getScrollX();
                            mInitXWhenInactive = dX;
                        }
                        if (viewHolder.itemView.getScrollX() >= mDefaultScrollX) {
                            viewHolder.itemView.scrollTo(Math.max(mCurrentScrollX + (int) -dX, mDefaultScrollX), 0);
                        } else {
                            // 这里只能做距离的比例缩放，因为回到最初位置必须得从当前位置开始，dx不一定与ItemView的滑动距离相等
                            viewHolder.itemView.scrollTo((int) (mCurrentScrollXWhenInactive * dX / mInitXWhenInactive), 0);
                        }
                    }
                } else if (dX > 0) { // 向左滑动
                    viewHolder.itemView.scrollTo(0, 0);
                }
            }
            // 重写 onSwiped() 方法，处理滑动删除的逻辑
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // 获取滑动的位置
                int position = viewHolder.getAdapterPosition();
                // 从数据集中移除该项
                mDataList.remove(position);
                // 通知 Adapter 数据发生了变化
                homeListRecyclerAdapter.notifyItemRemoved(position);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(homeRecyclerview);
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