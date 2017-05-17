package com.marijannovak.littletanks;

/**
 * Created by marij on 17.5.2017..
 */

public class ScoreItem {

    private String playerName;
    private int score, playTime, killed;

    public ScoreItem()
    {

    }

    public ScoreItem(String name, int scr, int time, int klld)
    {
        this.playerName = name;
        this.score = scr;
        this.playTime = time;
        this.killed = klld;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public int getPlayTime() {
        return playTime;
    }

    public int getKilled() {
        return killed;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public void setKilled(int killed) {
        this.killed = killed;
    }
}
