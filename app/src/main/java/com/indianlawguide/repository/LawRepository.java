package com.indianlawguide.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.indianlawguide.database.AppDatabase;
import com.indianlawguide.database.dao.FavoriteDao;
import com.indianlawguide.database.dao.HistoryDao;
import com.indianlawguide.database.dao.LawDao;
import com.indianlawguide.database.entities.FavoriteEntity;
import com.indianlawguide.database.entities.HistoryEntity;
import com.indianlawguide.database.entities.LawEntity;

import java.util.List;

public class LawRepository {

    private final LawDao lawDao;
    private final FavoriteDao favoriteDao;
    private final HistoryDao historyDao;

    public LawRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        lawDao = db.lawDao();
        favoriteDao = db.favoriteDao();
        historyDao = db.historyDao();
    }

    public LiveData<List<LawEntity>> getAllLaws() {
        return lawDao.getAllLaws();
    }

    public LiveData<List<LawEntity>> getLawsByCategory(String category) {
        return lawDao.getLawsByCategory(category);
    }

    public LiveData<LawEntity> getLawById(long id) {
        return lawDao.getLawById(id);
    }

    public LiveData<LawEntity> getRandomLawLive() {
        return lawDao.getRandomLawLive();
    }

    public LiveData<List<LawEntity>> searchLaws(String query) {
        return lawDao.searchLawsLike(query);
    }

    public LiveData<Boolean> isFavorite(long lawId) {
        return favoriteDao.isFavorite(lawId);
    }

    public void toggleFavorite(long lawId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            if (favoriteDao.isFavoriteSync(lawId)) {
                favoriteDao.deleteByLawId(lawId);
            } else {
                favoriteDao.insert(new FavoriteEntity(lawId, System.currentTimeMillis()));
            }
        });
    }

    public LiveData<List<LawEntity>> getFavoriteLaws() {
        return favoriteDao.getFavoriteLaws();
    }

    public void recordHistory(long lawId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            historyDao.insert(new HistoryEntity(lawId, System.currentTimeMillis()));
            historyDao.trimHistory();
        });
    }

    public LiveData<List<LawEntity>> getRecentlyViewedLaws() {
        return historyDao.getRecentlyViewedLaws();
    }

    public void clearHistory() {
        AppDatabase.databaseWriteExecutor.execute(historyDao::clearAll);
    }

    public void clearFavorites() {
        AppDatabase.databaseWriteExecutor.execute(favoriteDao::clearAll);
    }
}
