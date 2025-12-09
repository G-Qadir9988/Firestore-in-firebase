package com.example.lab11;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

// NEW IMPORT: Firebase Analytics
import com.google.firebase.analytics.FirebaseAnalytics;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private EditText etName, etEmail, etPhone;

    // NEW: Declare Firebase Analytics instance
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // 1. Apply Theme
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String currentTheme = sharedPreferences.getString("currentTheme", "Normal");
        setAppTheme(currentTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // NEW: Initialize Firebase Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        // ... (Authentication check and view initialization remain unchanged) ...

        if (user == null) {
            Toast.makeText(this, "User not logged in. Returning to Login.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Initialize views
        etName = findViewById(R.id.et_edit_name);
        etEmail = findViewById(R.id.et_edit_email);
        etPhone = findViewById(R.id.et_edit_phone);
        Button btnSave = findViewById(R.id.btn_save_profile);

        // Set email (read-only)
        etEmail.setText(user.getEmail());

        // Setup Database Reference: Users/USER_UID
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        // 2. Fetch existing data on startup
        fetchUserProfile();

        // 3. Setup Save Button Listener
        btnSave.setOnClickListener(v -> saveProfileData());
    }

    private void fetchUserProfile() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Populate fields if data exists in Firebase
                    String name = snapshot.child("name").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);

                    if (name != null) etName.setText(name);
                    if (phone != null) etPhone.setText(phone);
                } else {
                    // Initialize with empty strings if no data found
                    etName.setText("");
                    etPhone.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProfileData() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        // Validate mandatory and length fields
        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Name and Phone fields are required.", Toast.LENGTH_LONG).show();
            return;
        }
        if (phone.length() != 11) {
            Toast.makeText(this, "Phone number must be exactly 11 digits.", Toast.LENGTH_LONG).show();
            return;
        }

        // Convert to data map
        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        data.put("name", name);
        data.put("phone", phone);

        // Push data to Firebase Realtime Database
        userRef.updateChildren(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Profile Saved Successfully!", Toast.LENGTH_SHORT).show();

                // NEW: Log custom event to Firebase Analytics
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, user.getUid());
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "profile_data_save");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "user_profile");
                    mFirebaseAnalytics.logEvent("profile_updated", bundle);
                }

                finish(); // Close activity and return to ProfileView
            } else {
                Toast.makeText(this, "Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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