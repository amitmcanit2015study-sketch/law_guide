package com.indianlawguide.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.indianlawguide.database.entities.HistoryEntity;
import com.indianlawguide.database.entities.LawEntity;

import java.util.List;

@Dao
public interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HistoryEntity history);

    @Query("SELECT laws.* FROM laws INNER JOIN history ON laws.id = history.law_id ORDER BY history.viewed_at DESC LIMIT 100")
    LiveData<List<LawEntity>> getRecentlyViewedLaws();

    @Query("DELETE FROM history WHERE id NOT IN (SELECT id FROM history ORDER BY viewed_at DESC LIMIT 100)")
    void trimHistory();

    @Query("DELETE FROM history")
    void clearAll();
}
