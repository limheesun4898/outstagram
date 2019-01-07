package com.example.user.outstagram.UserPost;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.outstagram.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserPost extends AppCompatActivity {
    TextView nickname, favorite_count, title;
    CircleImageView photo;
    ImageView image,favorite;
    ImageButton chat,back;
    String Uimage, Utitle;
    String stUid;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post);

        nickname = findViewById(R.id.nickname);
        favorite_count = findViewById(R.id.favorite_count);
        title = findViewById(R.id.title);
        photo = findViewById(R.id.photo);
        image = findViewById(R.id.image);
        favorite = findViewById(R.id.favorite);
        chat = findViewById(R.id.chat);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        stUid = getIntent().getStringExtra("uid");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("users").child(stUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String stNickname = dataSnapshot.child("nickname").getValue().toString();
                String stPhoto = dataSnapshot.child("photo").getValue().toString();

                nickname.setText(stNickname);
                Glide.with(context).load(stPhoto).into(photo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Utitle = getIntent().getStringExtra("title");
        Uimage = getIntent().getStringExtra("image");

        title.setText(Utitle);
        Glide.with(this).load(Uimage).into(image);
    }
}
