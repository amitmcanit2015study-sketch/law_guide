package com.indianlawguide.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.indianlawguide.databinding.ActivityDisclaimerBinding;
import com.indianlawguide.utils.DataStoreManager;

public class DisclaimerActivity extends AppCompatActivity {

    private ActivityDisclaimerBinding binding;
    private DataStoreManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Install Android 12+ Splash Screen
        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        prefs = DataStoreManager.getInstance(this);

        // If disclaimer is already accepted, bypass directly to MainActivity
        if (prefs.isDisclaimerAccepted()) {
            startMainActivity();
            return;
        }

        binding = ActivityDisclaimerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupListeners();
    }

    private void setupListeners() {
        binding.checkboxUnderstand.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.btnContinue.setEnabled(isChecked);
        });

        binding.btnContinue.setOnClickListener(v -> {
            prefs.setDisclaimerAccepted(true);
            startMainActivity();
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
