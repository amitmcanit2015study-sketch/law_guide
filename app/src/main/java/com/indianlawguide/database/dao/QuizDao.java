package com.indianlawguide.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.indianlawguide.database.entities.QuizEntity;

import java.util.List;

@Dao
public interface QuizDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<QuizEntity> quizzes);

    @Query("SELECT * FROM quizzes ORDER BY RANDOM()")
    LiveData<List<QuizEntity>> getAllQuizzesRandom();

    @Query("SELECT * FROM quizzes WHERE category = :category ORDER BY RANDOM()")
    LiveData<List<QuizEntity>> getQuizzesByCategory(String category);

    @Query("SELECT DISTINCT category FROM quizzes ORDER BY category ASC")
    LiveData<List<String>> getQuizCategories();

    @Query("SELECT COUNT(*) FROM quizzes")
    int getCount();
}
