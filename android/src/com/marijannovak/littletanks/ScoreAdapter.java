package com.marijannovak.littletanks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by marij on 24.5.2017..
 */

public class ScoreAdapter extends BaseAdapter {

    private ArrayList<ScoreItem> scoreList;

    public ScoreAdapter(ArrayList<ScoreItem> list) {

        this.scoreList = list;
    }

    @Override
    public int getCount() {
        return scoreList.size();
    }

    @Override
    public Object getItem(int position) {
        return scoreList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item_score, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ScoreItem scoreItem = this.scoreList.get(position);

        viewHolder.tvPlayerName.setText(String.format("Player name: %s", scoreItem.getPlayerName()));
        viewHolder.tvPlayTime.setText(String.format("Play time: %s", formatTime(scoreItem.getPlayTime())));
        viewHolder.tvScore.setText(String.format("Score: %d", scoreItem.getScore()));
        viewHolder.tvKilled.setText(String.format("Enemies killed: %d", scoreItem.getKilled()));

        return convertView;
    }

    public void addScoreItem(ScoreItem score){
        this.scoreList.add(score);
        this.notifyDataSetChanged();
    }

    public void removeAt(int position)
    {
        this.scoreList.remove(position);
        this.notifyDataSetChanged();
    }

    private static class ViewHolder{

        TextView tvPlayerName, tvPlayTime, tvScore, tvKilled;


        public ViewHolder(View feedView)
        {
            tvPlayerName = (TextView) feedView.findViewById(R.id.tvPlayerName);
            tvPlayTime = (TextView) feedView.findViewById(R.id.tvPlayTime);
            tvScore = (TextView) feedView.findViewById(R.id.tvScore);
            tvKilled = (TextView) feedView.findViewById(R.id.tvKilled);
        }

    }

    private String formatTime(int secs)
    {
        return secs/60 + " min " + secs%60 + " sec";
    }
}
