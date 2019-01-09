package com.example.user.outstagram.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.outstagram.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Hashtable;

public class Signup extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mfirebaseAuth;
    String Uname, Uemail, Unickname;
    DatabaseReference myRef;
    private EditText loginsingup_name, loginsignup_email, loginsignup_password, loginsignup_nickname;

    String key = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        loginsingup_name = findViewById(R.id.loginsignup_name);
        loginsignup_email = findViewById(R.id.loginsignup_email);
        loginsignup_password = findViewById(R.id.loginsignup_password);
        loginsignup_nickname = findViewById(R.id.loginsignup_nickname);

        findViewById(R.id.Signup_loginsignupbutton).setOnClickListener(this);

        mfirebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

    }

    public void Singup(final String name, final String email, final String password, final String nickname) {
        mfirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                Toast.makeText(Signup.this, "비밀번호가 간단해요..", Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(Signup.this, "email 형식에 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(Signup.this, "이미존재하는 email 입니다.", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(Signup.this, "다시 확인해주세요..", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            FirebaseUser user = mfirebaseAuth.getCurrentUser();
                            if (user != null) {
                                Uname = name;
                                Uemail = email;
                                Unickname = nickname;

                                UserItem userItem = new UserItem();
                                userItem.setName(Uname);
                                userItem.setEmail(Uemail);
                                userItem.setNickname(Unickname);
                                userItem.setPhoto("https://firebasestorage.googleapis.com/v0/b/how-about-a-cafe.appspot.com/o/users%2Faccount.png?alt=media&token=187d46ea-019f-487f-97b6-3a2305272630");
                                myRef.child("users").child(user.getUid()).setValue(userItem);

                                myRef.child("NicknameCheck").child(user.getUid()).child("nickname").setValue(Unickname);
                            }

                            Toast.makeText(Signup.this, "이메일 회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Signup.this, Login.class));
                            finish();
                        }

                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Signup_loginsignupbutton:
                if (loginsingup_name.getText().toString().isEmpty() || loginsignup_email.getText().toString().isEmpty() ||
                        loginsignup_password.getText().toString().isEmpty() || loginsignup_nickname.getText().toString().isEmpty()) {
                    Toast.makeText(this, "빈칸을 채워주세요 :)", Toast.LENGTH_SHORT).show();
                } else
                    NicknameCheck(loginsignup_nickname.getText().toString());
                break;
        }
    }

    public void NicknameCheck(String nickname) {
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
                    Singup(loginsingup_name.getText().toString(), loginsignup_email.getText().toString(),
                            loginsignup_password.getText().toString(), loginsignup_nickname.getText().toString());
                } else {
                    Toast.makeText(Signup.this, "이미 있는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}