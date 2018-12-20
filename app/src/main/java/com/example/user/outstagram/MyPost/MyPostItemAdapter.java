package com.example.user.outstagram.MyPost;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.Request;
import com.example.user.outstagram.Fragment.Fragment_Account;
import com.example.user.outstagram.R;

import java.util.List;

public class MyPostItemAdapter extends RecyclerView.Adapter<MyPostItemAdapter.ViewHolder> {
    List<MyPostItem> myPostItem;
    Fragment_Account context;
    View view;

    public MyPostItemAdapter(List<MyPostItem> myPostItem, Fragment_Account context) {
        this.myPostItem = myPostItem;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        int width = parent.getResources().getDisplayMetrics().widthPixels / 3;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypost, parent, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(width, width));
        MyPostItemAdapter.ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyPostItem item = myPostItem.get(position);
        MyPostItemAdapter.ViewHolder viewHolder = (MyPostItemAdapter.ViewHolder) holder;
//      Glide.with(holder.itemView.getContext()).load(myPostItem.get(position).getPost_photo()).into(((ViewHolder)holder).post_photo);
        Glide.with(holder.itemView.getContext()).load(item.getPost_photo()).into(holder.post_photo);
    }

    @Override
    public int getItemCount() {
        return myPostItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView post_photo;

        public ViewHolder(View itemView) {
            super(itemView);
            post_photo = itemView.findViewById(R.id.mypost_item);
        }
    }
}
