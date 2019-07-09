package com.okan.mytinder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private Button LoginButton;
    private Button RegisterButton;
    private EditText TextEmail;
    private EditText TextPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;



    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null){
            Intent goIntent = new Intent(Login.this, MainActivity.class);
            startActivity(goIntent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        TextEmail = findViewById(R.id.E_mail);
        TextPassword = findViewById(R.id.Password);

        LoginButton = findViewById(R.id.Button_Login);
        LoginButton.setOnClickListener(this);
        RegisterButton = findViewById(R.id.Button_Register);
        RegisterButton.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }



    private void userLogin() {
        String email = TextEmail.getText().toString().trim();
        String password = TextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            TextEmail.setError("E-posta giriniz..");
            TextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            TextEmail.setError("Geçerli bir E-posta giriniz..");
            TextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            TextPassword.setError("Şifrenizi giriniz..");
            TextPassword.requestFocus();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Intent goIntent = new Intent(Login.this, MainActivity.class);
                            goIntent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK  );
                            startActivity(goIntent);
                            finish();

                        }
                        else{
                            Toast.makeText(getApplicationContext(),"E-posta veya şifre yanlış..",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }




    @Override
    public void onClick(View button) {
        switch (button.getId()){
            case R.id.Button_Login :
                userLogin();
                break;
            case R.id.Button_Register :
                Intent goIntent = new Intent(Login.this,Register.class);
                startActivity(goIntent);
        }
    }


}
