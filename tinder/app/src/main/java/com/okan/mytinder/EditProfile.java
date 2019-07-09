package com.okan.mytinder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.okan.mytinder.Fragments.ProfileFragment;

import java.util.HashMap;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;


    private EditText editAboutme;
    private EditText editEducation;
    private EditText editHobbies;
    private EditText editLocation;
    private Button buttonUpdate;
    private String userid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);



        editAboutme = findViewById(R.id.editAboutme);
        editEducation = findViewById(R.id.editEducation);
        editHobbies = findViewById(R.id.editHobbies);
        editLocation = findViewById(R.id.editLocation);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonUpdate :
            updateProfile();
            break;
        }
    }

    private void updateProfile() {

        String  aboutme = editAboutme.getText().toString().trim();
        String  education = editEducation.getText().toString().trim();
        String  hobbies = editHobbies.getText().toString().trim();
        String  location = editLocation.getText().toString().trim();


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userid = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("ProfileInformation").child(userid);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userid",userid);
        hashMap.put("aboutme",aboutme);
        hashMap.put("education",education);
        hashMap.put("hobbies",hobbies);
        hashMap.put("location",location);
        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Intent goIntent = new Intent(EditProfile.this, MainActivity.class);
                    startActivity(goIntent);
                }
            }
        });

    }

}
