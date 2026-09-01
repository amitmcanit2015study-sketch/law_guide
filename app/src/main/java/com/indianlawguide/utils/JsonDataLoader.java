package com.indianlawguide.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.indianlawguide.database.AppDatabase;
import com.indianlawguide.database.entities.EmergencyEntity;
import com.indianlawguide.database.entities.FaqEntity;
import com.indianlawguide.database.entities.LawEntity;
import com.indianlawguide.database.entities.QuizEntity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JsonDataLoader {

    private static final String TAG = "JsonDataLoader";

    public static void seedDatabase(Context context, AppDatabase database) {
        try {
            Gson gson = new Gson();

            // 1. Seed Laws
            if (database.lawDao().getCount() == 0) {
                InputStream isLaws = context.getAssets().open("json/laws_seed.json");
                Type lawListType = new TypeToken<List<LawEntity>>() {}.getType();
                List<LawEntity> laws = gson.fromJson(new InputStreamReader(isLaws, StandardCharsets.UTF_8), lawListType);
                if (laws != null && !laws.isEmpty()) {
                    database.lawDao().insertAll(laws);
                    Log.d(TAG, "Seeded " + laws.size() + " laws into database.");
                }
                isLaws.close();
            }

            // 2. Seed Emergencies
            if (database.emergencyDao().getCount() == 0) {
                InputStream isEmergencies = context.getAssets().open("json/emergencies_seed.json");
                Type emergencyListType = new TypeToken<List<EmergencyEntity>>() {}.getType();
                List<EmergencyEntity> emergencies = gson.fromJson(new InputStreamReader(isEmergencies, StandardCharsets.UTF_8), emergencyListType);
                if (emergencies != null && !emergencies.isEmpty()) {
                    database.emergencyDao().insertAll(emergencies);
                    Log.d(TAG, "Seeded " + emergencies.size() + " emergencies.");
                }
                isEmergencies.close();
            }

            // 3. Seed FAQs
            if (database.faqDao().getCount() == 0) {
                InputStream isFaqs = context.getAssets().open("json/faqs_seed.json");
                Type faqListType = new TypeToken<List<FaqEntity>>() {}.getType();
                List<FaqEntity> faqs = gson.fromJson(new InputStreamReader(isFaqs, StandardCharsets.UTF_8), faqListType);
                if (faqs != null && !faqs.isEmpty()) {
                    database.faqDao().insertAll(faqs);
                    Log.d(TAG, "Seeded " + faqs.size() + " FAQs.");
                }
                isFaqs.close();
            }

            // 4. Seed Quizzes
            if (database.quizDao().getCount() == 0) {
                InputStream isQuizzes = context.getAssets().open("json/quizzes_seed.json");
                Type quizListType = new TypeToken<List<QuizEntity>>() {}.getType();
                List<QuizEntity> quizzes = gson.fromJson(new InputStreamReader(isQuizzes, StandardCharsets.UTF_8), quizListType);
                if (quizzes != null && !quizzes.isEmpty()) {
                    database.quizDao().insertAll(quizzes);
                    Log.d(TAG, "Seeded " + quizzes.size() + " Quizzes.");
                }
                isQuizzes.close();
            }

        } catch (Exception e) {
            Log.e(TAG, "Error seeding database from JSON assets", e);
        }
    }
}
