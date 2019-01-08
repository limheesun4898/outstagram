package com.example.user.outstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.user.outstagram.Fragment.Fragment_Account;
import com.example.user.outstagram.Fragment.Fragment_Camera;
import com.example.user.outstagram.Fragment.Fragment_Home;
import com.example.user.outstagram.Fragment.Fragment_Search;
import com.example.user.outstagram.Login.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);

        //처음 실행 했을 대 home 프래그먼트를 띄워줌
        getFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment_Home()).commit();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user != null) {

        } else {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    getFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment_Home()).commit();
                    return true;
                case R.id.nav_search:
                    getFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment_Search()).commit();
                    return true;
                case R.id.nav_camera:
                    getFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment_Camera()).commit();
                    return true;
                case R.id.nav_account:
                    getFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment_Account()).commit();
                    return true;
            }
            return false;
        }
    };

}
