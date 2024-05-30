package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainPage extends AppCompatActivity {
    private Button logOutBtn;
    private Button playButton;
    private Button dataButton;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextView welcome;
    BroadcastReceiver broadcastReceiver;
    private static final String SHARED_NAME = "SharedPrefs";

    public void onStart() {
        super.onStart();
    }
    public String getUsername() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getDisplayName();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        //createNotificationChannel();
        this.logOutBtn = findViewById(R.id.logoutBtn);
        this.auth = FirebaseAuth.getInstance();
        this.user = this.auth.getCurrentUser();
        this.welcome = findViewById(R.id.welcomeMess);
        this.playButton = findViewById(R.id.playBtn);
        this.dataButton = findViewById(R.id.dataBtn);
        if (this.user == null) {
            Intent intent = new Intent(getApplicationContext(), ArkLogin.class);
            startActivity(intent);
            finish();
        } else {
            String s = "Hello " + this.getUsername() + "!";
            this.welcome.setText(s);
        }
        this.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LevelSelection.class);
                startActivity(intent);
                finish();
            }
        });
        this.dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(MainPage.this, MessageReminder.class);
                intent.setAction("NOTIFICATION_ACTION");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainPage.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                long triggerTime = System.currentTimeMillis() + 1000; // 10 seconds
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            }
        });
        this.logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", "");
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), ArkLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }
}