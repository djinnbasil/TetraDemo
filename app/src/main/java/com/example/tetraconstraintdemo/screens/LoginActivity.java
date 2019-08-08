package com.example.tetraconstraintdemo.screens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tetraconstraintdemo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1001;
    private EditText userName;
    private EditText userPassword;
    private ProgressDialog loadingDialog;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.userName);
        userPassword = findViewById(R.id.password);
        Button userLogin = findViewById(R.id.loginUser);
        TextView singUpUser = findViewById(R.id.signUpUser);
        SignInButton signInViaGoogle = findViewById(R.id.signInViaGoogle);

        userPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        loadingDialog = new ProgressDialog(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userNameString = userName.getText().toString();
                String password = userPassword.getText().toString();
                if (TextUtils.isEmpty(userNameString) || !android.util.Patterns.EMAIL_ADDRESS.matcher(userNameString).matches()) {
                    userName.setError("Please enter a valid email id");
                    return;
                }

                if (TextUtils.isEmpty(password) || password.length() < 6) {
                    userPassword.setError("Password min 6 characters");
                    return;
                }

                userName.setError(null);
                userPassword.setError(null);

                loginUser(userNameString, password);


            }
        });

        singUpUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userNameString = userName.getText().toString();
                String password = userPassword.getText().toString();
                if (TextUtils.isEmpty(userNameString) || !android.util.Patterns.EMAIL_ADDRESS.matcher(userNameString).matches()) {
                    userName.setError("Please enter a valid email id");
                    return;
                }

                if (TextUtils.isEmpty(password) || password.length() < 6) {
                    userPassword.setError("Password min 6 characters");
                    return;
                }

                userName.setError(null);
                userPassword.setError(null);

                signUpUser(userNameString, password);
            }
        });

        signInViaGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInViaGoogle();
            }
        });
    }

    public void loginUser(String email, String password) {


        loadingDialog.setTitle("Logging In");
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                loadingDialog.dismiss();

                                if (task.isSuccessful()) {


                                    Intent intent = new Intent(LoginActivity.this, MapsViewActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    new AlertDialog.Builder(LoginActivity.this)
                                            .setTitle("Login Failed")
                                            .setMessage(task.getException().getMessage())
                                            .setPositiveButton("DISMISS", null)
                                            .show();
                                }
                            }
                        });


    }

    public void signUpUser(String email, String password) {

        loadingDialog.setTitle("Signing Up");
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        loadingDialog.dismiss();

                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, MapsViewActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("Sign Up Failed")
                                    .setMessage(task.getException().getMessage())
                                    .setPositiveButton("DISMISS", null)
                                    .show();
                        }
                    }
                });
    }

    private void signInViaGoogle() {

        loadingDialog.setTitle("Signing In via Google");
        loadingDialog.setCancelable(false);
        loadingDialog.show();


        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                loadingDialog.dismiss();

                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Google Sign Up Failed")
                        .setMessage(task.getException().getMessage())
                        .setPositiveButton("DISMISS", null)
                        .show();

            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loadingDialog.dismiss();

                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, MapsViewActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("Google Sign Up Failed")
                                    .setMessage(task.getException().getMessage())
                                    .setPositiveButton("DISMISS", null)
                                    .show();
                        }
                    }
                });
    }
}
