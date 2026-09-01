package com.indianlawguide.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.indianlawguide.database.AppDatabase;
import com.indianlawguide.database.dao.QuizDao;
import com.indianlawguide.database.entities.QuizEntity;

import java.util.List;

public class QuizRepository {

    private final QuizDao quizDao;

    public QuizRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        quizDao = db.quizDao();
    }

    public LiveData<List<QuizEntity>> getAllQuizzes() {
        return quizDao.getAllQuizzesRandom();
    }

    public LiveData<List<QuizEntity>> getQuizzesByCategory(String category) {
        if ("All".equalsIgnoreCase(category) || category == null) {
            return quizDao.getAllQuizzesRandom();
        }
        return quizDao.getQuizzesByCategory(category);
    }

    public LiveData<List<String>> getQuizCategories() {
        return quizDao.getQuizCategories();
    }
}
