package com.indianlawguide.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.indianlawguide.database.entities.FaqEntity;

import java.util.List;

@Dao
public interface FaqDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<FaqEntity> faqs);

    @Query("SELECT * FROM faqs ORDER BY id ASC")
    LiveData<List<FaqEntity>> getAllFaqs();

    @Query("SELECT COUNT(*) FROM faqs")
    int getCount();
}
