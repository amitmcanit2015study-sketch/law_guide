package com.indianlawguide.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.indianlawguide.R;
import com.indianlawguide.databinding.FragmentAboutBinding;

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

        // Share App
        binding.btnShareApp.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            String shareMessage = "⚖ *Indian Law Pocket Guide*\n\n"
                + "Know your legal rights, police guidelines, traffic laws, and cyber safety—100% offline with zero ads!\n\n"
                + "Developed by Rooys Soft Tech";
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share App"));
        });

        // View Disclaimer Dialog
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

        // Send Feedback via Email
        View.OnClickListener emailListener = v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:rooyssofttech2020@gmail.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback: Indian Law Pocket Guide");
            try {
                startActivity(Intent.createChooser(emailIntent, "Send Feedback"));
            } catch (Exception e) {
                // Email client not found
            }
        };

        binding.btnSendFeedback.setOnClickListener(emailListener);
        binding.tvEmailAddress.setOnClickListener(emailListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
