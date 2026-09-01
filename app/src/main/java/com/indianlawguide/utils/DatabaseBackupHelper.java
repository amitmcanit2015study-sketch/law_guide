package com.indianlawguide.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.indianlawguide.database.AppDatabase;
import com.indianlawguide.database.entities.FavoriteEntity;
import com.indianlawguide.database.entities.NoteEntity;
import com.indianlawguide.models.BackupContainer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DatabaseBackupHelper {

    private static final String TAG = "DatabaseBackupHelper";

    public interface BackupCallback {
        void onSuccess(String filePath);
        void onError(String message);
    }

    public interface RestoreCallback {
        void onSuccess(int restoredFavorites, int restoredNotes);
        void onError(String message);
    }

    public static void exportBackup(Context context, BackupCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(context);
                List<FavoriteEntity> favorites = db.favoriteDao().getAllFavoritesSync();
                List<NoteEntity> notes = db.noteDao().getAllNotesSync();

                BackupContainer container = new BackupContainer(
                    "1.0.0",
                    System.currentTimeMillis(),
                    favorites,
                    notes
                );

                Gson gson = new Gson();
                String json = gson.toJson(container);

                File exportDir = new File(context.getExternalFilesDir(null), "backups");
                if (!exportDir.exists()) {
                    exportDir.mkdirs();
                }

                File backupFile = new File(exportDir, "IndianLawGuide_Backup_" + System.currentTimeMillis() + ".json");
                FileOutputStream fos = new FileOutputStream(backupFile);
                fos.write(json.getBytes(StandardCharsets.UTF_8));
                fos.close();

                callback.onSuccess(backupFile.getAbsolutePath());
            } catch (Exception e) {
                Log.e(TAG, "Error exporting backup", e);
                callback.onError(e.getLocalizedMessage());
            }
        });
    }

    public static void restoreBackup(Context context, File backupFile, RestoreCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                if (!backupFile.exists()) {
                    callback.onError("Backup file does not exist");
                    return;
                }

                FileInputStream fis = new FileInputStream(backupFile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                fis.close();

                Gson gson = new Gson();
                BackupContainer container = gson.fromJson(sb.toString(), BackupContainer.class);
                if (container == null) {
                    callback.onError("Invalid backup file format");
                    return;
                }

                AppDatabase db = AppDatabase.getInstance(context);
                int favCount = 0;
                int noteCount = 0;

                if (container.getFavorites() != null) {
                    for (FavoriteEntity fav : container.getFavorites()) {
                        db.favoriteDao().insert(fav);
                        favCount++;
                    }
                }

                if (container.getNotes() != null) {
                    for (NoteEntity note : container.getNotes()) {
                        db.noteDao().insert(note);
                        noteCount++;
                    }
                }

                callback.onSuccess(favCount, noteCount);
            } catch (Exception e) {
                Log.e(TAG, "Error restoring backup", e);
                callback.onError(e.getLocalizedMessage());
            }
        });
    }
}
