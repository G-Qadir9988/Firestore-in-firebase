package com.example.lab11;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class StudentDataActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText etId, etName, etSection, etDegree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply theme (Copy setAppTheme method from DashboardActivity)
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String currentTheme = sharedPreferences.getString("currentTheme", "Normal");
        setAppTheme(currentTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_data);

        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();

        // Initialize views
        etId = findViewById(R.id.et_student_id);
        etName = findViewById(R.id.et_student_name);
        etSection = findViewById(R.id.et_student_section);
        etDegree = findViewById(R.id.et_student_degree);
        Button btnSave = findViewById(R.id.btn_save_student_data);

        // Setup Save Button Listener
        btnSave.setOnClickListener(v -> saveStudentData());
    }

    private void saveStudentData() {
        String studentId = etId.getText().toString().trim();
        String studentName = etName.getText().toString().trim();
        String studentSection = etSection.getText().toString().trim();
        String studentDegree = etDegree.getText().toString().trim();

        // Validation
        if (studentId.isEmpty() || studentName.isEmpty() || studentSection.isEmpty() || studentDegree.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_LONG).show();
            return;
        }

        // Create a Map to hold the data (Firestore Document)
        Map<String, Object> student = new HashMap<>();
        student.put("student_id", studentId);
        student.put("student_name", studentName);
        student.put("section", studentSection);
        student.put("degree", studentDegree);

        // Save data to Firestore: Collection("Students") -> Document(studentId)
        db.collection("Students")
                .document(studentId) // Use Student ID as the unique document key
                .set(student) // Use .set() to create or overwrite the document
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(StudentDataActivity.this, "Student Data Saved to Firestore!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(StudentDataActivity.this, "Error saving data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    // You MUST include the setAppTheme method here! (Copied from DashboardActivity.java)
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