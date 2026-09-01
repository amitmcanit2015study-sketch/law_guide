package com.indianlawguide.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.indianlawguide.database.entities.NoteEntity;
import com.indianlawguide.repository.NotesRepository;

import java.util.List;

public class NotesViewModel extends AndroidViewModel {

    private final NotesRepository notesRepository;
    private final LiveData<List<NoteEntity>> allNotes;

    public NotesViewModel(@NonNull Application application) {
        super(application);
        notesRepository = new NotesRepository(application);
        allNotes = notesRepository.getAllNotes();
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<NoteEntity>> searchNotes(String query) {
        return notesRepository.searchNotes(query);
    }

    public void insertNote(NoteEntity note) {
        notesRepository.insertNote(note);
    }

    public void updateNote(NoteEntity note) {
        notesRepository.updateNote(note);
    }

    public void deleteNote(NoteEntity note) {
        notesRepository.deleteNote(note);
    }

    public void clearAllNotes() {
        notesRepository.clearAllNotes();
    }
}
