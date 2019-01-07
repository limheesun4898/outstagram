package com.example.user.outstagram.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.INotificationSideChannel;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.user.outstagram.Login.UserItem;
import com.example.user.outstagram.MyPost.MyPost;
import com.example.user.outstagram.MyPost.MyPostItem;
import com.example.user.outstagram.MyPost.MyPostItemAdapter;
import com.example.user.outstagram.MyPost.WritePost;
import com.example.user.outstagram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_Account extends Fragment {
    TextView Unickname, Uname, Uemail, follower, posts, following;
    CircleImageView Uphoto;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedcount;
    String stUid;
    int count;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private List<MyPostItem> itemList = new ArrayList<>();
    RecyclerView recyclerView;
    String stNickname, stPhoto;
    Button write;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        Unickname = view.findViewById(R.id.Unickname);
        Uname = view.findViewById(R.id.Uname);
        Uemail = view.findViewById(R.id.Uemail);
        Uphoto = view.findViewById(R.id.Uphoto);
        posts = view.findViewById(R.id.posts);
        follower = view.findViewById(R.id.follower);
        following = view.findViewById(R.id.following);
        write = view.findViewById(R.id.write);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WritePost.class);
                startActivity(intent);
            }
        });

        try {
            sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
            stUid = sharedPreferences.getString("Uid", "");
            System.out.println("userUid : " + stUid);
        } catch (NullPointerException e) {

        }
        try {
            sharedcount = getActivity().getSharedPreferences("count", Context.MODE_PRIVATE);
            count = sharedcount.getInt("Count", 0);
            System.out.println("Count : " + count);

            posts.setText(String.valueOf(count));

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            myRef.child("users").child(stUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserItem userItem = dataSnapshot.getValue(UserItem.class);

                    Glide.with(getActivity()).load(userItem.getPhoto()).into(Uphoto);
                    Uname.setText(userItem.getName());
                    Unickname.setText(userItem.getNickname());
                    Uemail.setText(userItem.getEmail());
                    follower.setText(String.valueOf(userItem.follower_count));
                    following.setText(String.valueOf(userItem.following_count));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        } catch (NullPointerException e) {

        }

        recyclerView = view.findViewById(R.id.mypost_item_recyclerview);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 내용이 갱신 되면 true, 갱싱되지 않고 고정이면 false
        final MyPostItemAdapter myPostItemAdapter = new MyPostItemAdapter(itemList, getActivity(), stNickname, stPhoto);
        System.out.println("stNickname : " + stNickname + " stPhoto : " + stPhoto);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myPostItemAdapter);

        try {
            database.getReference().child("post").child(stUid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MyPostItem myPostItem = snapshot.getValue(MyPostItem.class);
                        itemList.add(myPostItem);
                        posts.setText(String.valueOf(itemList.size()));
                    }
                    myPostItemAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (NullPointerException e) {

        }

        return view;
    }

}