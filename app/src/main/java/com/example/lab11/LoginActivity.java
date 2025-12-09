package com.example.lab11; // Use your package name

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // --- Theme Application Logic ---
        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        // Default to Normal theme if not set
        String currentTheme = sharedPreferences.getString("currentTheme", "Normal");
        setAppTheme(currentTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // --- End Theme Logic ---

        mAuth = FirebaseAuth.getInstance();

        EditText emailField = findViewById(R.id.et_email);
        EditText passwordField = findViewById(R.id.et_password);
        Button loginButton = findViewById(R.id.btn_login);
        TextView signUpLink = findViewById(R.id.tv_signup_link);

        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and password cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Firebase Login
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Authentication Succeeded.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        signUpLink.setOnClickListener(v -> {
            // Go to SignUp page
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });
    }

    // Check if user is already logged in
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
        }
    }

    // Helper method to apply theme dynamically
    private void setAppTheme(String themeName) {
        switch (themeName) {
            case "Dark":
                setTheme(R.style.Theme_Dark);
                break;
            case "Light":
                setTheme(R.style.Theme_Light);
                break;
            case "Normal":
            default:
                setTheme(R.style.Theme_Normal);
                break;
        }
    }
}