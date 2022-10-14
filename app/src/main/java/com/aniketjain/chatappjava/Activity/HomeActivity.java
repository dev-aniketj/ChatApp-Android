package com.aniketjain.chatappjava.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aniketjain.chatappjava.Adapter.UserAdapter;
import com.aniketjain.chatappjava.Model.Users;
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

        checkUserInstance();

    }


    private void checkUserInstance() {
        // when the new user start the app
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        } else {

            setUpMainRecyclerView();

            reference = database.getReference().child("user");
            reference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Users users = dataSnapshot.getValue(Users.class);
                        usersArrayList.add(users);
                    }
                    userAdapter.notifyDataSetChanged();
                    setUpMainRecyclerView();
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
}