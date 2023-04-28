package com.example.icephonetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.media.Ringtone;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManagerKindsActivity extends AppCompatActivity {

    public ImageView image_backRow;
    public RecyclerView kindRecyclerView;

    public ManagesKindsRecyclerAdapter managesKindsRecyclerAdapter;

    public Button addButton;
    public List<String> kindsDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_kinds);
        initView();
        initData();
        initClickListener();

    }

    public void initView() {
        image_backRow = findViewById(R.id.managerKind_backrow);
        kindRecyclerView = findViewById(R.id.itemKind_RecyclerView);
        addButton = findViewById(R.id.manager_addKinds);
    }

    public void initData() {
        SharedPreferences preferences = this.getSharedPreferences("MyAppData", MODE_PRIVATE);
        Set<String> stringSet = preferences.getStringSet("spinnerList", new HashSet<>());
        //获取到了spinnerList的Adapter
        kindsDatas = new ArrayList<>(stringSet);
        setRecyclerView();
    }

    public void initClickListener() {
        image_backRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputServer = new EditText(ManagerKindsActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(ManagerKindsActivity.this);

                builder.setTitle("请输入您要添加的分类名称").setView(inputServer)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString();
                        addKinds(inputName);

                    }
                });
                builder.show();
            }
        });
    }

    public void setRecyclerView() {
        kindRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        managesKindsRecyclerAdapter = new ManagesKindsRecyclerAdapter(kindsDatas);

        kindRecyclerView.setAdapter(managesKindsRecyclerAdapter);
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
                int mDefaultScrollX = 160; //最大向右滑动阈值
                int mCurrentScrollX = 0;
                View mainView = viewHolder.itemView.findViewById(R.id.kinditem_mainpart);
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
                        ValueAnimator animator = ValueAnimator.ofInt(mainView.getScrollX(), 0);
                        animator.setDuration(200);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                int scrollX = (int) animation.getAnimatedValue();
                                mainView.scrollTo(scrollX, 0);
                            }
                        });
                        animator.start();
                    } else if (dX > 0) { // 向右滑动
                        //手指滑动
                        if (isCurrentlyActive) {
                            mCurrentScrollX += dX;
                            mainView.scrollTo(-mCurrentScrollX, 0);
                            if (mCurrentScrollX >= mDefaultScrollX) {
                                mainView.scrollTo(-mDefaultScrollX, 0);
                            }
                        }
                        if (mCurrentScrollX > 30) {
                            ValueAnimator animator = ValueAnimator.ofInt(mainView.getScrollX(), -mDefaultScrollX);
                            animator.setDuration(100);
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    int scrollX = (int) animation.getAnimatedValue();
                                    mainView.scrollTo(scrollX, 0);
                                }
                            });
                            animator.start();
                        }
                    }
                }
            }


        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(kindRecyclerView);
    }

    private void addKinds(String inputName) {
        SharedPreferences preferences = this.getSharedPreferences("MyAppData", MODE_PRIVATE);
        Set<String> stringSet = preferences.getStringSet("spinnerList", new HashSet<String>());
        // 将Set转换为List
        kindsDatas = new ArrayList<String>(stringSet);
        kindsDatas.add(inputName);
        managesKindsRecyclerAdapter = new ManagesKindsRecyclerAdapter(kindsDatas);

        kindRecyclerView.setAdapter(managesKindsRecyclerAdapter);
        stringSet = new HashSet<String>(kindsDatas);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet("spinnerList", stringSet); // 存储Set数据
        editor.apply(); // 提交修改
    }
}