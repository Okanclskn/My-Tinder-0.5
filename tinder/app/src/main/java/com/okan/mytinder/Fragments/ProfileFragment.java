package com.okan.mytinder.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.okan.mytinder.R;
import com.okan.mytinder.LinkClasses.User;
import com.okan.mytinder.UserInformation;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;

    CircleImageView profilepic;
    private TextView username;
    private TextView aboutme;
    private TextView education;
    private TextView hobbies;
    private TextView location;
    private Uri Url,file;
    FirebaseUser fuser;
    ProgressDialog progressDialog;

    StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 234;
    StorageTask uploadtask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_profile, container,false);
        Context c = getActivity().getApplicationContext();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        profilepic = view.findViewById(R.id.profile_picture);
        username = view.findViewById(R.id.username);
        aboutme = view.findViewById(R.id.aboutme);
        education = view.findViewById(R.id.education);
        hobbies = view.findViewById(R.id.hobbies);
        location = view.findViewById(R.id.location);
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picturePick();
            }
        });



        fuser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference rf = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid());

        rf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                Url= Uri.parse(user.getUserpicture());
                location.setText(user.getCity());
                Picasso.get().load(Url).into(profilepic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference rfinformation = FirebaseDatabase.getInstance().getReference().child("ProfileInformation").child(fuser.getUid());
        rfinformation.addValueEventListener(new ValueEventListener() {
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


        return view;
    }

    private void picturePick() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Profil Resminizi Seçiniz.."), PICK_IMAGE_REQUEST);
    }






    public void changePicture(){
        if (file != null) {

            final String filename = System.currentTimeMillis()+" ";
            final StorageReference sRef = storageReference.child("image/*").child(filename);
            sRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri download_url = uri;
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    String userid=firebaseUser.getUid();
                                    DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("userpicture",String.valueOf(download_url));

                                    dRef.updateChildren(hashMap);

                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getContext(), "Lütfen Bekleyiniz..", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        else {
            Toast.makeText(getContext(), "Resim Bulunamadı..", Toast.LENGTH_LONG).show();
            return;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            file = data.getData();
           if (uploadtask != null && uploadtask.isInProgress()){

           }
           else {
               changePicture();
           }
        }
    }
}





