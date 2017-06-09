package com.marijannovak.littletanks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Settings extends Activity {

    private static final String TAG = "Settings Debug";

    private int diff;

    private SharedPreferences sharedPreferences;

    private ListView lvSettings;
    private ArrayList<SettingItem> settingItems;
    private SettingsAdapter settingsAdapter;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fillSettingsList();

        setUpLV();

    }

    private void setUpLV() {

        lvSettings = (ListView) findViewById(R.id.lvSettings);
        settingsAdapter = new SettingsAdapter(settingItems);
        lvSettings.setAdapter(settingsAdapter);

        lvSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    //za 0 i 1 implementirano u adapteru zbog pristupa checkboxu

                    case 2:

                        if(diff <= 2) diff++;
                        else diff = 1;

                        settingsAdapter.updateDiff(diff);

                        sharedPreferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putInt(Constants.Difficulty, diff);

                        editor.commit();

                        break;


                    case 3:

                        mAuth = FirebaseAuth.getInstance();

                        if(mAuth.getCurrentUser() != null) {

                            Toast.makeText(Settings.this, mAuth.getCurrentUser().getEmail() + " signed out!", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();

                        }

                        else
                        {
                            Intent registerIntent = new Intent(Settings.this, RegisterActivity.class);
                            registerIntent.putExtra(Constants.KEY_RL, 1);
                            startActivity(registerIntent);
                            finish();
                        }

                        break;

                    case 4:

                        displayAbout();
                        break;

                }
            }
        });
    }

    private void fillSettingsList() {

        settingItems = new ArrayList<>();

        sharedPreferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);


        settingItems.add(new SettingItem("Sound", sharedPreferences.getBoolean(Constants.Sound, true)));
        settingItems.add(new SettingItem("Sensor move", sharedPreferences.getBoolean(Constants.Sensors, false)));

        diff = sharedPreferences.getInt(Constants.Difficulty, 1);

        switch (diff)
        {
            case 1:
                settingItems.add(new SettingItem("Difficulty: Easy"));

                break;

            case 2:
                settingItems.add(new SettingItem("Difficulty: Medium"));

                break;

            case 3:
                settingItems.add(new SettingItem("Difficulty: Hard"));

                break;
        }

        settingItems.add(new SettingItem("Login/Logout"));
        settingItems.add(new SettingItem("About"));

    }

    private void displayAbout() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("About")

                .setMessage("\nMarijan Novak\n\n1.DKB\n\nRazvoj mobilnih aplikacija\n\nFERIT\n\n2017.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        savePrefs();

        Intent backIntent = new Intent(this, MenuActivity.class);
        startActivity(backIntent);
        finish();
    }

    private void savePrefs() {

        sharedPreferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(Constants.Sensors, settingsAdapter.getSensorStatus());
        editor.putBoolean(Constants.Sound, settingsAdapter.getSoundStatus());
        editor.commit();
    }

}
