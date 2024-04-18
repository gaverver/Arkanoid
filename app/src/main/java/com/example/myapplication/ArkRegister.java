package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ArkRegister extends Activity {
    TextInputEditText email;
    TextInputEditText password;
    TextInputEditText user;
    Button registerBut;
    FirebaseAuth auth;
    ProgressBar bar;
    TextView view;
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = this.auth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainPage.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ark_register);
        this.auth = FirebaseAuth.getInstance();
        this.email = findViewById(R.id.email);
        this.user = findViewById(R.id.username);
        this.password = findViewById(R.id.password);
        this.registerBut = findViewById(R.id.regBtn);
        this.bar = findViewById(R.id.progress);
        this.view = findViewById(R.id.toLogin);
        this.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ArkLogin.class);
                startActivity(intent);
                finish();
            }
        });
        this.registerBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bar.setVisibility(View.VISIBLE);
                String readedEmail, readedPassword, readedUsername;
                readedUsername = String.valueOf(user.getText());
                readedEmail = String.valueOf(email.getText());
                readedPassword = String.valueOf(password.getText());
                if (TextUtils.isEmpty(readedUsername)) {
                    Toast.makeText(ArkRegister.this, "please enter a username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(readedEmail)) {
                    Toast.makeText(ArkRegister.this, "please enter an email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(readedPassword)) {
                    Toast.makeText(ArkRegister.this, "please enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.createUserWithEmailAndPassword(readedEmail, readedPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                bar.setVisibility(View.GONE);
                                UserProfileChangeRequest change = new UserProfileChangeRequest.Builder().setDisplayName(String.valueOf(user.getText())).build();
                                if (auth.getCurrentUser() != null) {
                                    auth.getCurrentUser().updateProfile(change);
                                }
                                if (task.isSuccessful()) {
                                    FirebaseUser currentUser = auth.getCurrentUser();
                                    User writeUserDetails = new User(readedPassword, readedEmail, readedUsername);
                                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
                                    referenceProfile.child(currentUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ArkRegister.this, "Registration Success.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), ArkLogin.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(ArkRegister.this, "Authentication failed. please try again", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(ArkRegister.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

}
