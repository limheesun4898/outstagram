package com.example.user.outstagram.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.outstagram.MainActivity;
import com.example.user.outstagram.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Hashtable;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, onAuthStateChanged, View.OnClickListener {
    EditText login_email;
    EditText login_password;
    SignInButton Google_Signin_button;

    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 10;
    private FirebaseAuth.AuthStateListener mAuthLiestener;

    DatabaseReference myRef;
    String name, email, photoUrl;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        findViewById(R.id.loginbutton).setOnClickListener(this);
        findViewById(R.id.loginsignup_button).setOnClickListener(this);
        Google_Signin_button = findViewById(R.id.Google_Singin_button);

        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        mAuthLiestener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Uid", user.getUid());
                    editor.apply();
                } else {

                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Google_Signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

    }

    //이메일 로그인
    @Override
    public void clickSignIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            //로그인 실패
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                Toast.makeText(Login.this, "존재하지 않는 email 입니다.", Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(Login.this, "이메일 형식이 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                            } catch (FirebaseNetworkException e) {
                                Toast.makeText(Login.this, "Firebase NetworkException", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            //로그인 성공
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    //계정 데이터 가져옴
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        user.getEmail();
        user.getUid();
    }

    //구글 로그인
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthLiestener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthLiestener != null)
            mAuth.removeAuthStateListener(mAuthLiestener);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                        } else {
                            //구글 로그인 프로필 DB 저장
                            FirebaseUser user = mAuth.getCurrentUser();
                            name = user.getDisplayName();
                            email = user.getEmail();
                            photoUrl = "https://firebasestorage.googleapis.com/v0/b/how-about-a-cafe.appspot.com/o/users%2Faccount.png?alt=media&token=187d46ea-019f-487f-97b6-3a2305272630";
                            String following = "0";
                            String followers = "0";
                            String posts = "0";
                            //DB에 데이터 저장
                            Hashtable<String, String> profile = new Hashtable<String, String>();
                            profile.put("name", name);
                            profile.put("email", email);
                            profile.put("photo", photoUrl);
                            profile.put("nickname", "");
                            profile.put("following", following);
                            profile.put("followers", followers);
                            profile.put("posts", posts);
                            myRef.child(user.getUid()).setValue(profile);

                            startActivity(new Intent(Login.this, MainActivity.class));
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginbutton:
                String email = login_email.getText().toString();
                String password = login_password.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "빈칸을 채워주세요 :)", Toast.LENGTH_SHORT).show();
                } else {
                    clickSignIn(email, password);
                }
                break;
            case R.id.loginsignup_button:
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
                break;


        }
    }
    //https://isjang98.github.io/blog/Firebase-Authentication 참고 사이트
}
