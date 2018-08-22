package com.example.user.outstagram;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

interface onAuthStateChanged {
    void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth);
}
