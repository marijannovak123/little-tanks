package com.marijannovak.littletanks;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class HighScores extends Activity {

    ListView lvLocal, lvOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        this.lvLocal = (ListView) findViewById(R.id.lvLocalScores);
        this.lvOnline = (ListView) findViewById(R.id.lvOnlineScores);
    }
}
