package com.aniketjain.chatappjava.Activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aniketjain.chatappjava.Adapter.ChatAdapter;
import com.aniketjain.chatappjava.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding chatBinding;

    private String receiverImage, receiverUID, receiverName;
    private String senderImage;

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding SetUp
        chatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(chatBinding.getRoot());

        getIntentData();

        setUpReceiverProfile();

        // Firebase Current User Instance
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        getCurrentUserData();

        setUpRecyclerView();

        onClickListeners();


    }


    // get all the receive data from the User Adapter
    private void getIntentData() {
        receiverUID = getIntent().getStringExtra("receiver_uid");
        receiverName = getIntent().getStringExtra("receiver_name");
        receiverImage = getIntent().getStringExtra("receiver_image");
    }

    // setUp the Receiver Profile
    private void setUpReceiverProfile() {
        Picasso.get().load(receiverImage).into(chatBinding.chatReceiverProfileIv);
        chatBinding.chatReceiverNameTv.setText(receiverName);
    }

    private void getCurrentUserData() {
        firebaseDatabase
                .getReference()
                .child("user")
                .child(Objects.requireNonNull(auth.getUid()))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        senderImage = snapshot.child("imageUrl").toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void setUpRecyclerView() {
        chatBinding.chatRv.setLayoutManager(new LinearLayoutManager(this));
        chatBinding.chatRv.setAdapter(new ChatAdapter(this));
    }

    private void onClickListeners() {

    }
}