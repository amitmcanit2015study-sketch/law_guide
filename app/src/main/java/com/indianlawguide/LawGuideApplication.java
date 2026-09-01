package com.indianlawguide;

import android.app.Application;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.indianlawguide.constants.AppConstants;
import com.indianlawguide.database.AppDatabase;
import com.indianlawguide.models.DailyLawWorker;
import com.indianlawguide.theme.ThemeHelper;
import com.indianlawguide.utils.DataStoreManager;

import java.util.concurrent.TimeUnit;

public class LawGuideApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 1. Initialize & apply saved Theme
        DataStoreManager prefs = DataStoreManager.getInstance(this);
        ThemeHelper.applyTheme(prefs.getAppTheme());

        // 2. Initialize Room Database & Seed if empty
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            // Trigger check
            db.lawDao().getCount();
        });

        // 3. Schedule Daily Legal Tip Worker via WorkManager (Offline periodic task)
        PeriodicWorkRequest dailyLawRequest = new PeriodicWorkRequest.Builder(
            DailyLawWorker.class,
            24,
            TimeUnit.HOURS
        )
        .addTag(AppConstants.WORK_DAILY_LAW_TAG)
        .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            AppConstants.WORK_DAILY_LAW_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            dailyLawRequest
        );
    }
}
