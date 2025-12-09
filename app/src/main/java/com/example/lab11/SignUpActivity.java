package com.example.lab11; // Use your package name

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View; // ADD THIS IMPORT
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply theme as done in LoginActivity (for consistency)
        setTheme(R.style.Theme_Normal); // Assuming you'd get the current theme preference here too
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        EditText etName = findViewById(R.id.et_name);
        EditText etEmail = findViewById(R.id.et_email_signup);
        EditText etContact = findViewById(R.id.et_contact);
        EditText etPassword = findViewById(R.id.et_password_signup);
        EditText etConfirmPassword = findViewById(R.id.et_confirm_password);
        Button btnSignUp = findViewById(R.id.btn_signup);

        btnSignUp.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String contact = etContact.getText().toString().trim();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            if (!validateInput(name, email, contact, password, confirmPassword)) {
                return;
            }

            // Firebase Registration
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Registration successful!", Toast.LENGTH_LONG).show();
                            // Go back to Login Page
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Registration failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    /**
     * Handles the click event from the "Already have an account? Log In" link.
     * This method is called by the android:onClick attribute in activity_signup.xml.
     */
    public void onLoginLinkClick(View view) {
        // Finishes the current SignUpActivity, returning the user to the LoginActivity
        // which started it.
        finish();
    }

    private boolean validateInput(String name, String email, String contact, String password, String confirmPassword) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(contact) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Contact Validation (fixed 11 numbers)
        if (contact.length() != 11 || !TextUtils.isDigitsOnly(contact)) {
            Toast.makeText(this, "Contact must be exactly 11 numbers.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Password Validation (fixed 6 characters)
        if (password.length() != 6) {
            Toast.makeText(this, "Password must be exactly 6 characters long.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Confirm Password Validation
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password and Confirm Password do not match.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Basic Email validation (Firebase will handle complex email validation, this is a quick check)
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email address.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}