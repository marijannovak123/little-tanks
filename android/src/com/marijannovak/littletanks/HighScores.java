package com.marijannovak.littletanks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class HighScores extends Activity {

    ListView lvLocal, lvOnline;
    ArrayList<ScoreItem> scoreList;
//TODO BACKGROUND ONLINE BAZA
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        this.lvLocal = (ListView) findViewById(R.id.lvLocalScores);
        this.lvOnline = (ListView) findViewById(R.id.lvOnlineScores);

        lvOnline.setVisibility(View.GONE);

        scoreList = DatabaseHelper.getInstance(this).getHighScores();

        final ScoreAdapter scAdapter = new ScoreAdapter(scoreList);
        lvLocal.setAdapter(scAdapter);


        lvLocal.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                scAdapter.removeAt(position);
                DatabaseHelper.getInstance(HighScores.this).deleteScoreItem(position+1);

                Toast.makeText(HighScores.this, "Score record removed!", Toast.LENGTH_SHORT).show();

                return true;
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
