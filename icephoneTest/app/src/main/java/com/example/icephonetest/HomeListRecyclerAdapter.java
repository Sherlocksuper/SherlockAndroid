package com.example.icephonetest;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeListRecyclerAdapter extends RecyclerView.Adapter<HomeListRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<PublicResult> mTotalDataList;// 声明数据列表
    private FragmentManager fragmentManager;

    public SharedPreferences preferences;

    // 构造函数，传入数据列表
    public HomeListRecyclerAdapter(List<PublicResult> dataList, Context context, FragmentManager manager) {
        mTotalDataList = dataList;
        fragmentManager = manager;
        this.context = context;

        preferences = context.getSharedPreferences(UsersCounts.usersCount, MODE_PRIVATE);
    }

    // 创建 ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view, this);
    }

    // 绑定 ViewHolder
    //dosometest
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemDate.setText(mTotalDataList.get(position).date);
        holder.itemTitle.setText(mTotalDataList.get(position).title);
        holder.itemContent.setText(mTotalDataList.get(position).content);
        holder.itemKind.setText(mTotalDataList.get(position).kind);
        //给按钮设置删除监听器
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = preferences.getString("publicResultList", "");
                // 将JSON字符串转换回List<PublicResult>
                Gson gson = new Gson();
                Type type = new TypeToken<List<PublicResult>>() {
                }.getType();
                String certainKind = holder.itemKind.getText().toString(); // 将此变量替换为您要删除的kind的值
                int adapterPosition = holder.getAdapterPosition(); // 获取ViewHol
                mTotalDataList = gson.fromJson(json, type);

                List<PublicResult> processDataList = new ArrayList<>(mTotalDataList);

                mTotalDataList.clear();

                //获得所有类名相同的
                for (PublicResult publicResult : processDataList) {
                    if (publicResult.kind.equals(certainKind)) {
                        mTotalDataList.add(publicResult);
                    }
                }

                //删除所有类名相同的
                for (int i = 0; i < processDataList.size(); i++) {
                    if (processDataList.get(i).kind.equals(certainKind)) {
                        processDataList.remove(i);
                        i--;
                    }
                }
                //在所有kind相同的列表中删除选中的adapter
                mTotalDataList.remove(adapterPosition);
                notifyDataSetChanged();
                processDataList.addAll(mTotalDataList);
                String jsonPut = gson.toJson(processDataList);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("publicResultList", jsonPut);
                editor.apply();

                Toast.makeText(v.getContext(), "成功删除", Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NoteDetailFragment fragment = new NoteDetailFragment();
                Bundle args = new Bundle();
                args.putSerializable("public_result", mTotalDataList.get(holder.getAdapterPosition()));
                args.putInt("item_position", holder.getAdapterPosition());

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
        if (!mTotalDataList.isEmpty())
            return mTotalDataList.size();
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
        }// item 点击事
    }
}
