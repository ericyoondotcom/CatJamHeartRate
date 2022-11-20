package com.yoonicode.catjam_wearos;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.yoonicode.catjam_wearos.databinding.ActivityAuthenticationBinding;

public class AuthenticationActivity extends Activity {

    FirebaseManager firebase;
    private ActivityAuthenticationBinding binding;
    private SignInButton button;
    private GoogleSignInClient signInClient;
    private final int GOOGLE_REQUEST_CODE = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        button = binding.signInButton;

        button.setOnClickListener(view -> { signIn(); });

        if(FirebaseManager.instance == null) {
            firebase = new FirebaseManager(this);
        } else {
            firebase = FirebaseManager.instance;
        }

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.firebase_web_client_id))
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this, options);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void signIn() {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode != GOOGLE_REQUEST_CODE) return;
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            authenticateWithCredential(account);
        } catch(ApiException e) {
            Toast.makeText(AuthenticationActivity.this, "Google sign in failed", Toast.LENGTH_LONG).show();
            Log.d("sign in", "Google sign in failed: ", e);
        }
    }

    private void authenticateWithCredential(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        FirebaseManager.instance.getAuth().signInWithCredential(credential)
                .addOnSuccessListener(this, res -> {
                    Toast.makeText(AuthenticationActivity.this, "Signed in!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(this, e -> {
                    Toast.makeText(AuthenticationActivity.this, "Firebase sign in failed", Toast.LENGTH_LONG).show();
                });
    }
}
