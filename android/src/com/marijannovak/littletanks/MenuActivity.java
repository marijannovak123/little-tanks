package com.marijannovak.littletanks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.badlogic.gdx.utils.StringBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//TODO SHARED PREFS ZA LOGIN I OSTALO..SPAJANJE SAMO
public class MenuActivity extends Activity implements View.OnClickListener{

    private static final String TAG = "MainActivity Debug";
    private static final String KEY_PLAYER = "key_player";
    private static final String KEY_RL = "key_rl";
    private String loginName;
    private Button playButton, scoreButton;
    private ImageButton settingsButton;
    private TextView authStatus;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    loginName = getUsernameFromEmail(user.getEmail());

                    authStatus.setText("Logged in: " + loginName);

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getEmail());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        playButton = (Button) findViewById(R.id.playBtn);
        scoreButton = (Button) findViewById(R.id.scoresBtn);
        settingsButton = (ImageButton) findViewById(R.id.btnSettings);
        authStatus = (TextView) findViewById(R.id.authStatus);


        playButton.setOnClickListener(this);
        scoreButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
        authStatus.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //TODO database
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.playBtn:

                Intent playIntent = new Intent(this, AndroidLauncher.class);
                playIntent.putExtra(KEY_PLAYER, loginName);
                startActivity(playIntent);
                finish();

                break;

            case R.id.scoresBtn:

                Intent scoreIntent = new Intent(this, HighScores.class);
                startActivity(scoreIntent);
                finish();

                break;

            case R.id.btnSettings:

                Intent settingsIntent = new Intent(this, Settings.class);
                startActivity(settingsIntent);
                finish();

                break;

            case R.id.authStatus:

                if(authStatus.getText().equals("Login/Register"))
                    registerLoginDialog();
                break;

        }
    }

    private void registerLoginDialog() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Register/Login")

                .setMessage("Would you like to log in or register?")
                .setNeutralButton("Register", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent registerIntent = new Intent(MenuActivity.this, RegisterActivity.class);
                        registerIntent.putExtra(KEY_RL, 0);
                        startActivity(registerIntent);
                        finish();
                    }
                })
                .setPositiveButton("Log In", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent registerIntent = new Intent(MenuActivity.this, RegisterActivity.class);
                        registerIntent.putExtra(KEY_RL, 1);
                        startActivity(registerIntent);
                        finish();
                    }
                })
                .show();
    }

    private String getUsernameFromEmail(String email)
    {
        int i = 0;
        StringBuilder stringBuilder = new StringBuilder();

        while(email.charAt(i) != '@')
        {
            stringBuilder.append(email.charAt(i));
            i++;
        }

        return stringBuilder.toString();

    }
}
