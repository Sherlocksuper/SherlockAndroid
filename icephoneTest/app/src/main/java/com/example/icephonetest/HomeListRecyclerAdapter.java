package com.example.icephonetest;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

public class HomeListRecyclerAdapter extends RecyclerView.Adapter<HomeListRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<PublicResult> mDataList;// 声明数据列表
    private FragmentManager fragmentManager;

    // 构造函数，传入数据列表
    public HomeListRecyclerAdapter(List<PublicResult> dataList, Context context, FragmentManager manager) {
        Log.d("TAG", "HomeListRecyclerAdapter: " + dataList.size());
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
        holder.itemDate.setText(mDataList.get(position).date);
        holder.itemTitle.setText(mDataList.get(position).title);
        holder.itemContent.setText(mDataList.get(position).content);
        holder.itemKind.setText(mDataList.get(position).kind);
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
                Bundle args = new Bundle();
                args.putSerializable("public_result", mDataList.get(holder.getAdapterPosition()));
                fragment.setArguments(args);

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
        if (!mDataList.isEmpty())
            return mDataList.size();
        return 0;
    }


    // ViewHolder 类
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemDate;
        public TextView itemTitle;
        public TextView itemContent;

        public TextView itemKind;
        public Button deleteButton;
        public RecyclerView.Adapter adapter;


        public ViewHolder(View itemView, RecyclerView.Adapter adapter) {

            super(itemView);
            itemDate = itemView.findViewById(R.id.item_data);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemContent = itemView.findViewById(R.id.item_content);
            itemKind = itemView.findViewById(R.id.item_kind);

            deleteButton = itemView.findViewById(R.id.deleteItem);
            this.adapter = adapter;
        }
        // item 点击事件


    }
}
