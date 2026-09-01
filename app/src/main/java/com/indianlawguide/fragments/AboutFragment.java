package com.indianlawguide.fragments;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.indianlawguide.R;
import com.indianlawguide.databinding.FragmentAboutBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executors;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;

    private static final String APP_ABOUT_TEXT = "Indian Law Guide - Know Your Rights & Legal References\n\n"
            + "Indian Law Pocket Guide is a free, ad-free legal awareness application designed for a fast, simple, and seamless experience. Easily explore, search, and understand statutory rights, citizen protections, and legal remedies with a clean interface, smooth performance, and privacy at its core.\n\n"
            + "• Developed by: Amit Bharat\n"
            + "• Company: Rooys Soft Tech\n"
            + "• Contact: rooyssofttech2020@gmail.com\n"
            + "• Version: 1.0.1\n\n"
            + "Install the attached APK to get started!";

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

        // Language Switcher Toggle Group
        boolean isHindi = com.indianlawguide.utils.LocaleHelper.isHindi(requireContext());
        binding.toggleLanguageGroup.check(isHindi ? R.id.btnLangHindi : R.id.btnLangEnglish);

        binding.toggleLanguageGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked && getActivity() != null) {
                String targetLang = (checkedId == R.id.btnLangHindi) ? "hi" : "en";
                if (!targetLang.equals(com.indianlawguide.utils.LocaleHelper.getLanguage(requireContext()))) {
                    com.indianlawguide.utils.LocaleHelper.setLocale(getActivity(), targetLang);
                }
            }
        });

        // 1. Share APK / App info
        binding.btnShareApp.setOnClickListener(v -> shareAppApk());

        // 2. Download APK to Device Downloads
        binding.btnDownloadApk.setOnClickListener(v -> downloadAppApk());

        // 3. Send Feedback via Email
        View.OnClickListener emailListener = v -> sendFeedbackEmail();
        binding.btnFeedback.setOnClickListener(emailListener);
        binding.tvEmailAddress.setOnClickListener(emailListener);
    }

    private void downloadAppApk() {
        if (getContext() == null) return;
        Context context = requireContext().getApplicationContext();
        Toast.makeText(context, "Downloading APK to Downloads folder...", Toast.LENGTH_SHORT).show();

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                ApplicationInfo appInfo = context.getApplicationInfo();
                File originalApk = new File(appInfo.sourceDir);

                if (!originalApk.exists()) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> Toast.makeText(context, "Could not find app APK file.", Toast.LENGTH_LONG).show());
                    }
                    return;
                }

                String fileName = "indian-law-guide-amit-bharat.apk";
                boolean success = false;
                Uri downloadedUri = null;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
                    values.put(MediaStore.Downloads.MIME_TYPE, "application/vnd.android.package-archive");
                    values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                    downloadedUri = context.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                    if (downloadedUri != null) {
                        try (InputStream in = new FileInputStream(originalApk);
                             OutputStream out = context.getContentResolver().openOutputStream(downloadedUri)) {
                            if (out != null) {
                                byte[] buffer = new byte[8192];
                                int bytesRead;
                                while ((bytesRead = in.read(buffer)) != -1) {
                                    out.write(buffer, 0, bytesRead);
                                }
                                out.flush();
                                success = true;
                            }
                        }
                    }
                } else {
                    File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    if (!downloadsDir.exists()) {
                        downloadsDir.mkdirs();
                    }
                    File destApk = new File(downloadsDir, fileName);
                    try (InputStream in = new FileInputStream(originalApk);
                         OutputStream out = new FileOutputStream(destApk)) {
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = in.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                        }
                        out.flush();
                        success = true;
                    }
                    android.media.MediaScannerConnection.scanFile(
                            context,
                            new String[]{destApk.getAbsolutePath()},
                            new String[]{"application/vnd.android.package-archive"},
                            null
                    );
                    downloadedUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", destApk);
                }

                final boolean isSaved = success;
                final Uri finalUri = downloadedUri;

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (binding == null) return;
                        if (isSaved) {
                            Snackbar.make(
                                    binding.getRoot(),
                                    "Saved to Downloads: " + fileName,
                                    Snackbar.LENGTH_LONG
                            ).setAction("Open Downloads", v -> {
                                try {
                                    Intent intent = new Intent(android.app.DownloadManager.ACTION_VIEW_DOWNLOADS);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    if (finalUri != null) {
                                        try {
                                            Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                                            viewIntent.setDataAndType(finalUri, "application/vnd.android.package-archive");
                                            viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(viewIntent);
                                        } catch (Exception ignored) {}
                                    }
                                }
                            }).show();
                        } else {
                            Toast.makeText(context, "Failed to save APK to Downloads.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> Toast.makeText(context, "Error saving APK: " + e.getMessage(), Toast.LENGTH_LONG).show());
                }
            }
        });
    }

    private void shareAppApk() {
        if (getContext() == null) return;
        Context context = requireContext();
        Toast.makeText(context, "Preparing Indian Law Guide APK to share...", Toast.LENGTH_SHORT).show();

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                ApplicationInfo appInfo = context.getApplicationInfo();
                File originalApk = new File(appInfo.sourceDir);

                if (!originalApk.exists()) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(this::shareAppDescriptionFallback);
                    }
                    return;
                }

                File shareDir = new File(context.getCacheDir(), "shared_apk");
                if (!shareDir.exists()) {
                    shareDir.mkdirs();
                }
                File targetApk = new File(shareDir, "indian-law-guide-amit-bharat.apk");
                try (InputStream in = new FileInputStream(originalApk);
                     OutputStream out = new FileOutputStream(targetApk)) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    out.flush();
                }

                Uri apkUri = FileProvider.getUriForFile(
                        context,
                        context.getPackageName() + ".fileprovider",
                        targetApk
                );

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("application/vnd.android.package-archive");
                shareIntent.putExtra(Intent.EXTRA_STREAM, apkUri);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Indian Law Guide APK - by Amit Bharat (Rooys Soft Tech)");
                shareIntent.putExtra(Intent.EXTRA_TEXT, APP_ABOUT_TEXT);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        startActivity(Intent.createChooser(shareIntent, "Share Indian Law Guide APK & Details"));
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(context, "Sharing description...", Toast.LENGTH_SHORT).show();
                        shareAppDescriptionFallback();
                    });
                }
            }
        });
    }

    private void shareAppDescriptionFallback() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Indian Law Guide - Know Your Rights");
        intent.putExtra(Intent.EXTRA_TEXT, APP_ABOUT_TEXT);
        startActivity(Intent.createChooser(intent, "Share Indian Law Guide"));
    }

    private void sendFeedbackEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:rooyssofttech2020@gmail.com"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rooyssofttech2020@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Indian Law Guide App - Feedback & Support");
        String body = "Hello Rooys Soft Tech Team,\n\n"
                + "Feedback / Feature Request / Bug Report:\n\n\n"
                + "------------------------------\n"
                + "Device: " + Build.MANUFACTURER + " " + Build.MODEL + "\n"
                + "Android: " + Build.VERSION.RELEASE + " (API " + Build.VERSION.SDK_INT + ")\n"
                + "App Version: 1.0.1\n";
        intent.putExtra(Intent.EXTRA_TEXT, body);
        try {
            startActivity(Intent.createChooser(intent, "Send Email"));
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Email app not found. Please email rooyssofttech2020@gmail.com", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
