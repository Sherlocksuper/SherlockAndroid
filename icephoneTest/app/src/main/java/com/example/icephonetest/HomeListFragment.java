package com.example.icephonetest;

import static android.content.Context.MODE_PRIVATE;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.recyclerview.touch.OnItemStateChangedListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class HomeListFragment extends Fragment {


    public RecyclerView homeRecyclerview;
    public List<PublicResult> mTotalDataList;
    public List<PublicResult> mShowDataList;
    public HomeListRecyclerAdapter homeListRecyclerAdapter;
    public Spinner kindSpinner;
    public ImageButton addbutton;
    public View viewHL;
    public FragmentManager manager;
    FragmentTransaction transaction;
    ArrayAdapter<String> spinnerAdapter;

    List<String> spinnerListTotal;
    String[] spinnerItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewHL = inflater.inflate(R.layout.fragment_home_list, container, false);
        initHomeListView();
        initHomeListData();
        initHomeListListener();
        return viewHL;


    }

    private void initHomeListView() {
        homeRecyclerview = viewHL.findViewById(R.id.homelist_recyclerview);
        addbutton = viewHL.findViewById(R.id.homelist_addbutton);
        kindSpinner = viewHL.findViewById(R.id.homelist_spinner);
        spinnerListTotal = new ArrayList<>();
        mTotalDataList = new ArrayList<>();
        mShowDataList = new ArrayList<>();
    }

    private void initHomeListData() {
        //定义manager以及trasaction
        manager = getParentFragmentManager();
        transaction = manager.beginTransaction();
        setupSpinner();
        setupRecyclerView();
    }

    private void initHomeListListener() {
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //替换fragment
                NoteEditFragment noteEditFragment = new NoteEditFragment();
                transaction.replace(R.id.fragment_container, noteEditFragment);
                transaction.commit();
            }
        });
        kindSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences preferences = getContext().getSharedPreferences(UsersCounts.usersCount, MODE_PRIVATE);
                String json = preferences.getString("publicResultList", "");
                Gson gson = new Gson();
                Type type = new TypeToken<List<PublicResult>>() {}.getType();
                mTotalDataList = gson.fromJson(json, type);

                if (mTotalDataList == null) mTotalDataList = new ArrayList<>();

                mShowDataList.clear();
                for (int i = 0; i < mTotalDataList.size(); i++) {
                    if (Objects.equals(mTotalDataList.get(i).kind, kindSpinner.getSelectedItem().toString())) {
                        mShowDataList.add(mTotalDataList.get(i));
                    }
                }
                homeListRecyclerAdapter = new HomeListRecyclerAdapter(mShowDataList, getContext(), manager);
                homeRecyclerview.setAdapter(homeListRecyclerAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //设置下拉框
    private void setupSpinner() {
        SharedPreferences preferences = getContext().getSharedPreferences(UsersCounts.usersCount, MODE_PRIVATE);
        Set<String> stringSet = preferences.getStringSet("spinnerList", new HashSet<String>()); // 读取Set数据


        if (stringSet == null) stringSet = new HashSet<>();

        List<String> spinnerList = new ArrayList<String>(stringSet); // 将Set转换为List

        spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kindSpinner.setAdapter(spinnerAdapter);
    }

    //设置recyclerview
    private void setupRecyclerView() {
        // 从SharedPreferences中检索JSON字符串
        SharedPreferences preferences = getContext().getSharedPreferences(UsersCounts.usersCount, MODE_PRIVATE);
        String json = preferences.getString("publicResultList", "");
        Gson gson = new Gson();
        Type type = new TypeToken<List<PublicResult>>() {
        }.getType();
        mTotalDataList = gson.fromJson(json, type);

        if (mTotalDataList == null) mTotalDataList = new ArrayList<>();

        for (int i = 0; i < mTotalDataList.size(); i++) {
            if (kindSpinner.getSelectedItem() == null) break;
            if (Objects.equals(mTotalDataList.get(i).kind, kindSpinner.getSelectedItem().toString())) {
                mShowDataList.add(mTotalDataList.get(i));
            }
        }

        homeRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        homeListRecyclerAdapter = new HomeListRecyclerAdapter(mShowDataList, getContext(), manager);
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
