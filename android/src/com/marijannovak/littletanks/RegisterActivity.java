package com.marijannovak.littletanks;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private static final int RC_SIGN_IN = 1;

    private int rlStatus;

    private EditText etEmail, etPassword, etPasswordCheck;
    private Button btnSignUp;
    private SignInButton googleSignIn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;

    ProgressBar spinnerProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        this.spinnerProgress = (ProgressBar) findViewById(R.id.spinProgress2);

        spinnerProgress.setVisibility(View.GONE);

        setUpFirebase();

        referenceViews();

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });

        decideRegisterLogin();


    }



    private void referenceViews() {
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordCheck = (EditText) findViewById(R.id.etPasswordCheck);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        googleSignIn = (SignInButton) findViewById(R.id.googleSignIn);
    }

    private void setUpFirebase() {

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mAuth.getCurrentUser() != null)
                {
                    startActivity(new Intent(RegisterActivity.this, MenuActivity.class));
                    finish();
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener(){

                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(RegisterActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void googleSignIn() {

        spinnerProgress.setVisibility(View.VISIBLE);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void decideRegisterLogin() {

        Intent startingIntent = this.getIntent();

        if(startingIntent.hasExtra(Constants.KEY_RL))
        {
            rlStatus = startingIntent.getIntExtra(Constants.KEY_RL, 1);
        }
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

            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SignInUser(etEmail.getText().toString(), etPassword.getText().toString());
                }
            });

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            spinnerProgress.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Google sign in success!", Toast.LENGTH_SHORT).show();


                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            spinnerProgress.setVisibility(View.GONE);

                            Toast.makeText(RegisterActivity.this, "Authentication failed!",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void createNewUser(final String email, final String password) {

        if(etEmail.getText().toString().isEmpty()) etEmail.setError("You must enter an e-mail address!");

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
                                mAuth.getCurrentUser().sendEmailVerification();

                                Toast.makeText(RegisterActivity.this, mAuth.getCurrentUser().getEmail()+ " succesfully signed up!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, MenuActivity.class));
                                finish();
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

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent backIntent = new Intent(this, MenuActivity.class);
        startActivity(backIntent);
        finish();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }


}
