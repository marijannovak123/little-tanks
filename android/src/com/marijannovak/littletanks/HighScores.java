package com.marijannovak.littletanks;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class HighScores extends Activity {

    private ListView lvLocal, lvOnline;
    private ImageButton btnClear;
    private ArrayList<ScoreItem> scoreList, onlineScoreList;
    private ScoreAdapter scAdapter, onlineScAdapter;
    private TextView tvTitle;

    private FirebaseDatabase fbDatabase;
    private DatabaseReference dbRef;

    private ProgressBar spinnerProgress;

    private int loState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.btnClear = (ImageButton)findViewById(R.id.btnClear);
        this.lvLocal = (ListView) findViewById(R.id.lvLocalScores);
        this.lvOnline = (ListView) findViewById(R.id.lvOnlineScores);
        this.tvTitle = (TextView) findViewById(R.id.tvHighScores);
        this.spinnerProgress = (ProgressBar) findViewById(R.id.spinProgress);

        setUpLocal();
        setUpOnline();

        lvOnline.setVisibility(View.GONE);

        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loState == 0)
                {
                    tvTitle.setText("ONLINE HIGHSCORES");
                    loState = 1;
                    lvOnline.setVisibility(View.VISIBLE);
                    lvLocal.setVisibility(View.GONE);
                    btnClear.setVisibility(View.GONE);

                    if(onlineScoreList.isEmpty()) Toast.makeText(HighScores.this, "No online scores or you are not logged in!", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    tvTitle.setText("LOCAL HIGHSCORES");
                    loState = 0;
                    lvOnline.setVisibility(View.GONE);
                    lvLocal.setVisibility(View.VISIBLE);
                    btnClear.setVisibility(View.VISIBLE);

                    if(scoreList.isEmpty()) Toast.makeText(HighScores.this, "No score entries. Play the game and get a high score!", Toast.LENGTH_SHORT).show();


                }
            }
        });

    }

    public void setUpLocal()
    {
        scoreList = DatabaseHelper.getInstance(this).getHighScores();

        if(scoreList.isEmpty()) Toast.makeText(HighScores.this, "No score entries. Play the game and get a high score!", Toast.LENGTH_SHORT).show();

        Collections.sort(scoreList, new Comparator<ScoreItem>() {
            @Override public int compare(ScoreItem item1, ScoreItem item2) {
                return item2.getScore() - item1.getScore();
            }

        });

        scAdapter = new ScoreAdapter(scoreList);
        lvLocal.setAdapter(scAdapter);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(scoreList.size() > 0)
                {
                    DatabaseHelper.getInstance(HighScores.this).clearScoreTable();
                    scAdapter.clearItems();

                    Toast.makeText(HighScores.this, "High Score list cleared!", Toast.LENGTH_SHORT).show();

                }

                else
                {
                    Toast.makeText(HighScores.this, "Nothing to clear!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void setUpOnline()
    {

        fbDatabase = FirebaseDatabase.getInstance();
        dbRef = fbDatabase.getReference("");

        onlineScoreList = new ArrayList<>();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data: dataSnapshot.getChildren())
                {
                    ScoreItem score = new ScoreItem();
                    score.setPlayerName(data.child("username").getValue(String.class));
                    score.setScore(data.child("score").getValue(Integer.class));
                    score.setKilled(data.child("killed").getValue(Integer.class));
                    score.setPlayTime(data.child("time").getValue(Integer.class));

                    onlineScoreList.add(score);

                }

                Collections.sort(onlineScoreList, new Comparator<ScoreItem>() {
                    @Override public int compare(ScoreItem item1, ScoreItem item2) {
                        return item2.getScore() - item1.getScore();
                    }

                });

                onlineScAdapter = new ScoreAdapter(onlineScoreList);
                lvOnline.setAdapter(onlineScAdapter);

                spinnerProgress.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

                spinnerProgress.setVisibility(View.GONE);
                Toast.makeText(HighScores.this, "Failed getting online data! Login to continue!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent backIntent = new Intent(this, MenuActivity.class);
        startActivity(backIntent);
        finish();
    }
}
