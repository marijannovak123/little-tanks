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
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.utils.StringBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends Activity implements View.OnClickListener{

    private static final String TAG = "MainActivity Debug";

    private SharedPreferences sharedPreferences;

    private String loginName;
    private boolean soundCheck, sensorCheck;
    private int diff;

    private Button playButton, scoreButton;
    private ImageButton settingsButton;
    private TextView authStatus;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

        sharedPreferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);

        soundCheck = sharedPreferences.getBoolean(Constants.Sound, false);
        sensorCheck = sharedPreferences.getBoolean(Constants.Sensors, false);
        diff = sharedPreferences.getInt(Constants.Difficulty, 1);
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.playBtn:

                if(loginName != null)
                {
                    Intent playIntent = new Intent(MenuActivity.this, AndroidLauncher.class);
                    playIntent.putExtra(Constants.KEY_PLAYER, loginName);
                    playIntent.putExtra(Constants.Sound, soundCheck);
                    playIntent.putExtra(Constants.Sensors, sensorCheck);
                    playIntent.putExtra(Constants.Difficulty, diff);
                    startActivity(playIntent);
                    finish();
                }

                else enterNameDialog();


                break;

            case R.id.scoresBtn:

                Intent scoreIntent = new Intent(this, ScoresActivity.class);
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


                else logOutDialog();

                break;

        }
    }

    private void registerLoginDialog() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Register/Login")

                .setMessage("Would you like to log in or register?")
                .setNegativeButton("Register", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent registerIntent = new Intent(MenuActivity.this, RegisterActivity.class);
                        registerIntent.putExtra(Constants.KEY_RL, 0);
                        startActivity(registerIntent);
                        finish();
                    }
                })
                .setPositiveButton("Log In", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent registerIntent = new Intent(MenuActivity.this, RegisterActivity.class);
                        registerIntent.putExtra(Constants.KEY_RL, 1);
                        startActivity(registerIntent);
                        finish();
                    }
                })
                .show();
    }

    private void logOutDialog()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Log Out")

                .setMessage("Would you like to log out?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth = FirebaseAuth.getInstance();

                        if(mAuth.getCurrentUser() != null) {

                            Toast.makeText(MenuActivity.this, mAuth.getCurrentUser().getEmail() + " signed out!", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                            authStatus.setText("Login/Register");
                            loginName = null;

                        }
                    }
                })
                .show();

    }

    private void enterNameDialog()
    {
        final EditText enterName = new EditText(this);
        enterName.setGravity(Gravity.CENTER_HORIZONTAL);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Choose name")

                .setMessage("Enter your name or log in to disable this dialog!")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setView(enterName)
                .setNeutralButton("Log In", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent registerIntent = new Intent(MenuActivity.this, RegisterActivity.class);
                        registerIntent.putExtra(Constants.KEY_RL, 1);
                        startActivity(registerIntent);
                        finish();
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(enterName.getText().toString().isEmpty())

                            enterName.setText("Unknown");

                        Intent playIntent = new Intent(MenuActivity.this, AndroidLauncher.class);
                        playIntent.putExtra(Constants.KEY_PLAYER, enterName.getText().toString());
                        playIntent.putExtra(Constants.Sound, soundCheck);
                        playIntent.putExtra(Constants.Sensors, sensorCheck);
                        playIntent.putExtra(Constants.Difficulty, diff);
                        startActivity(playIntent);
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
