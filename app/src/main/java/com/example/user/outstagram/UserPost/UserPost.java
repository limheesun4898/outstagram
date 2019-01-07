package com.example.user.outstagram.UserPost;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.outstagram.MyPost.MyPostItem;
import com.example.user.outstagram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserPost extends AppCompatActivity {
    TextView nickname, favorite_count, title;
    CircleImageView photo;
    ImageView image, star;
    ImageButton chat, back;
    String Uimage, Utitle, Ukey, Uboolean;
    String Useruid;
    Context context = this;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        auth = FirebaseAuth.getInstance();

        Utitle = getIntent().getStringExtra("title");
        Uimage = getIntent().getStringExtra("image");
        Ukey = getIntent().getStringExtra("key");
        Useruid = getIntent().getStringExtra("uid");
        Uboolean = getIntent().getStringExtra("boolean");

        nickname = findViewById(R.id.nickname);
        favorite_count = findViewById(R.id.favorite_count);
        title = findViewById(R.id.title);
        photo = findViewById(R.id.photo);
        image = findViewById(R.id.image);
        star = findViewById(R.id.favorite);
        chat = findViewById(R.id.chat);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImageStar(database.getReference().child("post").child(Useruid).child(Ukey));

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStarClicked(database.getReference().child("post").child(Useruid).child(Ukey));
                ImageStar(database.getReference().child("post").child(Useruid).child(Ukey));
            }
        });

        myRef.child("users").child(Useruid).addListenerForSingleValueEvent(new ValueEventListener() {
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

        title.setText(Utitle);
        Glide.with(this).load(Uimage).into(image);
    }

    //좋아요 기능 트랜잭션 이용
    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                UserPostItem p = mutableData.getValue(UserPostItem.class);
                if (p == null) {

                    return Transaction.success(mutableData);
                }
                //좋아요 취소
                if (p.stars.containsKey(auth.getCurrentUser().getUid())) {
                    p.starCount = p.starCount - 1;
                    p.stars.remove(auth.getCurrentUser().getUid());

                } else {
                    //젛아요 누름~~
                    p.starCount = p.starCount + 1;
                    p.stars.put(auth.getCurrentUser().getUid(), true);

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

    //좋아요 기능 트랜잭션 이용
    private void ImageStar(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                UserPostItem p = mutableData.getValue(UserPostItem.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }
                if (p.stars.containsKey(auth.getCurrentUser().getUid())) {
                    //젛아요 눌려있음
                    star.setImageResource(R.drawable.like);
                    favorite_count.setText(String.valueOf(p.starCount));

                } else {
                    //젛아요 안눌려있음
                    star.setImageResource(R.drawable.favorite);
                    favorite_count.setText(String.valueOf(p.starCount));
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
