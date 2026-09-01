package com.indianlawguide.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.indianlawguide.database.entities.QuizEntity;
import com.indianlawguide.repository.QuizRepository;

import java.util.List;

public class QuizViewModel extends AndroidViewModel {

    private final QuizRepository quizRepository;
    private final MutableLiveData<String> selectedCategory = new MutableLiveData<>("All");

    public QuizViewModel(@NonNull Application application) {
        super(application);
        quizRepository = new QuizRepository(application);
    }

    public LiveData<List<QuizEntity>> getQuizzes(String category) {
        return quizRepository.getQuizzesByCategory(category);
    }

    public LiveData<List<String>> getQuizCategories() {
        return quizRepository.getQuizCategories();
    }

    public void setCategory(String category) {
        selectedCategory.setValue(category);
    }

    public LiveData<String> getSelectedCategory() {
        return selectedCategory;
    }
}
