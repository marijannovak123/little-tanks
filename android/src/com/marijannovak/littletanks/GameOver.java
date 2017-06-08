package com.marijannovak.littletanks;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GameOver extends Activity implements View.OnClickListener {
    
    private String playerName;
    private int score, time, killed;

    private TextView tvScore;
    private Button btnPlayAgain, btnMain;

    private FirebaseDatabase fbDatabase;
    private DatabaseReference dbRef;

    private boolean existsCheck = false;
    private Integer playerScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        tvScore = (TextView) findViewById(R.id.tvScore);
        btnPlayAgain = (Button) findViewById(R.id.btnAgain);
        btnMain = (Button) findViewById(R.id.btnMain);

        btnPlayAgain.setOnClickListener(this);
        btnMain.setOnClickListener(this);

        Intent startingIntent = this.getIntent();

        if(startingIntent.hasExtra(Constants.KEY_PLAYER))
        {
            playerName = startingIntent.getStringExtra(Constants.KEY_PLAYER);
        }

        if(startingIntent.hasExtra(Constants.KEY_SCORE))
        {
            score = startingIntent.getIntExtra(Constants.KEY_SCORE, 0);
        }

        if(startingIntent.hasExtra(Constants.KEY_TIME))
        {
            time = startingIntent.getIntExtra(Constants.KEY_TIME, 0);
        }

        if(startingIntent.hasExtra(Constants.KEY_KILLED))
        {
            killed = startingIntent.getIntExtra(Constants.KEY_KILLED, 0);
        }

        DatabaseHelper.getInstance(this).addScore(new ScoreItem(playerName, score, time, killed));

        fbDatabase = FirebaseDatabase.getInstance();
        dbRef = fbDatabase.getReference("");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(playerName))
                {
                    playerScore = dataSnapshot.child(playerName).child("score").getValue(Integer.class);

                    if (playerScore != null && playerScore < score)
                    {
                        dbRef.child(playerName).child("score").setValue(score);
                        dbRef.child(playerName).child("killed").setValue(killed);
                        dbRef.child(playerName).child("time").setValue(time);

                        Toast.makeText(GameOver.this, "New high score! Saving to online database!", Toast.LENGTH_SHORT).show();
                    }

                } else createOnlineDatabaseEntry(playerName);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnAgain:

                Intent againIntent = new Intent(this, AndroidLauncher.class);
                againIntent.putExtra(Constants.KEY_PLAYER, playerName);
                startActivity(againIntent);
                finish();

                break;

            case R.id.btnMain:

                Intent mainIntent = new Intent(this, MenuActivity.class);
                startActivity(mainIntent);
                finish();

                break;
        }
    }

    private void createOnlineDatabaseEntry(String name) {

        dbRef.child(name).setValue(0);
        dbRef.child(name).child("username").setValue(name);
        dbRef.child(name).child("score").setValue(score);
        dbRef.child(name).child("killed").setValue(killed);
        dbRef.child(name).child("time").setValue(time);

        Toast.makeText(GameOver.this, "New high score! Saving to online database!", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onBackPressed() {

    }
}
