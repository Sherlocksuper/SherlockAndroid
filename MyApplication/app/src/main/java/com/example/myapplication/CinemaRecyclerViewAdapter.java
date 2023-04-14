package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.internal.ScrimInsetsFrameLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CinemaRecyclerViewAdapter extends RecyclerView.Adapter<CinemaRecyclerViewAdapter.ViewHolder> {


    private Context mcontect;
    private ArrayList<MainpageCinemaItem> itemList1 = new ArrayList<>();
    private ArrayList<MainpageCinemaItem> itemList2 = new ArrayList<>();
    private final int TYPE_HEADER = 1001;
    private final int TYPE_MIDDLE = 2002;

    @NonNull
    @Override
    public CinemaRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mcontect == null) {
            mcontect = parent.getContext();
        }
        //设置header
        if (viewType == TYPE_HEADER) {
            View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_cinema_logged, parent, false);
            return new HeaderViewHolder(headerView);
        }
        //设置中部图片
        if (viewType == TYPE_MIDDLE) {
            View middleView = LayoutInflater.from(parent.getContext()).inflate(R.layout.middle_cinema_logged, parent, false);
            return new MiddleViewHolder(middleView);
        }
        //设置普通item
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_mainpage_cinema_item, parent, false);
        return new ViewHolder(inflate);
    }

    public CinemaRecyclerViewAdapter(Context context, ArrayList<MainpageCinemaItem> itemList1, ArrayList<MainpageCinemaItem> itemList2) {

        this.mcontect = context;
        this.itemList1 = itemList1;
        this.itemList2 = itemList2;

    }

    @Override
    public void onBindViewHolder(@NonNull CinemaRecyclerViewAdapter.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case 1001:
                StaggeredGridLayoutManager.LayoutParams clp = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                clp.setFullSpan(true);
            case 2002:
                StaggeredGridLayoutManager.LayoutParams clp1 = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                clp1.setFullSpan(true);
        }

        //第一组电影
        if (position != 0 && position < (itemList1.size() + 1)) {

            MainpageCinemaItem cinema = itemList1.get(position - 1);
            holder.tv_getName.setText(cinema.getName());
            holder.tv_getGrade.setText(cinema.getGrade() + "");
            Glide.with(mcontect).load(cinema.getPicUrl()).into(holder.im_getImage);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer id = cinema.getId();
                    BigDecimal grade = cinema.getGrade();
                    toDetailedCinemaPage(id,grade);
                }
            });

        }
        //第二组电影
        if (position > (itemList1.size() + 1) && position < getItemCount()) {

            MainpageCinemaItem cinema = itemList2.get(position - 1 - itemList1.size() - 1);
            holder.tv_getName.setText(cinema.getName());
            holder.tv_getGrade.setText(cinema.getGrade() + "");
            Glide.with(mcontect).load(cinema.getPicUrl()).into(holder.im_getImage);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer id = cinema.getId();
                    BigDecimal grade = cinema.getGrade();
                    toDetailedCinemaPage(id,grade);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return itemList1.size() + itemList2.size() + 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_getName;
        TextView tv_getGrade;
        ImageView im_getImage;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardView = itemView.findViewById(R.id.cardView_item);
            this.tv_getGrade = itemView.findViewById(R.id.mainpage_itemGrade);
            this.tv_getName = itemView.findViewById(R.id.mainpage_itemName);
            this.im_getImage = itemView.findViewById(R.id.mainpage_itemImage);
        }
    }

    //header 图标
    private class HeaderViewHolder extends ViewHolder {
        public HeaderViewHolder(View headerView) {
            super(headerView);
        }
    }

    //middle 图表holder
    private class MiddleViewHolder extends ViewHolder {
        public MiddleViewHolder(View middleView) {
            super(middleView);
        }
    }

    //获取type
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        if (position == itemList1.size() + 1) {
            return TYPE_MIDDLE;
        }
        return super.getItemViewType(position);
    }


    public void toDetailedCinemaPage(Integer id,BigDecimal grade) {
        Intent intent = new Intent(mcontect, DetailedCinemaThings.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        String gradeS = grade+"";
        bundle.putString("grade",gradeS);
        intent.putExtras(bundle);

        mcontect.startActivity(intent);
    }
}
