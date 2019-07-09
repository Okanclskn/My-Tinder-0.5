package com.okan.mytinder.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.okan.mytinder.R;

public class ProfileAdapter  {
    private Context context;


    public ProfileAdapter(Context context) {
        this.context = context;

    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        private ImageView profilepic;
        private TextView username;


        public CardViewHolder(View view) {
            super(view);

            profilepic = view.findViewById(R.id.users_profile);
            username = view.findViewById(R.id.users_username);

        }

    }
}
