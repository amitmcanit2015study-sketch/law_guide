package com.indianlawguide.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.indianlawguide.R;
import com.indianlawguide.databinding.FragmentSettingsBinding;
import com.indianlawguide.utils.DatabaseBackupHelper;
import com.indianlawguide.viewmodel.SettingsViewModel;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private SettingsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        setupThemeSelection();
        setupNotificationsSwitch();
        setupBackupAndReset();

        binding.btnOpenAbout.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_settings_to_about);
        });
    }

    private void setupThemeSelection() {
        String currentTheme = viewModel.getTheme();
        if ("light".equalsIgnoreCase(currentTheme)) {
            binding.rbThemeLight.setChecked(true);
        } else if ("dark".equalsIgnoreCase(currentTheme)) {
            binding.rbThemeDark.setChecked(true);
        } else {
            binding.rbThemeSystem.setChecked(true);
        }

        binding.rgTheme.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbThemeLight) {
                viewModel.setTheme("light");
            } else if (checkedId == R.id.rbThemeDark) {
                viewModel.setTheme("dark");
            } else {
                viewModel.setTheme("system");
            }
        });
    }

    private void setupNotificationsSwitch() {
        binding.switchDailyNotification.setChecked(viewModel.isDailyNotificationEnabled());
        binding.switchDailyNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.setDailyNotificationEnabled(isChecked);
        });
    }

    private void setupBackupAndReset() {
        binding.btnExportBackup.setOnClickListener(v -> {
            DatabaseBackupHelper.exportBackup(requireContext(), new DatabaseBackupHelper.BackupCallback() {
                @Override
                public void onSuccess(String filePath) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(requireContext(), "Backup exported to: " + filePath, Toast.LENGTH_LONG).show();
                        });
                    }
                }

                @Override
                public void onError(String message) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(requireContext(), "Backup error: " + message, Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            });
        });

        binding.btnClearStorage.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                .setTitle("Reset App Storage")
                .setMessage("Are you sure you want to clear all your saved bookmarks, personal notes, and recent searches?")
                .setPositiveButton("Reset", (dialog, which) -> {
                    viewModel.resetAllData();
                    Toast.makeText(requireContext(), "Personal data cleared successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
