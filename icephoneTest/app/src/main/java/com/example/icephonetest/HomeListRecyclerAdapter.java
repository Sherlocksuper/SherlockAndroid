package com.example.icephonetest;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

public class HomeListRecyclerAdapter extends RecyclerView.Adapter<HomeListRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<String> mDataList;// 声明数据列表
    private FragmentManager fragmentManager;

    // 构造函数，传入数据列表
    public HomeListRecyclerAdapter(List<String> dataList, Context context, FragmentManager manager) {
        mDataList = dataList;
        fragmentManager = manager;
        this.context = context;
    }

    // 创建 ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view, this);
    }

    // 绑定 ViewHolder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String data = mDataList.get(position);
        holder.textView.setText(data);

        //给按钮设置删除监听器
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteDetailFragment fragment = new NoteDetailFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }

    // 返回数据项数量
    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    // ViewHolder 类
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public Button deleteButton;
        public RecyclerView.Adapter adapter;

        public ViewHolder(View itemView, RecyclerView.Adapter adapter) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview);
            deleteButton = itemView.findViewById(R.id.deleteItem);
            this.adapter = adapter;
        }
        // item 点击事件


    }
}
