package com.indianlawguide.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.indianlawguide.database.entities.LawEntity;
import com.indianlawguide.database.entities.NoteEntity;
import com.indianlawguide.repository.LawRepository;
import com.indianlawguide.repository.NotesRepository;

import java.util.List;

public class LawDetailViewModel extends AndroidViewModel {

    private final LawRepository lawRepository;
    private final NotesRepository notesRepository;

    public LawDetailViewModel(@NonNull Application application) {
        super(application);
        lawRepository = new LawRepository(application);
        notesRepository = new NotesRepository(application);
    }

    public LiveData<LawEntity> getLawById(long id) {
        return lawRepository.getLawById(id);
    }

    public LiveData<Boolean> isFavorite(long lawId) {
        return lawRepository.isFavorite(lawId);
    }

    public void toggleFavorite(long lawId) {
        lawRepository.toggleFavorite(lawId);
    }

    public void recordHistory(long lawId) {
        lawRepository.recordHistory(lawId);
    }

    public LiveData<List<NoteEntity>> getNotesForLaw(long lawId) {
        return notesRepository.getNotesForLaw(lawId);
    }

    public void saveNote(long lawId, String lawTitle, String noteText) {
        NoteEntity note = new NoteEntity(lawId, lawTitle, noteText, System.currentTimeMillis());
        notesRepository.insertNote(note);
    }

    public void deleteNote(NoteEntity note) {
        notesRepository.deleteNote(note);
    }
}
