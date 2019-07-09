package com.okan.mytinder;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.okan.mytinder.Adapters.UsersAdapter;
import com.okan.mytinder.LinkClasses.User;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    private CircleImageView profilepic;
    private TextView username;
    private TextView aboutme;
    private TextView education;
    private TextView hobbies;
    private TextView location;
    private Uri Url;
    private List<User> userList;
    UsersAdapter usersAdapter;
    String userid, uid;

    DatabaseReference databaseReference, dbrf;
    FirebaseUser firebaseUser;
    Intent intent;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);


        profilepic = findViewById(R.id.profile_picture);
        username = findViewById(R.id.username);
        aboutme = findViewById(R.id.aboutme);
        education = findViewById(R.id.education);
        hobbies = findViewById(R.id.hobbies);
        location = findViewById(R.id.location);

        intent = getIntent();
        userid = intent.getStringExtra("userid");



        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                Url = Uri.parse(user.getUserpicture());
                Picasso.get().load(Url).into(profilepic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dbrf = FirebaseDatabase.getInstance().getReference("ProfileInformation").child(userid);
        dbrf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                aboutme.setText(userInformation.getAboutme());
                education.setText(userInformation.getEducation());
                hobbies.setText(userInformation.getHobbies());
                location.setText(userInformation.getLocation());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profilemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.sendmessage:

                startActivity(new Intent(Profile.this, Message.class));
                return true;
        }
        return false;
    }
    */
}
