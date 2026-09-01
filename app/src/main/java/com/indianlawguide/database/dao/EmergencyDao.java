package com.indianlawguide.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.indianlawguide.database.entities.EmergencyEntity;

import java.util.List;

@Dao
public interface EmergencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<EmergencyEntity> emergencies);

    @Query("SELECT * FROM emergencies ORDER BY id ASC")
    LiveData<List<EmergencyEntity>> getAllEmergencies();

    @Query("SELECT COUNT(*) FROM emergencies")
    int getCount();
}
