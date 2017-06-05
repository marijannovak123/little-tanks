package com.marijannovak.littletanks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Settings extends Activity {

    private static final String TAG = "Settings Debug";
    public static final String KEY_RL = "key_rl";

    //TODO BACKGOURND
    ListView lvSettings;
    ArrayList<SettingItem> settingItems;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingItems = new ArrayList<>();

        settingItems.add(new SettingItem("Sound", true));
        settingItems.add(new SettingItem("Sensor move", false));
        settingItems.add(new SettingItem("Difficulty"));
        settingItems.add(new SettingItem("Login/Logout"));
        settingItems.add(new SettingItem("About"));

        lvSettings = (ListView) findViewById(R.id.lvSettings);
        SettingsAdapter settingsAdapter = new SettingsAdapter(settingItems);
        lvSettings.setAdapter(settingsAdapter);

        lvSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 3:

                        mAuth = FirebaseAuth.getInstance();

                        if(mAuth.getCurrentUser() != null) {

                            Toast.makeText(Settings.this, mAuth.getCurrentUser().getEmail() + " signed out!", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();

                        }

                        else
                        {
                            Intent registerIntent = new Intent(Settings.this, RegisterActivity.class);
                            registerIntent.putExtra(KEY_RL, 1);
                            startActivity(registerIntent);
                            finish();
                        }


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
