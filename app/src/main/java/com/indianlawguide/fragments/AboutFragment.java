package com.indianlawguide.fragments;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.indianlawguide.R;
import com.indianlawguide.databinding.FragmentAboutBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.toolbarAbout.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        // 1. Share APK / App info
        binding.btnShareApp.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            String shareMessage = "⚖ *Indian Law Pocket Guide*\n\n"
                + "Know your legal rights, police guidelines, traffic laws, and cyber safety—100% offline with zero ads!\n\n"
                + "Developed by Rooys Soft Tech";
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share Indian Law Pocket Guide"));
        });

        // 2. Download APK to Device Downloads
        binding.btnDownloadApk.setOnClickListener(v -> downloadApkToDevice());

        // 3. View Disclaimer Dialog
        binding.btnViewDisclaimer.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                .setTitle(R.string.disclaimer_title)
                .setMessage(
                    getString(R.string.disclaimer_body_1) + "\n\n"
                    + getString(R.string.disclaimer_body_2) + "\n\n"
                    + getString(R.string.disclaimer_body_3) + "\n\n"
                    + getString(R.string.app_data_sources)
                )
                .setPositiveButton("Understood", null)
                .show();
        });

        // 4. Send Feedback via Email
        View.OnClickListener emailListener = v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:rooyssofttech2020@gmail.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback: Indian Law Pocket Guide");
            try {
                startActivity(Intent.createChooser(emailIntent, "Send Feedback"));
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Email app not found. Please email rooyssofttech2020@gmail.com", Toast.LENGTH_SHORT).show();
            }
        };

        binding.btnSendFeedback.setOnClickListener(emailListener);
        binding.tvEmailAddress.setOnClickListener(emailListener);
    }

    private void downloadApkToDevice() {
        try {
            Context context = requireContext();
            String sourceDir = context.getApplicationInfo().sourceDir;
            File srcApk = new File(sourceDir);

            if (!srcApk.exists()) {
                Toast.makeText(context, "APK source not accessible", Toast.LENGTH_SHORT).show();
                return;
            }

            String apkFileName = "IndianLawPocketGuide_v1.0.1.apk";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Downloads.DISPLAY_NAME, apkFileName);
                values.put(MediaStore.Downloads.MIME_TYPE, "application/vnd.android.package-archive");
                values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                Uri uri = context.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                if (uri != null) {
                    try (InputStream in = new FileInputStream(srcApk);
                         OutputStream out = context.getContentResolver().openOutputStream(uri)) {
                        byte[] buffer = new byte[8192];
                        int read;
                        while ((read = in.read(buffer)) != -1) {
                            out.write(buffer, 0, read);
                        }
                    }
                    Toast.makeText(context, "APK downloaded to Downloads/" + apkFileName, Toast.LENGTH_LONG).show();
                }
            } else {
                File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                if (!downloadDir.exists()) {
                    downloadDir.mkdirs();
                }
                File destFile = new File(downloadDir, apkFileName);
                try (InputStream in = new FileInputStream(srcApk);
                     OutputStream out = new FileOutputStream(destFile)) {
                    byte[] buffer = new byte[8192];
                    int read;
                    while ((read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                }
                Toast.makeText(context, "APK downloaded to Downloads/" + apkFileName, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Download error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
