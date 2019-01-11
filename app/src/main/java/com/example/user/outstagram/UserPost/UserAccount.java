package com.example.user.outstagram.UserPost;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.outstagram.Login.UserItem;
import com.example.user.outstagram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAccount extends AppCompatActivity {
    TextView Unickname, Uname,follower, following;
    CircleImageView Uphoto;
    Button follow;
    String userUid;
    Context context = this;
    ImageButton back;
    RecyclerView recyclerView;
    String stNickname, stPhoto;
    private List<UserPostItem> itemArrayList = new ArrayList<>();
    private List<String> uidList = new ArrayList<>();

    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        Uname = findViewById(R.id.Uname);
        Unickname = findViewById(R.id.Unickname);
        Uphoto = findViewById(R.id.Uphoto);
        follow = findViewById(R.id.follow);
        follower = findViewById(R.id.follower);
        following = findViewById(R.id.following);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        userUid = getIntent().getStringExtra("key");

        ImageStarFollower(database.getReference().child("users").child(userUid));
        ImageStarFollowing(database.getReference().child("users").child(auth.getCurrentUser().getUid()));
        try {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            myRef.child("users").child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String stName = dataSnapshot.child("name").getValue().toString();
                    stNickname = dataSnapshot.child("nickname").getValue().toString();
                    stPhoto = dataSnapshot.child("photo").getValue().toString();

                    Glide.with(context).load(stPhoto).into(Uphoto);
                    Uname.setText(stName);
                    Unickname.setText(stNickname);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (NullPointerException e) {
        }

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStarClickedFollower(database.getReference().child("users").child(userUid));
                ImageStarFollower(database.getReference().child("users").child(userUid));

                onStarClickedFollowing(database.getReference().child("users").child(auth.getCurrentUser().getUid()));
                ImageStarFollowing(database.getReference().child("users").child(auth.getCurrentUser().getUid()));
            }
        });

        recyclerView = findViewById(R.id.user_item_recyclerview);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 내용이 갱신 되면 true, 갱싱되지 않고 고정이면 false
        final UserAccountAdapter userAccountAdapter = new UserAccountAdapter(itemArrayList, getApplicationContext(), stNickname, stPhoto, userUid, uidList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(userAccountAdapter);

        try {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            myRef.child("post").child(userUid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    itemArrayList.clear();
                    uidList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        UserPostItem userPostItem = snapshot.getValue(UserPostItem.class);
                        itemArrayList.add(userPostItem);
                        Collections.reverse(itemArrayList);
                        uidList.add(key);
                    }
                    userAccountAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (NullPointerException e) {

        }


    }

    //follower 기능 트랜잭션 이용
    private void onStarClickedFollower(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                UserItem p = mutableData.getValue(UserItem.class);
                if (p == null) {

                    return Transaction.success(mutableData);
                }
                //팔로우 취소
                if (p.follower.containsKey(auth.getCurrentUser().getUid())) {
                    p.follower_count = p.follower_count - 1;
                    p.follower.remove(auth.getCurrentUser().getUid());

                } else {
                    //팔로우 누름~~
                    p.follower_count = p.follower_count + 1;
                    p.follower.put(auth.getCurrentUser().getUid(),true);

                }
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
            }
        });
    }
    //follower 기능 트랜잭션 이용
    private void ImageStarFollower(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                UserItem p = mutableData.getValue(UserItem.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }
                //팔로우 취소
                if (p.follower.containsKey(auth.getCurrentUser().getUid())) {
                    follow.setText("팔로우 중");
                    follow.setBackgroundColor(getApplicationContext().getColor(R.color.GRAY));
                    follower.setText(String.valueOf(p.follower_count));
                    following.setText(String.valueOf(p.following_count));
                } else {
                    //팔로우 누름~~
                    follow.setText("팔로우");
                    follow.setBackgroundColor(getApplicationContext().getColor(R.color.BULE));
                    follower.setText(String.valueOf(p.follower_count));
                    following.setText(String.valueOf(p.following_count));
                }
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
            }
        });
    }
    //following 기능 트랜잭션 이용
    private void onStarClickedFollowing(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                UserItem p = mutableData.getValue(UserItem.class);
                if (p == null) {

                    return Transaction.success(mutableData);
                }
                //팔로우 취소
                if (p.followering.containsKey(auth.getCurrentUser().getUid())) {
                    p.following_count = p.following_count - 1;
                    p.followering.remove(auth.getCurrentUser().getUid());

                } else {
                    //팔로우 누름~~
                    p.following_count = p.following_count + 1;
                    p.followering.put(auth.getCurrentUser().getUid(), true);

                }
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
            }
        });
    }
    //folloing 기능 트랜잭션 이용
    private void ImageStarFollowing(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                UserItem p = mutableData.getValue(UserItem.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }
                //팔로우 취소
                if (p.followering.containsKey(auth.getCurrentUser().getUid())) {
                    follow.setText("팔로우 중");
                    follow.setBackgroundColor(getApplicationContext().getColor(R.color.GRAY));
                } else {
                    //팔로우 누름~~
                    follow.setText("팔로우");
                    follow.setBackgroundColor(getApplicationContext().getColor(R.color.BULE));

                }
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
            }
        });
    }
}
