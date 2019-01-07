package com.example.user.outstagram.MyPost;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.outstagram.EditActivity;
import com.example.user.outstagram.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPost extends AppCompatActivity {
    TextView nickname, favorite_count, title;
    CircleImageView photo;
    ImageView image, star, mypost_menubtn;
    ImageButton chat, back;
    String Uimage, Utitle, stUid, stformatDate;
    SharedPreferences sharedPreferences;
    Context context = this;
    FirebaseDatabase database;
    DatabaseReference myRef;
    SharedPreferences sharedcount;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        nickname = findViewById(R.id.nickname);
        favorite_count = findViewById(R.id.favorite_count);
        title = findViewById(R.id.title);
        photo = findViewById(R.id.photo);
        image = findViewById(R.id.image);
        chat = findViewById(R.id.chat);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        sharedcount = getApplicationContext().getSharedPreferences("count", Context.MODE_PRIVATE);
        count = sharedcount.getInt("Count", 0);

        star = findViewById(R.id.star);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mypost_menubtn = findViewById(R.id.mypost_menubtn);
        mypost_menubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, mypost_menubtn);
                popupMenu.inflate(R.menu.edit_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_del:
                                deleteAlert();
                                break;
                            case R.id.menu_edit:
                                Intent intent = new Intent(context, EditActivity.class);
                                intent.putExtra("formatDate", stformatDate);
                                context.startActivity(intent);
                                ((Activity) context).finish();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        Utitle = getIntent().getStringExtra("title");
        Uimage = getIntent().getStringExtra("image");
        stformatDate = getIntent().getStringExtra("formatDate");

        title.setText(Utitle);
        Glide.with(this).load(Uimage).into(image);

        try {
            sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
            stUid = sharedPreferences.getString("Uid", "");
        } catch (NullPointerException e) {

        }

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

    }

    public void deleteAlert() {
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(context);
        alert_confirm.setMessage("삭제하시겠습니까?").setCancelable(false).setPositiveButton("삭제",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        database.getReference().child("post").child(stUid).child(stformatDate).removeValue();

                        StorageReference mStorage = FirebaseStorage.getInstance().getReference();
                        mStorage.child("post").child(stUid).child(stformatDate).delete();
                        count = count-1;
                        onBackPressed();
                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alert_confirm.create();
        alert.show();
    }

}
