package com.aniketjain.chatappjava.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aniketjain.chatappjava.Adapter.UserAdapter;
import com.aniketjain.chatappjava.Model.Users;
import com.aniketjain.chatappjava.R;
import com.aniketjain.chatappjava.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding homeBinding;
    private UserAdapter userAdapter;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ArrayList<Users> usersArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding SetUp
        homeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(homeBinding.getRoot());

        // Firebase Current User Instance
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        setUpMainRecyclerView();

        checkUserInstance();

        onClickListeners();

    }


    private void checkUserInstance() {
        // when the new user start the app
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        } else {

            reference = database.getReference().child("user");
            reference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Users users = dataSnapshot.getValue(Users.class);
                        usersArrayList.add(users);
                    }
                    // when user do changes on to user array list
                    userAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


        }
    }

    private void setUpMainRecyclerView() {
        // main recyclerView SetUp
        userAdapter = new UserAdapter(this, usersArrayList);
        homeBinding.mainRv.setLayoutManager(new LinearLayoutManager(this));
        homeBinding.mainRv.setAdapter(userAdapter);
    }

    private void onClickListeners() {
        homeBinding.logoutIv.setOnClickListener(view -> {
            Dialog dialog = new Dialog(this, R.style.Dialoge);

            dialog.setContentView(R.layout.dialoge_layout);
            dialog.findViewById(R.id.logout_no_btn).setOnClickListener(view1 -> dialog.dismiss());
            dialog.findViewById(R.id.logout_yes_btn).setOnClickListener(view1 -> {
                auth.signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            });

            dialog.show();
        });
    }
}