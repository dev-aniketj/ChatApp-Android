package com.aniketjain.chatappjava.Adapter;

import static com.aniketjain.chatappjava.Activity.ChatActivity.receiverImage;
import static com.aniketjain.chatappjava.Activity.ChatActivity.senderImage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aniketjain.chatappjava.Model.Messages;
import com.aniketjain.chatappjava.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final ArrayList<Messages> messagesArrayList;
    private final int ITEM_SEND = 1;
    private final int ITEM_RECEIVE = 2;

    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages = messagesArrayList.get(position);

        if (holder.getClass() == SenderViewHolder.class) {
            SenderViewHolder senderViewHolder = (SenderViewHolder) holder;

            senderViewHolder.senderMessageTv.setText(messages.getMessage());
            Picasso.get().load(senderImage).into(senderViewHolder.senderProfileIv);
        } else {
            ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder) holder;

            receiverViewHolder.receiverMessageTv.setText(messages.getMessage());
            Picasso.get().load(receiverImage).into(receiverViewHolder.receiverProfileIv);
        }
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Log.i("message_pos", messagesArrayList.get(position).toString());

        // check the user is sender or receiver
        Messages messages = messagesArrayList.get(position);
        if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()
                .equals(messages.getSenderId())) {
            return ITEM_SEND;
        } else {
            return ITEM_RECEIVE;
        }
    }

    static class SenderViewHolder extends RecyclerView.ViewHolder {

        CircleImageView senderProfileIv;
        TextView senderMessageTv;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderProfileIv = itemView.findViewById(R.id.sender_profile_iv);
            senderMessageTv = itemView.findViewById(R.id.sender_message_tv);

        }
    }

    static class ReceiverViewHolder extends RecyclerView.ViewHolder {

        CircleImageView receiverProfileIv;
        TextView receiverMessageTv;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receiverProfileIv = itemView.findViewById(R.id.receiver_profile_iv);
            receiverMessageTv = itemView.findViewById(R.id.receiver_message_tv);
        }
    }
}
