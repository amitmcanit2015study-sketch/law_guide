package com.indianlawguide.models;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.indianlawguide.R;
import com.indianlawguide.activities.MainActivity;
import com.indianlawguide.constants.AppConstants;
import com.indianlawguide.database.AppDatabase;
import com.indianlawguide.database.entities.LawEntity;
import com.indianlawguide.utils.DataStoreManager;

public class DailyLawWorker extends Worker {

    private static final String TAG = "DailyLawWorker";

    public DailyLawWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        DataStoreManager prefs = DataStoreManager.getInstance(context);

        if (!prefs.isDailyNotificationEnabled()) {
            return Result.success();
        }

        try {
            AppDatabase db = AppDatabase.getInstance(context);
            LawEntity law = db.lawDao().getRandomLawSync();

            if (law != null) {
                prefs.setLastDailyLawId(law.getId());
                showNotification(context, law);
            }
            return Result.success();
        } catch (Exception e) {
            Log.e(TAG, "Error executing DailyLawWorker", e);
            return Result.retry();
        }
    }

    private void showNotification(Context context, LawEntity law) {
        createNotificationChannel(context);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(AppConstants.ARG_LAW_ID, law.getId());

        PendingIntent pendingIntent = PendingIntent.getActivity(
            context,
            1001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, AppConstants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_ashoka_pillar)
            .setContentTitle("Daily Legal Right: " + law.getTitle())
            .setContentText(law.getSummary())
            .setStyle(new NotificationCompat.BigTextStyle().bigText(law.getSummary()))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);

        try {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(2001, builder.build());
        } catch (SecurityException se) {
            Log.w(TAG, "Notification permission not granted", se);
        }
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                AppConstants.NOTIFICATION_CHANNEL_ID,
                AppConstants.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Shows one offline daily Indian legal right or safety tip.");
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
