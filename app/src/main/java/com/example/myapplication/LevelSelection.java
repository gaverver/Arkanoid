package com.example.myapplication;




import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;


public class LevelSelection extends AppCompatActivity {
    ImageButton homePage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.homePage = findViewById(R.id.homePage);
        this.homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainPage.class);
                startActivity(intent);
                finish();
            }
        });

    }
    public void startFirstLevel(View view) {
        GameLevelOne gameLevelOne = new GameLevelOne(this);
        setContentView(gameLevelOne);

    }
    public void startSecondLevel(View view) {
        GameLevelTwo gameLevelOne = new GameLevelTwo(this);
        setContentView(gameLevelOne);
    }
    public void startThirdLevel(View view) {
        GameLevelThree gameLevelOne = new GameLevelThree(this);
        setContentView(gameLevelOne);
    }

}