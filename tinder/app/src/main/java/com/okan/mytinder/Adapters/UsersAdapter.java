package com.okan.mytinder.Adapters;

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
import com.okan.mytinder.Profile;
import com.okan.mytinder.R;
import com.okan.mytinder.LinkClasses.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.CardViewHolder>{
    private Context mContext;
    private List<User> userList;


    public UsersAdapter(Context mContext, List<User> userList) {
        this.mContext = mContext;
        this.userList = userList;
    }



    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.card_view,parent,false);

        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        final User user = userList.get(position);
        holder.username.setText(user.getUsername());
        holder.url= Uri.parse(user.getUserpicture());
        Picasso.get().load(holder.url).into(holder.userpicture);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Profile.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public class CardViewHolder extends RecyclerView.ViewHolder{
        private ImageView userpicture;
        private TextView username;
        private Uri url;



        public CardViewHolder(View v){
            super(v);

            userpicture = v.findViewById(R.id.users_profile);
            username = v.findViewById(R.id.users_username);

        }

    }
}
