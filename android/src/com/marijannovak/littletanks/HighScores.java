package com.marijannovak.littletanks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HighScores extends Activity {

    ListView lvLocal, lvOnline;
    ImageButton btnClear;
    ArrayList<ScoreItem> scoreList;
    ScoreAdapter scAdapter;
//TODO BACKGROUND ONLINE BAZA
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        this.btnClear = (ImageButton)findViewById(R.id.btnClear);
        this.lvLocal = (ListView) findViewById(R.id.lvLocalScores);
        this.lvOnline = (ListView) findViewById(R.id.lvOnlineScores);

        lvOnline.setVisibility(View.GONE);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent backIntent = new Intent(this, MenuActivity.class);
        startActivity(backIntent);
        finish();
    }
}
