package com.indianlawguide.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.indianlawguide.database.AppDatabase;
import com.indianlawguide.database.dao.EmergencyDao;
import com.indianlawguide.database.dao.FaqDao;
import com.indianlawguide.database.entities.EmergencyEntity;
import com.indianlawguide.database.entities.FaqEntity;

import java.util.List;

public class EmergencyRepository {

    private final EmergencyDao emergencyDao;
    private final FaqDao faqDao;

    public EmergencyRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        emergencyDao = db.emergencyDao();
        faqDao = db.faqDao();
    }

    public LiveData<List<EmergencyEntity>> getAllEmergencies() {
        return emergencyDao.getAllEmergencies();
    }

    public LiveData<List<FaqEntity>> getAllFaqs() {
        return faqDao.getAllFaqs();
    }
}
