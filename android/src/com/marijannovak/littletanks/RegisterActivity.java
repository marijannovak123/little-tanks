package com.marijannovak.littletanks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends Activity {

    EditText etUsername, etEmail, etPassword, etPasswordCheck;
    Button btnSignUp;

    private static final String KEY_RL = "key_rl";
    private static final String TAG = "RegisterActivity Debug";

    private int rlStatus;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getEmail());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

        Intent startingIntent = this.getIntent();

        if(startingIntent.hasExtra(KEY_RL))
        {
           rlStatus = startingIntent.getIntExtra(KEY_RL, 1);
        }

        etUsername = (EditText) findViewById(R.id.etUsername);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordCheck = (EditText) findViewById(R.id.etPasswordCheck);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        if(rlStatus == 0) {


            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    createNewUser(etEmail.getText().toString(), etPassword.getText().toString());

                }
            });

        }

        else if(rlStatus == 1)
        {
            btnSignUp.setText(R.string.log_in);
            etPasswordCheck.setVisibility(View.GONE);
            etUsername.setVisibility(View.GONE);

            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SignInUser(etEmail.getText().toString(), etPassword.getText().toString());
                }
            });

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createNewUser(final String email, final String password) {

        if(etUsername.getText().toString().isEmpty()) etUsername.setError("You must enter username");

        else if(etEmail.getText().toString().isEmpty()) etEmail.setError("You must enter an e-mail address!");

        else if(!isEmailValid(etEmail.getText().toString())) etEmail.setError("You haven't entered a valid e-mail address!");

        else if(etPassword.getText().toString().isEmpty()) etPassword.setError("You must enter a password!");

        else if(etPasswordCheck.getText().toString().isEmpty()) etPasswordCheck.setError("You must repeat your password!");

        else if(!etPassword.getText().toString().equals(etPasswordCheck.getText().toString())) etPasswordCheck.setError("Passwords don't match!");

        else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {

                                Toast.makeText(RegisterActivity.this, "Sign up failed!", Toast.LENGTH_SHORT).show();
                            }

                            else
                            {


                            }

                        }
                    });
        }

    }

    private void SignInUser(String email, String password) {

        if(etEmail.getText().toString().isEmpty()) etEmail.setError("You must enter an e-mail address!");

        else if(!isEmailValid(etEmail.getText().toString())) etEmail.setError("You haven't entered a valid e-mail address!");

        else if(etPassword.getText().toString().isEmpty()) etPassword.setError("You must enter a password!");

        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "SignUpWithEmail:onComplete:" + task.isSuccessful());

                            if (!task.isSuccessful())
                            {
                                Log.w(TAG, "SignUpWithEmail:failed", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed!",
                                        Toast.LENGTH_SHORT).show();
                            }

                            else
                            {
                                Toast.makeText(RegisterActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                                Intent backIntent = new Intent(RegisterActivity.this, MenuActivity.class);
                                startActivity(backIntent);
                                finish();
                            }

                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent backIntent = new Intent(this, MenuActivity.class);
        startActivity(backIntent);
        finish();
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
