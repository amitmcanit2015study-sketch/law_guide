package com.indianlawguide.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.indianlawguide.R;
import com.indianlawguide.constants.AppConstants;
import com.indianlawguide.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Handle System Windows Insets (Status bar and Navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
            .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.bottomNavigation, navController);

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                int id = destination.getId();
                if (id == R.id.nav_law_detail || id == R.id.nav_quiz_play) {
                    binding.bottomNavigation.setVisibility(View.GONE);
                } else {
                    binding.bottomNavigation.setVisibility(View.VISIBLE);
                }
            });
        }

        handleIncomingNotificationIntent();
    }

    private void handleIncomingNotificationIntent() {
        if (getIntent() != null && getIntent().hasExtra(AppConstants.ARG_LAW_ID)) {
            long lawId = getIntent().getLongExtra(AppConstants.ARG_LAW_ID, -1L);
            if (lawId > 0 && navController != null) {
                Bundle args = new Bundle();
                args.putLong(AppConstants.ARG_LAW_ID, lawId);
                navController.navigate(R.id.nav_law_detail, args);
            }
        }
    }
}
