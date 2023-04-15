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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

public class HomeListRecyclerAdapter extends RecyclerView.Adapter<HomeListRecyclerAdapter.ViewHolder> {
    private  Context context;
    private List<String> mDataList;// 声明数据列表
    private recyclerItemTouchHelper listener;



    public interface recyclerItemTouchHelper {
        void onRecyclerItemClick(int position);
    }

    // 构造函数，传入数据列表
    public HomeListRecyclerAdapter(List<String> dataList, Context context, recyclerItemTouchHelper listener) {
        mDataList = dataList;
        this.listener =listener;
        this.context = context;
    }

    // 创建 ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    // 绑定 ViewHolder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String data = mDataList.get(position);
        holder.textView.setText(data);
    }

    // 返回数据项数量
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    // ViewHolder 类
    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        public Button deleteButton;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview);
            deleteButton = itemView.findViewById(R.id.deleteItem);
            itemView.setOnClickListener(this);
        }

        // item 点击事件
        @Override
        public void onClick(View v) {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onRecyclerItemClick(position);
                }
            }
        }
    }

}
