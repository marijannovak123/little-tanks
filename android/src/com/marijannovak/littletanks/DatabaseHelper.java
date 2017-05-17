package com.marijannovak.littletanks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by marij on 20.4.2017..
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper Instance = null;

    private DatabaseHelper(Context context)
    {
        super(context.getApplicationContext(),DatabaseScheme.DB_NAME, null, DatabaseScheme.VERSION);

    }

    public static synchronized DatabaseHelper getInstance(Context context)
    {
        if(Instance == null)
        {
            Instance = new DatabaseHelper(context);
        }

        return Instance;
    }

    private static final String SELECT_SCORES = "SELECT * FROM " + DatabaseScheme.SCORE_TABLE;

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + DatabaseScheme.SCORE_TABLE + " (" + DatabaseScheme.PLAYER + " TEXT, " +
                DatabaseScheme.PLAYTIME + " INT, " + DatabaseScheme.SCORE + " INT, " +
                DatabaseScheme.KILLED + " INT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DatabaseScheme.SCORE_TABLE);
        this.onCreate(db);
    }

    public void addScore(ScoreItem score)
    {
        ContentValues cValues = new ContentValues();

        cValues.put(DatabaseScheme.PLAYER, score.getPlayerName());
        cValues.put(DatabaseScheme.SCORE, score.getPlayerName());
        cValues.put(DatabaseScheme.PLAYTIME, score.getPlayerName());
        cValues.put(DatabaseScheme.KILLED, score.getPlayerName());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DatabaseScheme.SCORE_TABLE, DatabaseScheme.PLAYER, cValues);
        db.close();
    }

    public ArrayList<ScoreItem> getHighScores()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor scoreCursor = db.rawQuery(SELECT_SCORES, null);
        ArrayList<ScoreItem> scoreList = new ArrayList<>();


        if(scoreCursor.moveToFirst())
        {
            do {

                ScoreItem score = new ScoreItem();
                score.setPlayerName(scoreCursor.getString(0));
                score.setScore(scoreCursor.getInt(1));
                score.setPlayTime(scoreCursor.getInt(2));
                score.setKilled(scoreCursor.getInt(3));

                scoreList.add(score);

            }while (scoreCursor.moveToNext());
        }

        scoreCursor.close();
        db.close();

        return scoreList;
    }

    public void deleteScoreItem(String player)
    {
        SQLiteDatabase writeableDatabase = this.getWritableDatabase();
        writeableDatabase.delete(DatabaseScheme.SCORE_TABLE, DatabaseScheme.PLAYER + " = ? ", new String[] {player});
    }

    public static class DatabaseScheme
    {

        private static final int VERSION = 1;
        private static final String DB_NAME = "scoresDB.db";
        private static final String SCORE_TABLE = "SCORES";
        private static final String PLAYER = "player";
        private static final String SCORE  = "score";
        private static final String PLAYTIME = "playtime";
        private static final String KILLED = "killed";

    }
}
