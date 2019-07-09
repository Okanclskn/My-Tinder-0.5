package com.okan.mytinder.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.okan.mytinder.LinkClasses.User;

import com.okan.mytinder.Message;
import com.okan.mytinder.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder>{
    private Context mContext;
    private List<User> mUsers;

    public ChatListAdapter(Context mContext, List<User> mUsers){
        this.mUsers=mUsers;
        this.mContext=mContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chatprofile, parent, false );
        return new ChatListAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = mUsers.get(position);
        holder.username.setText(user.getUsername());

        holder.url= Uri.parse(user.getUserpicture());
        Picasso.get().load(holder.url).into(holder.profile_picture);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*
                intent = ((Activity) mContext).getIntent();
                userid = intent.getStringExtra("userid");
                ((Activity) mContext).finish();

                iduser.setUserid(userid);
                Toast.makeText(mContext, userid, Toast.LENGTH_SHORT).show();
*/

                Intent ıntent = new Intent(mContext, Message.class);
                ıntent.putExtra("userid", user.getId());
                mContext.startActivity(ıntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_picture;
        private Uri url;

        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_picture = itemView.findViewById(R.id.profile_picture);
        }
    }
}
