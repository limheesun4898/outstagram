package com.example.user.outstagram.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.outstagram.R;
import com.example.user.outstagram.UserPost.UserAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_Search extends Fragment {
    EditText findnickname;
    Button btn;
    CircleImageView userphoto;
    TextView usernickname;
    RelativeLayout findUser;
    String key = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        findnickname = view.findViewById(R.id.findnickname);
        usernickname = view.findViewById(R.id.usernickname);
        userphoto = view.findViewById(R.id.userphoto);
        findUser = view.findViewById(R.id.findUser);
        btn = view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //입력이 끝났을 때 호출
                String find = null;
                find = findnickname.getText().toString();
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
                Query query = databaseReference.orderByChild("nickname").equalTo(find);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            key = childSnapshot.getKey();
                            System.out.println("useruid : " + key);
                            if (key == null) {
                                System.out.println("useruid : 검색결과가 없습니다.");
                            } else {
                                findUser.setVisibility(View.VISIBLE);
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference();
                                myRef.child("users").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String stNickname = dataSnapshot.child("nickname").getValue().toString();
                                        String stPhoto = dataSnapshot.child("photo").getValue().toString();

                                        Glide.with(getActivity()).load(stPhoto).into(userphoto);
                                        usernickname.setText(stNickname);
                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

        findUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UserAccount.class);
                intent.putExtra("key", key);
                startActivity(intent);
                System.out.println("key : " + key);
            }
        });


        return view;
    }


}
