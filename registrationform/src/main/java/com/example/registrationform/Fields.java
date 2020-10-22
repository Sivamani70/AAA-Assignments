package com.example.registrationform;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public interface Fields {
    String NAME = "NAME";
    String ROLL_NUMBER = "ROLL_NUMBER";
    String GENDER = "GENDER";
    String MAIL = "MAIL";
    String PASSWORD = "PASSWORD";
    String PHONE_NUMBER = "PHONE_NUMBER";
    String BRANCH = "BRANCH";
    String YEAR = "YEAR";
    String LANGUAGES = "LANGUAGES";
    String PREFS_KEY = "com.example.registrationform";

    default void message(View view) {
        Snackbar.make(view, "Data Saved", Snackbar.LENGTH_LONG).show();
    }


}
