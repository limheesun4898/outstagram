package com.example.user.outstagram.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toolbar;

import com.example.user.outstagram.FollowingPost.FollowingPostAdapter;
import com.example.user.outstagram.Login.Login;

import com.example.user.outstagram.MyPost.WritePost;
import com.example.user.outstagram.R;
import com.example.user.outstagram.UserPost.UserAccountAdapter;
import com.example.user.outstagram.UserPost.UserPostItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fragment_Home extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    RecyclerView recyclerView;
    Context context;

    private List<String> uidList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        recyclerView = view.findViewById(R.id.following_item_recyclerview);

        recyclerView.setHasFixedSize(true); // 리사이클러뷰 내용이 갱신 되면 true, 갱싱되지 않고 고정이면 false
        FollowingPostAdapter followingPostAdapter = new FollowingPostAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(followingPostAdapter);

//        try {
//            final FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference myRef = database.getReference();
//            myRef.child("users").child(mAuth.getCurrentUser().getUid()).child("followering").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    uidList.clear();
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        UserPostItem userPostItem = snapshot.getValue(UserPostItem.class);
//                        uidList.add(userPostItem);
//                        Collections.reverse(itemArrayList);
//                        uidList.add(key);
//                    }
//                    userAccountAdapter.notifyDataSetChanged();
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        } catch (NullPointerException e) {
//
//        }


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (user == null) {
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        } else {

        }
    }


}
