package com.example.user.outstagram.Login;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

interface onAuthStateChanged {
    //이메일 로그인
    void clickSignIn(String email, String password);
    void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth);
}
