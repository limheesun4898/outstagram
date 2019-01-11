package com.example.user.outstagram.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.outstagram.Login.Login;
import com.example.user.outstagram.Login.UserItem;
import com.example.user.outstagram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Fragment_Home extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    RecyclerView recyclerView;
    List<Set> uidList = new ArrayList<>();
    ArrayList<HomeItem> itemList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        recyclerView = view.findViewById(R.id.following_item_recyclerview);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 내용이 갱신 되면 true, 갱싱되지 않고 고정이면 false
        final HomeAdapter homeAdapter = new HomeAdapter(itemList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(homeAdapter);

//        try {
//            final FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference myRef = database.getReference();
//            myRef.child("uers").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    uidList.clear();
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        UserItem homeItem = snapshot.getValue(UserItem.class);
//                        Set key = homeItem.followering.keySet();
//
////                        HomeItem homeItem = snapshot.getValue(HomeItem.class);
//                        System.out.println("Key set values are" + key);
//                    }
//                    homeAdapter.notifyDataSetChanged();
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        } catch (NullPointerException e) {
//
//        }


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (user == null) {
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        } else {

        }
    }


}
