package com.example.user.outstagram.MyPost;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.bumptech.glide.Glide;

import com.example.user.outstagram.Fragment.Fragment_Account;
import com.example.user.outstagram.R;

import java.util.List;


public class MyPostItemAdapter extends RecyclerView.Adapter<MyPostItemAdapter.ViewHolder> {
    List<MyPostItem> myPostItem;
    Context context;
    String stnickanme, stphoto;
    private AdapterView.OnItemClickListener onItemClickListener;

    public MyPostItemAdapter(List<MyPostItem> myPostItem, Context context,String stnickanme, String stphoto) {
        this.myPostItem = myPostItem;
        this.context = context;
        this.stnickanme = stnickanme;
        this.stphoto = stphoto;
    }

    public MyPostItemAdapter(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
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
        final MyPostItem item = myPostItem.get(position);
        MyPostItemAdapter.ViewHolder viewHolder = (MyPostItemAdapter.ViewHolder) holder;

        Glide.with(holder.itemView.getContext()).load(item.getImage()).into(holder.post_photo);
        holder.post_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyPost.class);
                intent.putExtra("image", item.getImage());
                intent.putExtra("title",item.getTitle());
                intent.putExtra("formatDate",item.getFormatDate());
                intent.putExtra("starCount",item.getStarCount());
                context.startActivity(intent);

            }
        });

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
