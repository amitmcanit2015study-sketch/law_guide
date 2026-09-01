package com.indianlawguide.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.indianlawguide.database.entities.FavoriteEntity;
import com.indianlawguide.database.entities.LawEntity;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteEntity favorite);

    @Query("DELETE FROM favorites WHERE law_id = :lawId")
    void deleteByLawId(long lawId);

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE law_id = :lawId LIMIT 1)")
    LiveData<Boolean> isFavorite(long lawId);

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE law_id = :lawId LIMIT 1)")
    boolean isFavoriteSync(long lawId);

    @Query("SELECT laws.* FROM laws INNER JOIN favorites ON laws.id = favorites.law_id ORDER BY favorites.created_at DESC")
    LiveData<List<LawEntity>> getFavoriteLaws();

    @Query("SELECT * FROM favorites ORDER BY created_at DESC")
    List<FavoriteEntity> getAllFavoritesSync();

    @Query("DELETE FROM favorites")
    void clearAll();
}
