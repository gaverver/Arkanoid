package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class YouWonScreen extends AppCompatActivity {
    TextView score;
    Button backToScreen;
    User user;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_losing_screen);
        this.score = findViewById(R.id.score);
        this.backToScreen = findViewById(R.id.homeScreen);
        this.backToScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LevelSelection.class);
                startActivity(intent);
                finish();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users").child(currentUser.getUid());

                referenceProfile.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (counter == 0) {
                            user = snapshot.getValue(User.class);
                            user.setNumGames(user.getNumGames()+1);
                            user.setGamesWon(user.getNumGames()+1);
                            referenceProfile.setValue(user);
                        }
                        counter++;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });
        int points = getIntent().getExtras().getInt("score");
        this.score.setText(points + "");

    }
}