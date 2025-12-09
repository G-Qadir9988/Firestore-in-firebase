package com.example.lab11;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class AnalyticsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Retrieve and apply the user's selected theme for consistency
        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String currentTheme = sharedPreferences.getString("currentTheme", "Normal");
        setAppTheme(currentTheme);

        super.onCreate(savedInstanceState);
        // Link to the activity_analytics.xml layout file
        setContentView(R.layout.activity_analytics);

        // **TODO:** Add logic here later to fetch data and display charts or analytics metrics.
    }

    // Helper method to apply theme dynamically (copied from Login/Settings)
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