package com.indianlawguide.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.indianlawguide.database.entities.LawEntity;
import com.indianlawguide.repository.LawRepository;
import com.indianlawguide.utils.DataStoreManager;

import java.util.Collections;
import java.util.List;

public class SearchViewModel extends AndroidViewModel {

    private final LawRepository lawRepository;
    private final DataStoreManager prefs;
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final LiveData<List<LawEntity>> searchResults;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        lawRepository = new LawRepository(application);
        prefs = DataStoreManager.getInstance(application);

        searchResults = Transformations.switchMap(searchQuery, query -> {
            if (query == null || query.trim().isEmpty()) {
                MutableLiveData<List<LawEntity>> empty = new MutableLiveData<>();
                empty.setValue(Collections.emptyList());
                return empty;
            }
            return lawRepository.searchLaws(query.trim());
        });
    }

    public void setQuery(String query) {
        searchQuery.setValue(query);
    }

    public LiveData<String> getQuery() {
        return searchQuery;
    }

    public LiveData<List<LawEntity>> getSearchResults() {
        return searchResults;
    }

    public void saveRecentSearch(String query) {
        prefs.saveSearchQuery(query);
    }

    public String getRecentSearches() {
        return prefs.getRecentSearches();
    }

    public void clearRecentSearches() {
        prefs.clearRecentSearches();
    }
}
