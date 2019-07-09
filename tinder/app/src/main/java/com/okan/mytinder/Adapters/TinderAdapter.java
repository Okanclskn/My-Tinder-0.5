package com.okan.mytinder.Adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.okan.mytinder.LinkClasses.Pictures;
import com.okan.mytinder.LinkClasses.Tinder;
import com.okan.mytinder.R;
import com.okan.mytinder.LinkClasses.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TinderAdapter extends BaseAdapter {
    private Context context;
    private List<Pictures> tinList;


    public TinderAdapter(Context context, List<Pictures> tinList) {
        this.context = context;
        this.tinList = tinList;

    }


    @Override
    public int getCount() {
        return tinList.size();
    }

    @Override
    public Object getItem(int i) {
        return tinList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Pictures pictures = tinList.get(i);
        view = LayoutInflater.from(context).inflate(R.layout.tinder_card, viewGroup, false );
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.tinderpick = view.findViewById(R.id.cardImage2);
        viewHolder.url = Uri.parse(pictures.getImageurl());
        Picasso.get().load(viewHolder.url).into(viewHolder.tinderpick);

        return view;
    }
    public class ViewHolder{
        ImageView tinderpick;
        Uri url;

    }
}