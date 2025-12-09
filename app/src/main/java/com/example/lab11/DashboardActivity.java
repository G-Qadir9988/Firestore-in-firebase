package com.example.lab11;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.activity.OnBackPressedCallback; // CRITICAL IMPORT for modern back button handling
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // 1. Theme Application
        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String currentTheme = sharedPreferences.getString("currentTheme", "Normal");
        setAppTheme(currentTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();

        // 1b. Sign Out Button Logic
        Button signOutButton = findViewById(R.id.btn_sign_out);
        signOutButton.setOnClickListener(v -> {
            mAuth.signOut();

            // Navigate to LoginActivity and clear the activity stack
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // =======================================================
        // 1c. MODERN BACK BUTTON CONTROL FIX (OnBackPressedCallback)
        // Prevents going back to the Login screen.
        // =======================================================
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Show a toast message to guide the user to the Sign Out button
                Toast.makeText(DashboardActivity.this,
                        "Please use the 'Sign Out' button to end your session.",
                        Toast.LENGTH_SHORT).show();

                // Exits the entire app structure safely.
                finishAffinity();
            }
        });
        // =======================================================


        // 2. Prepare Dashboard Data using the DashboardItem Model
        List<DashboardItem> dashboardItems = new ArrayList<>();

        // Using the actual resource IDs
        dashboardItems.add(new DashboardItem("Profile View", R.drawable.ic_profile, ProfileActivity.class));
        dashboardItems.add(new DashboardItem("Settings", R.drawable.ic_settings, SettingsActivity.class));
        dashboardItems.add(new DashboardItem("Alerts", R.drawable.ic_alerts, AlertsActivity.class));
        dashboardItems.add(new DashboardItem("Analytics", R.drawable.ic_analytics, AnalyticsActivity.class));

        dashboardItems.add(new DashboardItem("Student Data (Firestore)", R.drawable.ic_analytics, StudentDataActivity.class));
        // 3. Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rv_dashboard_options);

        // Use a 2-column grid layout
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // 4. Attach Adapter
        DashboardAdapter adapter = new DashboardAdapter(this, dashboardItems);
        recyclerView.setAdapter(adapter);
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