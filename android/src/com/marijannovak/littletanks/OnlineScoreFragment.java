package com.marijannovak.littletanks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class OnlineScoreFragment extends Fragment {

    private ListView lvOnline;
    private ArrayList<ScoreItem> onlineScoreList;
    private ScoreAdapter onlineScAdapter;

    private FirebaseDatabase fbDatabase;
    private DatabaseReference dbRef;

    private ProgressBar spinnerProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.online_tab,container,false);

        fbDatabase = FirebaseDatabase.getInstance();
        dbRef = fbDatabase.getReference("");


            this.lvOnline = (ListView) view.findViewById(R.id.lvOnlineScores);
            this.spinnerProgress = (ProgressBar) view.findViewById(R.id.spinProgress);


            onlineScoreList = new ArrayList<>();

            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        ScoreItem score = new ScoreItem();
                        score.setPlayerName(data.child("username").getValue(String.class));
                        score.setScore(data.child("score").getValue(Integer.class));
                        score.setKilled(data.child("killed").getValue(Integer.class));
                        score.setPlayTime(data.child("time").getValue(Integer.class));

                        onlineScoreList.add(score);

                    }

                    Collections.sort(onlineScoreList, new Comparator<ScoreItem>() {
                        @Override
                        public int compare(ScoreItem item1, ScoreItem item2) {
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

                }
            });


        return view;
    }

}

