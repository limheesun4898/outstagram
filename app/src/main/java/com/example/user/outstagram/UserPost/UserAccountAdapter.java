package com.example.user.outstagram.UserPost;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.user.outstagram.MyPost.MyPost;
import com.example.user.outstagram.MyPost.MyPostItem;
import com.example.user.outstagram.MyPost.MyPostItemAdapter;
import com.example.user.outstagram.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class UserAccountAdapter extends RecyclerView.Adapter<UserAccountAdapter.ViewHolder> {
    List<UserPostItem> userPostItemList;
    List<String> useruidList;
    Context context;
    String stnickanme, stphoto;
    private AdapterView.OnItemClickListener onItemClickListener;
    String UserUid;


    public UserAccountAdapter(List<UserPostItem> userPostItemList, Context context, String stnickanme, String stphoto, String useruid, List<String> useruidList) {
        this.userPostItemList = userPostItemList;
        this.context = context;
        this.stnickanme = stnickanme;
        this.stphoto = stphoto;
        this.UserUid = useruid;
        this.useruidList = useruidList;
    }

    public UserAccountAdapter(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    @NonNull
    @Override
    public UserAccountAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        int width = parent.getResources().getDisplayMetrics().widthPixels / 3;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userpost, parent, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(width, width));
        UserAccountAdapter.ViewHolder holder = new UserAccountAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final UserPostItem item = userPostItemList.get(position);
        Glide.with(holder.itemView.getContext()).load(item.getImage()).into(holder.post_photo);

        holder.post_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserPost.class);
                intent.putExtra("image", item.getImage());
                intent.putExtra("title", item.getTitle());
                intent.putExtra("key", useruidList.get(position));
                intent.putExtra("uid", UserUid);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return userPostItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView post_photo;

        public ViewHolder(View itemView) {
            super(itemView);
            post_photo = itemView.findViewById(R.id.userpost_item);
        }
    }

}
