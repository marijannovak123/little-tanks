package com.marijannovak.littletanks;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class Settings extends Activity {

    ListView lvSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        lvSettings = (ListView) findViewById(R.id.lvSettings);
        SettingsAdapter settingsAdapter = new SettingsAdapter();
        lvSettings.setAdapter(settingsAdapter);
    }
}
