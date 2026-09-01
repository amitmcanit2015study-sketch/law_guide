package com.indianlawguide.models;

import com.indianlawguide.database.entities.FavoriteEntity;
import com.indianlawguide.database.entities.NoteEntity;

import java.io.Serializable;
import java.util.List;

public class BackupContainer implements Serializable {

    private String appVersion;
    private long exportTimestamp;
    private List<FavoriteEntity> favorites;
    private List<NoteEntity> notes;

    public BackupContainer() {
    }

    public BackupContainer(String appVersion, long exportTimestamp, List<FavoriteEntity> favorites, List<NoteEntity> notes) {
        this.appVersion = appVersion;
        this.exportTimestamp = exportTimestamp;
        this.favorites = favorites;
        this.notes = notes;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public long getExportTimestamp() {
        return exportTimestamp;
    }

    public void setExportTimestamp(long exportTimestamp) {
        this.exportTimestamp = exportTimestamp;
    }

    public List<FavoriteEntity> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<FavoriteEntity> favorites) {
        this.favorites = favorites;
    }

    public List<NoteEntity> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteEntity> notes) {
        this.notes = notes;
    }
}
