package com.indianlawguide.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.indianlawguide.R;
import com.indianlawguide.database.entities.LawEntity;
import com.indianlawguide.models.CategoryModel;
import com.indianlawguide.repository.LawRepository;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final LawRepository lawRepository;
    private final LiveData<LawEntity> randomDailyLaw;
    private final MutableLiveData<List<CategoryModel>> categoriesLiveData = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        lawRepository = new LawRepository(application);
        randomDailyLaw = lawRepository.getRandomLawLive();
        loadCategories();
    }

    public LiveData<LawEntity> getRandomDailyLaw() {
        return randomDailyLaw;
    }

    public LiveData<List<CategoryModel>> getCategories() {
        return categoriesLiveData;
    }

    private void loadCategories() {
        List<CategoryModel> list = new ArrayList<>();
        list.add(new CategoryModel("Police Rights", "👮", R.color.cat_police, 8));
        list.add(new CategoryModel("Traffic Laws", "🚗", R.color.cat_traffic, 8));
        list.add(new CategoryModel("Women Rights", "👩", R.color.cat_women, 7));
        list.add(new CategoryModel("Child Rights", "👶", R.color.cat_child, 5));
        list.add(new CategoryModel("Property Laws", "🏠", R.color.cat_property, 4));
        list.add(new CategoryModel("Tenant Rights", "🏢", R.color.cat_tenant, 4));
        list.add(new CategoryModel("Employment Laws", "💼", R.color.cat_employment, 5));
        list.add(new CategoryModel("Consumer Rights", "🛒", R.color.cat_consumer, 5));
        list.add(new CategoryModel("Cyber Crime", "💳", R.color.cat_cyber, 6));
        list.add(new CategoryModel("Medical Rights", "🏥", R.color.cat_medical, 4));
        list.add(new CategoryModel("Banking Rights", "🏦", R.color.cat_banking, 4));
        list.add(new CategoryModel("Constitution", "📜", R.color.cat_constitution, 6));
        list.add(new CategoryModel("Bharatiya Nyaya Sanhita", "⚖", R.color.cat_bns, 5));
        list.add(new CategoryModel("Bharatiya Nagarik Suraksha Sanhita", "📄", R.color.cat_bnss, 5));
        list.add(new CategoryModel("Bharatiya Sakshya Adhiniyam", "📖", R.color.cat_bsa, 4));

        categoriesLiveData.setValue(list);
    }
}
