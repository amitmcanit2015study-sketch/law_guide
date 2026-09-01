package com.indianlawguide.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.indianlawguide.database.AppDatabase;
import com.indianlawguide.database.dao.NoteDao;
import com.indianlawguide.database.entities.NoteEntity;

import java.util.List;

public class NotesRepository {

    private final NoteDao noteDao;

    public NotesRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        noteDao = db.noteDao();
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return noteDao.getAllNotes();
    }

    public LiveData<List<NoteEntity>> getNotesForLaw(long lawId) {
        return noteDao.getNotesForLaw(lawId);
    }

    public LiveData<List<NoteEntity>> searchNotes(String query) {
        return noteDao.searchNotes(query);
    }

    public void insertNote(NoteEntity note) {
        AppDatabase.databaseWriteExecutor.execute(() -> noteDao.insert(note));
    }

    public void updateNote(NoteEntity note) {
        AppDatabase.databaseWriteExecutor.execute(() -> noteDao.update(note));
    }

    public void deleteNote(NoteEntity note) {
        AppDatabase.databaseWriteExecutor.execute(() -> noteDao.delete(note));
    }

    public void deleteNoteById(long noteId) {
        AppDatabase.databaseWriteExecutor.execute(() -> noteDao.deleteById(noteId));
    }

    public void clearAllNotes() {
        AppDatabase.databaseWriteExecutor.execute(noteDao::clearAll);
    }
}
