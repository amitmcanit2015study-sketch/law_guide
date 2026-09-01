package com.indianlawguide.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.indianlawguide.database.dao.EmergencyDao;
import com.indianlawguide.database.dao.FaqDao;
import com.indianlawguide.database.dao.FavoriteDao;
import com.indianlawguide.database.dao.HistoryDao;
import com.indianlawguide.database.dao.LawDao;
import com.indianlawguide.database.dao.NoteDao;
import com.indianlawguide.database.dao.QuizDao;
import com.indianlawguide.database.entities.EmergencyEntity;
import com.indianlawguide.database.entities.FaqEntity;
import com.indianlawguide.database.entities.FavoriteEntity;
import com.indianlawguide.database.entities.HistoryEntity;
import com.indianlawguide.database.entities.LawEntity;
import com.indianlawguide.database.entities.LawFtsEntity;
import com.indianlawguide.database.entities.NoteEntity;
import com.indianlawguide.database.entities.QuizEntity;
import com.indianlawguide.utils.JsonDataLoader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
    entities = {
        LawEntity.class,
        LawFtsEntity.class,
        FavoriteEntity.class,
        NoteEntity.class,
        HistoryEntity.class,
        FaqEntity.class,
        EmergencyEntity.class,
        QuizEntity.class
    },
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "indian_law_guide.db";
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract LawDao lawDao();
    public abstract FavoriteDao favoriteDao();
    public abstract NoteDao noteDao();
    public abstract HistoryDao historyDao();
    public abstract FaqDao faqDao();
    public abstract EmergencyDao emergencyDao();
    public abstract QuizDao quizDao();

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        DATABASE_NAME
                    )
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            databaseWriteExecutor.execute(() -> {
                                JsonDataLoader.seedDatabase(context.getApplicationContext(), getInstance(context));
                            });
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
