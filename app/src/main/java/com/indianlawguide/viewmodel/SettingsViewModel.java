package com.indianlawguide.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.indianlawguide.repository.LawRepository;
import com.indianlawguide.repository.NotesRepository;
import com.indianlawguide.theme.ThemeHelper;
import com.indianlawguide.utils.DataStoreManager;

public class SettingsViewModel extends AndroidViewModel {

    private final DataStoreManager prefs;
    private final LawRepository lawRepository;
    private final NotesRepository notesRepository;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        prefs = DataStoreManager.getInstance(application);
        lawRepository = new LawRepository(application);
        notesRepository = new NotesRepository(application);
    }

    public String getTheme() {
        return prefs.getAppTheme();
    }

    public void setTheme(String theme) {
        prefs.setAppTheme(theme);
        ThemeHelper.applyTheme(theme);
    }

    public float getTextSizeScale() {
        return prefs.getTextSizeScale();
    }

    public void setTextSizeScale(float scale) {
        prefs.setTextSizeScale(scale);
    }

    public boolean isDailyNotificationEnabled() {
        return prefs.isDailyNotificationEnabled();
    }

    public void setDailyNotificationEnabled(boolean enabled) {
        prefs.setDailyNotificationEnabled(enabled);
    }

    public void clearFavorites() {
        lawRepository.clearFavorites();
    }

    public void clearHistory() {
        lawRepository.clearHistory();
    }

    public void clearNotes() {
        notesRepository.clearAllNotes();
    }

    public void resetAllData() {
        lawRepository.clearFavorites();
        lawRepository.clearHistory();
        notesRepository.clearAllNotes();
        prefs.clearRecentSearches();
    }
}
