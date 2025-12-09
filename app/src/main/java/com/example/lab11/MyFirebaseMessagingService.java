package com.example.lab11; // Use your package name
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.Toast; // For quick testing

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM_SERVICE";

    /**
     * Called if the FCM registration token is updated.
     */
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);
        // This is where you'd send the token to your server if you had one.
    }

    /**
     * Called when a message is received (when the app is in the foreground).
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Display a Toast for quick foreground testing (Replace this with a real Notification later)
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Notification Body: " + body);

            // Note: Toast is only visible if the app is actively in the foreground!
            Toast.makeText(getApplicationContext(), title + ": " + body, Toast.LENGTH_LONG).show();
        }
    }
}