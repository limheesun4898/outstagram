package com.example.user.outstagram;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class login_sign_up extends AppCompatActivity {

    Button Signup_loginsignupbutton;
    EditText loginsignup_email;
    EditText loginsignup_password1;
    EditText loginsignup_password2;
    private FirebaseAuth mAuth;
    private String apassword, bpassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);


        Signup_loginsignupbutton = findViewById(R.id.Signup_loginsignupbutton);
        loginsignup_email = findViewById(R.id.loginsignup_email);
        loginsignup_password1 = findViewById(R.id.loginsignup_password1);
        loginsignup_password2 = findViewById(R.id.loginsignup_password2);


        mAuth = FirebaseAuth.getInstance();

        Signup_loginsignupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apassword = loginsignup_password1.getText().toString();
                bpassword = loginsignup_password2.getText().toString();

                creatAccount(loginsignup_email.getText().toString(), loginsignup_password1.getText().toString());

                if (apassword.equals(bpassword)) {
                    creatUser(loginsignup_email.getText().toString(), loginsignup_password1.getText().toString());
                    Toast.makeText(login_sign_up.this, "이메일회원가입 성공되었습니다.", Toast.LENGTH_SHORT).show();

                    Intent a = new Intent(login_sign_up.this, login.class);
                    startActivity(a);
                } else {
                    Toast.makeText(login_sign_up.this, "비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //이메일 유효성 체크
    private boolean isValidEmail(String target) {
        if (target == null || TextUtils.isEmpty(target)){
            Toast.makeText(this, "이미 사용중인 이메일 입니다 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;}
        else
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    //비밀번호 6자리 이상 한글미포함
    private boolean isValidPasswd(String target) {
        Pattern p = Pattern.compile("(^.*(?=.{6,100})(?=.*[0-9])(?=.*[a-zA-Z]).*$)");

        Matcher m = p.matcher(target);
        if (m.find() && !target.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
            Toast.makeText(this, "비밀번호 6자리 이상입니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }
    private void creatAccount(String email, String password){
        if(isValidEmail(loginsignup_email.getText().toString())){
            Intent intent = new Intent(login_sign_up.this, login_sign_up.class);
            startActivity(intent);
            return;
        }
        if (isValidPasswd(loginsignup_password1.getText().toString())){
            Intent intent = new Intent(login_sign_up.this, login_sign_up.class);
            startActivity(intent);
            return;
        }
    }
    private void creatUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            // 로그인 실패 부분
                        } else {
                        }
                    }
                });
    }
}