package com.okan.mytinder;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private static final int PICK_IMAGE_REQUEST = 234;

    Uri filePath;


    private EditText TextName;
    private EditText TextSurname;
    private EditText TextRPassword;
    private EditText TextRePassword;
    private EditText TextREmail;
    private EditText TextDate;
    private Button RegisterButton;
    private ImageView PPidturePick;
    private ImageView PPicture;
    private String city;
    ProgressDialog progressDialog;
    DatePickerDialog datePickerDialog;
    Spinner spinner;
    String REGEX = "^[&%$##@!~]";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        TextName = findViewById(R.id.Name);
        TextSurname = findViewById(R.id.Surname);
        TextRPassword = findViewById(R.id.RePassword);
        TextRePassword = findViewById(R.id.RPassword);
        TextREmail = findViewById(R.id.R_Email);
        RegisterButton = findViewById(R.id.Register);
        RegisterButton.setOnClickListener(this);
        PPidturePick= findViewById(R.id.PpUpload);
        PPidturePick.setOnClickListener(this);
        TextDate = findViewById(R.id.Birthday);
        TextDate.setOnClickListener(this);
        PPicture = findViewById(R.id.PPicture);
        progressDialog = new ProgressDialog(this);
        spinner = (Spinner)findViewById(R.id.spinner_dialog);



        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.cities_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) Register.this);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        city =parent.getItemAtPosition(position).toString();
        String [] cities = getResources().getStringArray(R.array.cities_options);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void userRegister() {
        final String name = TextName.getText().toString().trim();
        final String surname = TextSurname.getText().toString().trim();
        final String password  = TextRPassword.getText().toString().trim();
        final String repassword = TextRePassword.getText().toString().trim();
        final String email = TextREmail.getText().toString().trim();
        final String birthday = TextDate.getText().toString().trim();

        String REGEX = "[^&%$#@!~]*";
        //boolean valid = name.matches("(?i)(^[a-zA-Z])((?![ .,'-]$)[a-z .,'-]){4,24}$");

        /*Pattern special = Pattern.compile ("[^!@#$%&*()_+=|<>?{}\\[\\]~-]");
        Pattern digit = Pattern.compile("[0-9]");
        Matcher controlspecial = special.matcher(name);
        Matcher controldigit = digit.matcher(name);
        boolean match = controlspecial.matches();
        boolean match2 = controldigit.matches();
*/
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(name);


        if(TextUtils.isEmpty(email)&& TextUtils.isEmpty(password) && TextUtils.isEmpty(name) && TextUtils.isEmpty(surname) && TextUtils.isEmpty(repassword) && TextUtils.isEmpty(birthday)){

            Toast.makeText(this,"Lütfen tüm alanları doldurunuz..",Toast.LENGTH_LONG).show();
            return;
        }

        if(!matcher.matches()){
            Toast.makeText(this,"İsminiz özel karakter ve rakam içeremez", Toast.LENGTH_LONG).show();
            return;

        }

        if(password.length()<6){
            Toast.makeText(this,"Şifreniz 6 karakterden küçük olamaz..",Toast.LENGTH_LONG).show();
            return;
        }
        if(!password.equals(repassword)) {
            Toast.makeText(this, "Şifre  doğrulanamadı", Toast.LENGTH_LONG).show();
            return;
        }

        uploadProfilePicture();

        if (filePath == null){
             Toast.makeText(getApplicationContext(),"Lütfen resim yükleyiniz..",Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Lütfen Bekleyiniz..");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            final String userid=firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String,String> hashmap = new HashMap<>();
                            hashmap.put("id",userid);
                            hashmap.put("name",name);
                            hashmap.put("surName",surname);
                            hashmap.put("e-mail",email);
                            hashmap.put("password",password);
                            hashmap.put("birthday",birthday);
                            hashmap.put("city",city);
                            hashmap.put("username",name+" "+surname);
                            reference.setValue(hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {

                                        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference("ProfileInformation").child(userid);
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("userid","  ");
                                        hashMap.put("aboutme"," ");
                                        hashMap.put("education","   ");
                                        hashMap.put("hobbies"," ");
                                        hashMap.put("location",city);
                                        dbrf.setValue(hashMap);

                                        Toast.makeText(Register.this, "Kayıt Başarılı..", Toast.LENGTH_LONG).show();
                                        Intent goIntent = new Intent(Register.this, Login.class);
                                        startActivity(goIntent);
                                    }
                                }
                            });

                        }
                        else{
                            Toast.makeText(Register.this,"Lütfen tüm bilgileri kontrol ediniz..",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });


    }

    private void uploadProfilePicture() {

        if (filePath != null) {

            final String filename = System.currentTimeMillis()+" ";
            final StorageReference sRef = storageReference.child("image/*").child(filename);
            sRef.putFile(filePath)
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

                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            progressDialog.setMessage("Yükleniyor.." + ((int) progress) + "%..." );
                        }
                    });
        }

        else {
            Toast.makeText(getApplicationContext(), "Resim Bulunamadı..", Toast.LENGTH_LONG).show();
            return;
        }

    }

    private void birthdaychoose() {
        Calendar calendar = Calendar.getInstance();

        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                String date = day+"/"+(month+1)+"/"+year;
                TextDate.setText(date);
            }
        },year,month,day);
        datePickerDialog.show();

    }

    private void PickPPicture() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Profil Resminizi Seçiniz.."), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                PPicture.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View button) {
        switch(button.getId()){
            case R.id.Register :
                userRegister();
                break;
            case R.id.Birthday :
                birthdaychoose();
                break;
            case R.id.PpUpload :
                PickPPicture();
                break;
        }
    }
}
