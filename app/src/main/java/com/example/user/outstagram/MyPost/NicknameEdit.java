package com.example.user.outstagram.MyPost;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.outstagram.Login.UserItem;
import com.example.user.outstagram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Hashtable;

public class NicknameEdit extends AppCompatActivity {
    EditText nickname;
    TextView check;
    Button findBtn;
    String stnickname;
    String key;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname_edit);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        nickname = findViewById(R.id.nickname);
        check = findViewById(R.id.check);
        findBtn = findViewById(R.id.findbtn);

        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stnickname = nickname.getText().toString();
                if (stnickname.isEmpty()) {
                    check.setVisibility(View.VISIBLE);
                    check.setText("닉네임 작성을 해주세요.");
                } else {
                    check.setVisibility(View.GONE);
                    NicknameCheck(nickname.getText().toString());

                }
            }
        });

    }

    public void NicknameCheck(final String nickname) {
        key = null;
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("NicknameCheck");
        Query query = databaseReference.orderByChild("nickname").equalTo(nickname);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    key = childSnapshot.getKey();
                }
                if (key == null) {
                    check.setVisibility(View.GONE);

                    database.getReference().child("users").child(user.getUid()).child("nickname").setValue(nickname);
                    database.getReference().child("NicknameCheck").child(user.getUid()).child("nickname").setValue(nickname);
                    Toast.makeText(NicknameEdit.this, "닉네임 입력이 되었습니다.", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    check.setText("이미 있는 닉네임입니다.");
                    check.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
