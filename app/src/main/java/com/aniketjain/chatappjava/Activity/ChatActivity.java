package com.aniketjain.chatappjava.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aniketjain.chatappjava.Adapter.MessagesAdapter;
import com.aniketjain.chatappjava.Model.Messages;
import com.aniketjain.chatappjava.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding chatBinding;

    private String receiverUID, receiverName;
    private String senderUID;

    public static String senderImage, receiverImage;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference, chatReference;

    private String senderRoom, receiverRoom;
    private ArrayList<Messages> messagesArrayList;

    private MessagesAdapter messagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding SetUp
        chatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(chatBinding.getRoot());

        getReceiverDataFromIntent();

        setUpReceiverProfile();

        // Firebase Current User Instance
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        messagesArrayList = new ArrayList<>();

        getCurrentUserData();

        // setUp the data in the senderRoom, receiverRoom
        setRoomStringData();

        onClickListeners();

        // Firebase Database Current User Reference
//        databaseReference = database.getReference().child("user").child(senderUID);
        chatReference = FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("messages");

        // get all the message and store it into the Message Array List
        getChatData();

        setUpRecyclerView();

    }


    // get all the receive data from the User Adapter
    private void getReceiverDataFromIntent() {
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
        // set the current user UID in senderUID
        senderUID = auth.getUid();

        // get the current user Profile Image from the database,
        // and set the profile url/uri in senderImage
        database
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

        //
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        //
        messagesAdapter = new MessagesAdapter(this, messagesArrayList);

        //
        chatBinding.chatRv.setLayoutManager(linearLayoutManager);
        chatBinding.chatRv.setAdapter(messagesAdapter);
    }

    private void setRoomStringData() {
        senderRoom = senderUID + receiverUID;
        receiverRoom = receiverUID + senderUID;
    }

    private void onClickListeners() {


        chatBinding.sendMessageCvBtn.setOnClickListener(view -> {
            String message = chatBinding.chatMessageEt.getText().toString();
            if (!(message.isEmpty())) {
                // set Null after get the message
                chatBinding.chatMessageEt.setText("");

                // get the current date and time
                Date date = new Date();

                // pass data in the chat model
                Messages messages = new Messages(message, senderUID, date.getTime());

                /*
                 * Set the message data into the two rooms - senderRoom, receiverRoom
                 * and both the rooms are having different address
                 * that why we are adding both senderUID, receiverUID in two ways and
                 * creating a unique UID (address) where we store the unique two users
                 * chatting (message) data.
                 */

                // for Sender Room
                database = FirebaseDatabase.getInstance();
                database
                        .getReference()
                        .child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(messages)
                        .addOnCompleteListener(task -> {
                            // for Receiver Room
                            database
                                    .getReference()
                                    .child("chats")
                                    .child(receiverRoom)
                                    .child("messages")
                                    .push()
                                    .setValue(messages)
                                    .addOnCompleteListener(task1 -> {
                                    });
                        });


            }
        });
    }

    private void getChatData() {
        chatReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Messages messages = dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                // set the data change in recycler view
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}