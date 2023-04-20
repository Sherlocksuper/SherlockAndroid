package com.example.icephonetest;

import android.animation.ValueAnimator;
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
import android.widget.Toast;

import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class HomeListFragment extends Fragment  {

    public RecyclerView homeRecyclerview;
    public List<String> mDataList;
    public HomeListRecyclerAdapter homeListRecyclerAdapter;

    String[] spinnerList;
    public Spinner kindSpinner;
    public ImageButton addbutton;
    public View viewHL;
   public FragmentManager manager;
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
        homeListRecyclerAdapter = new HomeListRecyclerAdapter(mDataList, getContext(), manager);

        homeRecyclerview.setAdapter(homeListRecyclerAdapter);
        // 创建一个 ItemTouchHelper.Callback 对象
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            // 获取滑动阈值
            @Override
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return Integer.MAX_VALUE;
            }

            // 获取滑动速度
            @Override
            public float getSwipeEscapeVelocity(float defaultValue) {
                return Integer.MAX_VALUE;
            }

            // 禁止拖拽
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            // 处理滑动删除操作
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            // 处理滑动事件
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                boolean mFirstInactive = false; //第一次滑动
                int mDefaultScrollX = 160; //最大向左滑动阈值
                int mCurrentScrollX = 0;

                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) { // 拖动状态
                    viewHolder.itemView.scrollBy((int) -dX, 0);

                    if (viewHolder.itemView.getScrollX() < 0) {
                        viewHolder.itemView.scrollTo(0, 0);
                    }

                    if (viewHolder.itemView.getScrollX() > mDefaultScrollX) {
                        viewHolder.itemView.scrollTo(mDefaultScrollX, 0);
                    }
                } else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) { // 滑动状态
                    if (dX < 0) { // 向左滑动
                        //手指滑动
                        if (isCurrentlyActive) {
                            mCurrentScrollX += -dX;
                            viewHolder.itemView.scrollTo(mCurrentScrollX, 0);
                            if (mCurrentScrollX >= mDefaultScrollX) {
                                viewHolder.itemView.scrollTo(mDefaultScrollX, 0);
                            }
                        }
                        if (mCurrentScrollX > 30) {
                            ValueAnimator animator = ValueAnimator.ofInt(viewHolder.itemView.getScrollX(), mDefaultScrollX);
                            animator.setDuration(100);
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    int scrollX = (int) animation.getAnimatedValue();
                                    viewHolder.itemView.scrollTo(scrollX, 0);
                                }
                            });
                            animator.start();
                        }
                    } else if (dX > 0) { // 向右滑动
                        ValueAnimator animator = ValueAnimator.ofInt(viewHolder.itemView.getScrollX(), 0);
                        animator.setDuration(200);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                int scrollX = (int) animation.getAnimatedValue();
                                viewHolder.itemView.scrollTo(scrollX, 0);
                            }
                        });
                        animator.start();
                    }
                }

            }

        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(homeRecyclerview);
    }
}