package com.example.firebaseauth;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity :";
    final String PREFS_KEY = "com.example.registrationform";
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            Context formContext = createPackageContext(PREFS_KEY, CONTEXT_IGNORE_SECURITY);
            preferences = formContext.getSharedPreferences(PREFS_KEY, Context.MODE_MULTI_PROCESS);
            Log.e(TAG, "onCreate: Name :\t" + preferences.getString("NAME", ""));
            Log.e(TAG, "onCreate: RollNumber :\t" + preferences.getString("ROLL_NUMBER", ""));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "onCreate: Error while Accessing" + e.getMessage());
        }
    }
}