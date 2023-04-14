package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CinemaCommentRecyclerViewAdapter extends RecyclerView.Adapter<CinemaCommentRecyclerViewAdapter.ViewHolder> {
    ArrayList<DetailedCinemaThings.commentThings.RemarkVO> itemList;
    private View view;
    private Context context;

    @NonNull
    @Override
    public CinemaCommentRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.comment_items_list, parent, false);
        CinemaCommentRecyclerViewAdapter.ViewHolder myHolder = new CinemaCommentRecyclerViewAdapter.ViewHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CinemaCommentRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.commentUserName.setText(itemList.get(position).userName);
        holder.commentUserContent.setText("     "+itemList.get(position).content);
        holder.commentUserGrade.setText(itemList.get(position).grade+"");
        holder.commentUserFavorNum.setText(itemList.get(position).favorPeople+"");


        Glide.with(holder.itemView).load((itemList.get(position).userPicUrl)).into(holder.commentUserPic);
        Log.d("TAG", "onBindViewHolder: "+itemList.get(position).userPicUrl);
        //holder.commentUserPic.setImageURI(Uri.parse(itemList.get(position).userPicUrl));

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView commentUserName, commentUserGrade, commentUserContent, commentUserFavorNum;
        ImageView commentUserPic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentUserName = itemView.findViewById(R.id.commentUserName);
            commentUserContent = itemView.findViewById(R.id.commentUserContent);
            commentUserGrade = itemView.findViewById(R.id.commentUserGrade);
            commentUserFavorNum = itemView.findViewById(R.id.commentFavorPeople);

            commentUserPic = itemView.findViewById(R.id.commentUserPic);
        }
    }

    public CinemaCommentRecyclerViewAdapter(Context context, DetailedCinemaThings.commentThings itemList) {
        this.itemList = itemList.getData();
        this.context = context;
    }
}
