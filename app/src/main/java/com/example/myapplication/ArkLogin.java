package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.firebase.database.DatabaseReference;

public class ArkLogin extends Activity {
    private TextInputEditText email;
    private TextInputEditText password;
    private Button logButton;
    private FirebaseAuth auth;
    private ProgressBar bar;
    private TextView view;
    private CheckBox remember;
    private static final String SHARED_NAME = "SharedPrefs";

    private void checkBox() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_NAME, MODE_PRIVATE);
        String check = sharedPreferences.getString("name", "");
        if (check.equals("true")) {
            Toast.makeText(ArkLogin.this, "Login success!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainPage.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ark_login);
        this.remember = findViewById(R.id.rememberMeBox);
        this.auth = FirebaseAuth.getInstance();
        this.email = findViewById(R.id.email);
        this.password = findViewById(R.id.password);
        this.logButton = findViewById(R.id.loginBtn);
        this.bar = findViewById(R.id.progress);
        this.view = findViewById(R.id.toRegister);
        checkBox();
        this.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ArkRegister.class);
                startActivity(intent);
                finish();
            }
        });
        this.logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String readedEmail, readedPassword;
                readedEmail = String.valueOf(email.getText());
                readedPassword = String.valueOf(password.getText());
                if (TextUtils.isEmpty(readedEmail)) {
                    Toast.makeText(ArkLogin.this, "please enter an email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(readedPassword)) {
                    Toast.makeText(ArkLogin.this, "please enter an password", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.signInWithEmailAndPassword(readedEmail, readedPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                bar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_NAME, MODE_PRIVATE);
                                    if (remember.isChecked()) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("name", "true");
                                        editor.apply();
                                    }
                                    Toast.makeText(ArkLogin.this, "Login success!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainPage.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(ArkLogin.this, "Login failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}
