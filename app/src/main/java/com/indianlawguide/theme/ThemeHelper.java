package com.indianlawguide.theme;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeHelper {

    public static void applyTheme(String themeMode) {
        if ("light".equalsIgnoreCase(themeMode)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if ("dark".equalsIgnoreCase(themeMode)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }
}
