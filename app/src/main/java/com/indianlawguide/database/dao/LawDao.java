package com.indianlawguide.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.indianlawguide.database.entities.LawEntity;

import java.util.List;

@Dao
public interface LawDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LawEntity> laws);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LawEntity law);

    @Query("SELECT * FROM laws ORDER BY id ASC")
    LiveData<List<LawEntity>> getAllLaws();

    @Query("SELECT * FROM laws ORDER BY id ASC")
    List<LawEntity> getAllLawsSync();

    @Query("SELECT * FROM laws WHERE category = :category ORDER BY id ASC")
    LiveData<List<LawEntity>> getLawsByCategory(String category);

    @Query("SELECT * FROM laws WHERE id = :id LIMIT 1")
    LiveData<LawEntity> getLawById(long id);

    @Query("SELECT * FROM laws WHERE id = :id LIMIT 1")
    LawEntity getLawByIdSync(long id);

    @Query("SELECT * FROM laws ORDER BY RANDOM() LIMIT 1")
    LawEntity getRandomLawSync();

    @Query("SELECT * FROM laws ORDER BY RANDOM() LIMIT 1")
    LiveData<LawEntity> getRandomLawLive();

    @Query("SELECT DISTINCT category FROM laws ORDER BY category ASC")
    LiveData<List<String>> getAllCategories();

    @Query("SELECT COUNT(*) FROM laws WHERE category = :category")
    int getCategoryCount(String category);

    @Query("SELECT COUNT(*) FROM laws")
    int getCount();

    // Full-Text Search via FTS4 Virtual Table
    @Query("SELECT laws.* FROM laws JOIN laws_fts ON (laws.id = laws_fts.rowid) WHERE laws_fts MATCH :query")
    LiveData<List<LawEntity>> searchLawsFts(String query);

    // Fallback substring query across multiple columns
    @Query("SELECT * FROM laws WHERE title LIKE '%' || :query || '%' OR keywords LIKE '%' || :query || '%' OR section LIKE '%' || :query || '%' OR law_name LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' OR punishment LIKE '%' || :query || '%'")
    LiveData<List<LawEntity>> searchLawsLike(String query);
}
