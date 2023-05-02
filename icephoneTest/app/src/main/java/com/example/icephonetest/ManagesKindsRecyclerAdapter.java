package com.example.icephonetest;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManagesKindsRecyclerAdapter extends RecyclerView.Adapter<ManagesKindsRecyclerAdapter.ViewHolder> {

    private List<String> mData;

    public ManagesKindsRecyclerAdapter(List<String> data) {
        mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kindlayout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTextView.setText(mData.get(position));

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = v.getContext().getSharedPreferences(UsersCounts.usersCount, MODE_PRIVATE);
                Set<String> stringSet = preferences.getStringSet("spinnerList", new HashSet<String>());
                // 将Set转换为List
                List<String> kindsDatas = new ArrayList<String>(stringSet);
                kindsDatas.remove(holder.getAdapterPosition());

                mData.remove(holder.getAdapterPosition());
                notifyDataSetChanged();

                stringSet = new HashSet<String>(mData);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putStringSet("spinnerList", stringSet); // 存储Set数据
                editor.apply(); // 提交修改
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageButton deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.item_kind_text);
            deleteButton = itemView.findViewById(R.id.kind_deleteItem);
        }
    }

}

