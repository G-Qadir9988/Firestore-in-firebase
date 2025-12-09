package com.example.lab11;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        // Load and apply theme here for persistence
        String currentTheme = sharedPreferences.getString("currentTheme", "Normal");
        setAppTheme(currentTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Theme Buttons Logic (linking to the buttons in the XML below)
        findViewById(R.id.btn_theme_dark).setOnClickListener(v -> applyNewTheme("Dark"));
        findViewById(R.id.btn_theme_normal).setOnClickListener(v -> applyNewTheme("Normal"));
        findViewById(R.id.btn_theme_light).setOnClickListener(v -> applyNewTheme("Light"));
    }

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

    private void applyNewTheme(String themeName) {
        // 1. Save the new theme preference
        sharedPreferences.edit().putString("currentTheme", themeName).apply();

        // 2. Restart the activity to apply the new theme
        recreate();
        // You might also want to send a broadcast or use a listener to update other open activities
    }
}