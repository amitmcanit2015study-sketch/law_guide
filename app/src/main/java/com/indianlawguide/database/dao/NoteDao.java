package com.indianlawguide.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.indianlawguide.database.entities.NoteEntity;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(NoteEntity note);

    @Update
    void update(NoteEntity note);

    @Delete
    void delete(NoteEntity note);

    @Query("DELETE FROM notes WHERE id = :id")
    void deleteById(long id);

    @Query("SELECT * FROM notes WHERE law_id = :lawId ORDER BY created_at DESC")
    LiveData<List<NoteEntity>> getNotesForLaw(long lawId);

    @Query("SELECT * FROM notes ORDER BY created_at DESC")
    LiveData<List<NoteEntity>> getAllNotes();

    @Query("SELECT * FROM notes ORDER BY created_at DESC")
    List<NoteEntity> getAllNotesSync();

    @Query("SELECT * FROM notes WHERE note LIKE '%' || :query || '%' OR law_title LIKE '%' || :query || '%' ORDER BY created_at DESC")
    LiveData<List<NoteEntity>> searchNotes(String query);

    @Query("DELETE FROM notes")
    void clearAll();
}
