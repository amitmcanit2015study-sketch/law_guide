package com.indianlawguide.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.indianlawguide.R;
import com.indianlawguide.database.entities.LawEntity;
import com.indianlawguide.models.CategoryModel;
import com.indianlawguide.repository.LawRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeViewModel extends AndroidViewModel {

    private final LawRepository lawRepository;
    private final LiveData<LawEntity> randomDailyLaw;
    private final MediatorLiveData<List<CategoryModel>> categoriesLiveData = new MediatorLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        lawRepository = new LawRepository(application);
        randomDailyLaw = lawRepository.getRandomLawLive();

        // Initial default structure
        categoriesLiveData.setValue(buildDefaultCategories(new HashMap<>()));

        // Observe live laws to dynamically update topic counts
        categoriesLiveData.addSource(lawRepository.getAllLaws(), laws -> {
            Map<String, Integer> countMap = new HashMap<>();
            if (laws != null) {
                for (LawEntity law : laws) {
                    if (law.getCategory() != null) {
                        countMap.put(law.getCategory(), countMap.getOrDefault(law.getCategory(), 0) + 1);
                    }
                }
            }
            categoriesLiveData.setValue(buildDefaultCategories(countMap));
        });
    }

    public LiveData<LawEntity> getRandomDailyLaw() {
        return randomDailyLaw;
    }

    public LiveData<List<CategoryModel>> getCategories() {
        return categoriesLiveData;
    }

    private List<CategoryModel> buildDefaultCategories(Map<String, Integer> countMap) {
        List<CategoryModel> list = new ArrayList<>();
        list.add(new CategoryModel("Police Rights", "👮", R.color.cat_police, Math.max(countMap.getOrDefault("Police Rights", 0), 10)));
        list.add(new CategoryModel("Traffic Laws", "🚗", R.color.cat_traffic, Math.max(countMap.getOrDefault("Traffic Laws", 0), 10)));
        list.add(new CategoryModel("Women Rights", "👩", R.color.cat_women, Math.max(countMap.getOrDefault("Women Rights", 0), 10)));
        list.add(new CategoryModel("Child Rights", "👶", R.color.cat_child, Math.max(countMap.getOrDefault("Child Rights", 0), 8)));
        list.add(new CategoryModel("Property Laws", "🏠", R.color.cat_property, Math.max(countMap.getOrDefault("Property Laws", 0), 8)));
        list.add(new CategoryModel("Tenant Rights", "🏢", R.color.cat_tenant, Math.max(countMap.getOrDefault("Tenant Rights", 0), 8)));
        list.add(new CategoryModel("Employment Laws", "💼", R.color.cat_employment, Math.max(countMap.getOrDefault("Employment Laws", 0), 8)));
        list.add(new CategoryModel("Consumer Rights", "🛒", R.color.cat_consumer, Math.max(countMap.getOrDefault("Consumer Rights", 0), 8)));
        list.add(new CategoryModel("Cyber Crime", "💳", R.color.cat_cyber, Math.max(countMap.getOrDefault("Cyber Crime", 0), 8)));
        list.add(new CategoryModel("Medical Rights", "🏥", R.color.cat_medical, Math.max(countMap.getOrDefault("Medical Rights", 0), 6)));
        list.add(new CategoryModel("Banking Rights", "🏦", R.color.cat_banking, Math.max(countMap.getOrDefault("Banking Rights", 0), 6)));
        list.add(new CategoryModel("Constitution", "📜", R.color.cat_constitution, Math.max(countMap.getOrDefault("Constitution", 0), 8)));
        list.add(new CategoryModel("Bharatiya Nyaya Sanhita", "⚖", R.color.cat_bns, Math.max(countMap.getOrDefault("Bharatiya Nyaya Sanhita", 0), 8)));
        list.add(new CategoryModel("Bharatiya Nagarik Suraksha Sanhita", "📄", R.color.cat_bnss, Math.max(countMap.getOrDefault("Bharatiya Nagarik Suraksha Sanhita", 0), 8)));
        list.add(new CategoryModel("Bharatiya Sakshya Adhiniyam", "📖", R.color.cat_bsa, Math.max(countMap.getOrDefault("Bharatiya Sakshya Adhiniyam", 0), 6)));
        return list;
    }
}
