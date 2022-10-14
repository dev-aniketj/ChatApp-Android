package com.aniketjain.chatappjava.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aniketjain.chatappjava.Model.Users;
import com.aniketjain.chatappjava.R;
import com.aniketjain.roastedtoast.Toasty;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserItemViewHolder> {

    private Context context;
    private ArrayList<Users> usersArrayList;

    public UserAdapter(Context context, ArrayList<Users> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public UserItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_layout, parent, false);
        return new UserItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserItemViewHolder holder, int position) {

        Users users = usersArrayList.get(position);

        holder.userRl.setOnClickListener(view -> {
            Toasty.normal(context, "Tap on User");
        });

        //Picasso.get().load(users.getImage_uri()).into(holder.profileIv);
        holder.nameTv.setText(users.getName());
        holder.statusTv.setText(users.getStatus());
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    static class UserItemViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout userRl;
        private CircleImageView profileIv;
        private TextView nameTv, statusTv;

        public UserItemViewHolder(@NonNull View itemView) {
            super(itemView);
            userRl = itemView.findViewById(R.id.user_rl);
            profileIv = itemView.findViewById(R.id.user_profile_iv);
            nameTv = itemView.findViewById(R.id.user_name_tv);
            statusTv = itemView.findViewById(R.id.user_status_tv);
        }
    }
}
