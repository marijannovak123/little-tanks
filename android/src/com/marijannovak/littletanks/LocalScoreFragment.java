package com.marijannovak.littletanks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by marij on 13.6.2017..
 */

public class LocalScoreFragment extends Fragment {

    private ListView lvLocal;
    private ArrayList<ScoreItem> scoreList;
    private ScoreAdapter scAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.local_tab,container,false);

        this.lvLocal = (ListView) view.findViewById(R.id.lvLocalScores);
        scoreList = DatabaseHelper.getInstance(getActivity()).getHighScores();

        if(scoreList.isEmpty()) Toast.makeText(getActivity(), "No score entries. Play the game and get a high score!", Toast.LENGTH_SHORT).show();

        Collections.sort(scoreList, new Comparator<ScoreItem>() {
            @Override public int compare(ScoreItem item1, ScoreItem item2) {
                return item2.getScore() - item1.getScore();
            }

        });

        scAdapter = new ScoreAdapter(scoreList);
        lvLocal.setAdapter(scAdapter);

        lvLocal.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if(scoreList.size() > 0)
                {
                    DatabaseHelper.getInstance(getActivity()).clearScoreTable();
                    scAdapter.clearItems();

                    Toast.makeText(getActivity(), "High Score list cleared!", Toast.LENGTH_SHORT).show();

                }

                else
                {
                    Toast.makeText(getActivity(), "Nothing to clear!", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        return view;
    }
}
