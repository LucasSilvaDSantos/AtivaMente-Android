package com.example.ativamente;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class AtivamenteApp extends Application {

    public static final String PREFS_NAME = "theme_prefs";
    public static final String KEY_THEME = "theme_mode";

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int currentNightMode = sharedPreferences.getInt(KEY_THEME, AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(currentNightMode);
    }
}
