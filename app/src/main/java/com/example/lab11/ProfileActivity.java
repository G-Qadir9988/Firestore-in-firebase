package com.example.lab11;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent; // NEW: Needed for navigation
import android.widget.Button; // NEW: Needed to handle the Edit Profile button
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView tvUserEmail;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // 1. Theme Application (FIXED)
        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String currentTheme = sharedPreferences.getString("currentTheme", "Normal");
        setAppTheme(currentTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        tvUserEmail = findViewById(R.id.tv_user_email);

        // NEW: Find the Edit Profile button
        Button editButton = findViewById(R.id.btn_edit_profile);

        // NEW: Set listener to open the EditProfileActivity
        editButton.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
        });

        displayUserProfile();
    }

    // Helper method to display user email
    private void displayUserProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            if (email != null) {
                tvUserEmail.setText("Logged in as:\n" + email);
            } else {
                tvUserEmail.setText("Email not found.");
            }
        } else {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            // Optionally redirect to login if the user is somehow unauthenticated here
        }
    }

    // Helper method to apply theme dynamically (Copied from Dashboard/Settings)
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