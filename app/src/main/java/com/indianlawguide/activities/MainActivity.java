package com.indianlawguide.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

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
            setupBottomNavigation();

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                int id = destination.getId();
                if (id == R.id.nav_law_detail || id == R.id.nav_quiz_play) {
                    binding.bottomNavigation.setVisibility(View.GONE);
                } else {
                    binding.bottomNavigation.setVisibility(View.VISIBLE);
                }

                // Sync bottom nav selection if destination matches a tab
                if (id == R.id.nav_home || id == R.id.nav_search || id == R.id.nav_emergency || id == R.id.nav_favorites) {
                    if (binding.bottomNavigation.getSelectedItemId() != id) {
                        binding.bottomNavigation.getMenu().findItem(id).setChecked(true);
                    }
                }
            });
        }

        handleIncomingNotificationIntent();
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (navController == null) return false;

            if (itemId == R.id.nav_home) {
                // Always pop back to home root reliably
                navController.popBackStack(R.id.nav_home, false);
                if (navController.getCurrentDestination() == null || navController.getCurrentDestination().getId() != R.id.nav_home) {
                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.nav_home, true)
                            .setLaunchSingleTop(true)
                            .build();
                    navController.navigate(R.id.nav_home, null, navOptions);
                }
                return true;
            } else if (itemId == R.id.nav_search || itemId == R.id.nav_emergency || itemId == R.id.nav_favorites) {
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.nav_home, false)
                        .setLaunchSingleTop(true)
                        .build();
                navController.navigate(itemId, null, navOptions);
                return true;
            }
            return false;
        });

        binding.bottomNavigation.setOnItemReselectedListener(item -> {
            if (navController == null) return;
            if (item.getItemId() == R.id.nav_home) {
                // Clear any sub-screens opened on top of home
                navController.popBackStack(R.id.nav_home, false);
            }
        });
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
