package com.okan.mytinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.okan.mytinder.Adapters.UsersAdapter;
import com.okan.mytinder.LinkClasses.User;

import java.util.ArrayList;

public class Users extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<User> users;
    private UsersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));



        users = new ArrayList<>();


        adapter = new UsersAdapter(this,users);
        recyclerView.setAdapter(adapter);

    }
}
