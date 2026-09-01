package com.indianlawguide.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class DataStoreManager {

    private static final String PREFS_NAME = "indian_law_guide_prefs";
    private static final String KEY_DISCLAIMER_ACCEPTED = "key_disclaimer_accepted";
    private static final String KEY_APP_THEME = "key_app_theme";
    private static final String KEY_TEXT_SIZE_SCALE = "key_text_size_scale";
    private static final String KEY_DAILY_NOTIFICATION = "key_daily_notification";
    private static final String KEY_LAST_DAILY_LAW_ID = "key_last_daily_law_id";
    private static final String KEY_RECENT_SEARCHES = "key_recent_searches";

    private static volatile DataStoreManager INSTANCE;
    private final SharedPreferences prefs;

    private DataStoreManager(Context context) {
        this.prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static DataStoreManager getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (DataStoreManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DataStoreManager(context);
                }
            }
        }
        return INSTANCE;
    }

    public boolean isDisclaimerAccepted() {
        return prefs.getBoolean(KEY_DISCLAIMER_ACCEPTED, false);
    }

    public void setDisclaimerAccepted(boolean accepted) {
        prefs.edit().putBoolean(KEY_DISCLAIMER_ACCEPTED, accepted).apply();
    }

    public String getAppTheme() {
        return prefs.getString(KEY_APP_THEME, "system");
    }

    public void setAppTheme(String theme) {
        prefs.edit().putString(KEY_APP_THEME, theme).apply();
    }

    public float getTextSizeScale() {
        return prefs.getFloat(KEY_TEXT_SIZE_SCALE, 1.0f);
    }

    public void setTextSizeScale(float scale) {
        prefs.edit().putFloat(KEY_TEXT_SIZE_SCALE, scale).apply();
    }

    public boolean isDailyNotificationEnabled() {
        return prefs.getBoolean(KEY_DAILY_NOTIFICATION, true);
    }

    public void setDailyNotificationEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_DAILY_NOTIFICATION, enabled).apply();
    }

    public long getLastDailyLawId() {
        return prefs.getLong(KEY_LAST_DAILY_LAW_ID, 1L);
    }

    public void setLastDailyLawId(long lawId) {
        prefs.edit().putLong(KEY_LAST_DAILY_LAW_ID, lawId).apply();
    }

    public String getRecentSearches() {
        return prefs.getString(KEY_RECENT_SEARCHES, "");
    }

    public void saveSearchQuery(String query) {
        if (query == null || query.trim().isEmpty()) return;
        String current = getRecentSearches();
        String trimmed = query.trim();
        String[] items = current.split(",");
        StringBuilder sb = new StringBuilder(trimmed);
        int count = 1;
        for (String item : items) {
            if (!item.isEmpty() && !item.equalsIgnoreCase(trimmed) && count < 6) {
                sb.append(",").append(item);
                count++;
            }
        }
        prefs.edit().putString(KEY_RECENT_SEARCHES, sb.toString()).apply();
    }

    public void clearRecentSearches() {
        prefs.edit().remove(KEY_RECENT_SEARCHES).apply();
    }

    public void clearAllPreferences() {
        prefs.edit().clear().apply();
    }
}
