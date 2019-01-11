package com.example.user.outstagram.Fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.outstagram.MyPost.MyPostItem;
import com.example.user.outstagram.MyPost.MyPostItemAdapter;
import com.example.user.outstagram.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    ArrayList<HomeItem> arrayList;
    List<String> uidlist;
    Context context;

    HomeAdapter(ArrayList<HomeItem> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        HomeItem item = arrayList.get(position);
//        Glide.with(holder.itemView.getContext()).load(item.getPhoto()).into(holder.photo);
//        holder.nickname.setText(item.getNickname());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView nickname;

        public ViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
            nickname = itemView.findViewById(R.id.nickname);
        }
    }
}
