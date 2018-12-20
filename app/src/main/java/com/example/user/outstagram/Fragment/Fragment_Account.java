package com.example.user.outstagram.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    TextView Unickname, Uname, Uemail;
    CircleImageView Uphoto;
    SharedPreferences sharedPreferences;
    String stUid;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private MyPostItemAdapter myPostItemAdapter;
    private List<MyPostItem> itemList = new ArrayList<>();
    Fragment_Account context = this;
    RecyclerView recyclerView;
    Button post;

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

        recyclerView = view.findViewById(R.id.mypost_item_recyclerview);
        recyclerView.setHasFixedSize(true);
        myPostItemAdapter = new MyPostItemAdapter(itemList, context);
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
                    }
                    myPostItemAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (NullPointerException e) {

        }

        post = view.findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    //현재 사용자 확인
    @Override
    public void onStart() {
        super.onStart();
        if (user == null) {

        } else {
            try {
                sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                stUid = sharedPreferences.getString("Uid", "");
                System.out.println("userUid : " + stUid);

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                myRef.child("users").child(stUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String stName = dataSnapshot.child("name").getValue().toString();
                        String stNickname = dataSnapshot.child("nickname").getValue().toString();
                        String stEmail = dataSnapshot.child("email").getValue().toString();
                        String stPhoto = dataSnapshot.child("photo").getValue().toString();

                        Glide.with(getActivity()).load(stPhoto).into(Uphoto);
                        Uname.setText(stName);
                        Unickname.setText(stNickname);
                        Uemail.setText(stEmail);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } catch (NullPointerException e) {

            }
        }
    }

}