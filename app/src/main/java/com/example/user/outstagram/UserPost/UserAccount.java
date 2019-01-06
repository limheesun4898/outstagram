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
import com.example.user.outstagram.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAccount extends AppCompatActivity {
    TextView Unickname, Uname;
    CircleImageView Uphoto;
    Button follow;
    String stUid;
    Context context = this;
    ImageButton back;
    RecyclerView recyclerView;
    String stNickname,stPhoto;
    private List<UserPostItem> itemArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        Uname = findViewById(R.id.Uname);
        Unickname = findViewById(R.id.Unickname);
        Uphoto = findViewById(R.id.Uphoto);
        follow = findViewById(R.id.follow);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        stUid = getIntent().getStringExtra("key");

        try {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            myRef.child("users").child(stUid).addListenerForSingleValueEvent(new ValueEventListener() {
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

        } catch (NullPointerException e) { }

        recyclerView = findViewById(R.id.user_item_recyclerview);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 내용이 갱신 되면 true, 갱싱되지 않고 고정이면 false
        final UserAccountAdapter userAccountAdapter = new UserAccountAdapter(itemArrayList, getApplicationContext(),stNickname,stPhoto, stUid);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(userAccountAdapter);

        try {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            myRef.child("post").child(stUid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    itemArrayList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserPostItem userPostItem = snapshot.getValue(UserPostItem.class);
                        itemArrayList.add(userPostItem);
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

}
