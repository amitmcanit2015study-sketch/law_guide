package com.indianlawguide.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.indianlawguide.R;
import com.indianlawguide.constants.AppConstants;
import com.indianlawguide.database.entities.LawEntity;
import com.indianlawguide.databinding.FragmentLawDetailBinding;
import com.indianlawguide.utils.PdfPrintHelper;
import com.indianlawguide.utils.TtsHelper;
import com.indianlawguide.viewmodel.LawDetailViewModel;

public class LawDetailFragment extends Fragment implements TtsHelper.TtsStateListener {

    private FragmentLawDetailBinding binding;
    private LawDetailViewModel viewModel;
    private TtsHelper ttsHelper;
    private LawEntity currentLaw;
    private long lawId = -1;
    private float currentTextScale = 1.0f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLawDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            lawId = getArguments().getLong(AppConstants.ARG_LAW_ID, -1);
        }

        viewModel = new ViewModelProvider(this).get(LawDetailViewModel.class);
        ttsHelper = new TtsHelper(requireContext(), this);

        binding.toolbarDetail.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        loadLawDetails();
    }

    private void loadLawDetails() {
        if (lawId <= 0) return;

        viewModel.recordHistory(lawId);

        viewModel.getLawById(lawId).observe(getViewLifecycleOwner(), law -> {
            if (law != null) {
                currentLaw = law;
                bindLawData(law);
            }
        });

        viewModel.isFavorite(lawId).observe(getViewLifecycleOwner(), isFav -> {
            if (Boolean.TRUE.equals(isFav)) {
                binding.btnBookmark.setImageResource(R.drawable.ic_bookmark_filled);
            } else {
                binding.btnBookmark.setImageResource(R.drawable.ic_bookmark);
            }
        });
    }

    private void bindLawData(LawEntity law) {
        binding.tvDetailCategory.setText(law.getCategory());
        binding.tvDetailLastUpdated.setText("Updated " + law.getLastUpdated());
        binding.tvDetailTitle.setText(law.getTitle());
        binding.tvDetailSummary.setText(law.getSummary());
        binding.tvDetailDescription.setText(law.getDescription());
        binding.tvDetailRights.setText(formatBulletPoints(law.getRights()));
        binding.tvDetailDos.setText(formatBulletPoints(law.getDos()));
        binding.tvDetailDonts.setText(formatBulletPoints(law.getDonts()));
        binding.tvDetailLawNameAndSection.setText(law.getLawName() + " — " + law.getSection());
        binding.tvDetailPunishment.setText(law.getPunishment());
        binding.tvDetailHelpline.setText(law.getHelpline());

        // Helpline Call Button
        binding.btnDetailDial.setOnClickListener(v -> {
            String number = law.getEmergencyNumber() != null && !law.getEmergencyNumber().isEmpty() ? law.getEmergencyNumber() : "112";
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + number.replaceAll("[^0-9]", "")));
            startActivity(intent);
        });

        // Bookmark Toggle
        binding.btnBookmark.setOnClickListener(v -> {
            viewModel.toggleFavorite(lawId);
        });

        // Share Law
        binding.btnShare.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            String shareContent = "⚖ *Indian Law Pocket Guide*\n\n*" + law.getTitle() + "*\n\n"
                + law.getSummary() + "\n\n• *Law:* " + law.getLawName() + " (" + law.getSection() + ")\n"
                + "• *Helpline:* " + law.getHelpline() + "\n\n_Know your legal rights offline!_";
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share Law"));
        });

        // PDF Print
        binding.btnPrintPdf.setOnClickListener(v -> {
            PdfPrintHelper.printLawToPdf(requireContext(), law);
        });

        // TTS Read Aloud
        binding.btnTtsRead.setOnClickListener(v -> {
            if (ttsHelper.isSpeaking()) {
                ttsHelper.stop();
                binding.btnTtsRead.setText(R.string.tts_speak);
                binding.btnTtsRead.setIconResource(R.drawable.ic_tts);
            } else {
                String speechText = law.getTitle() + ". " + law.getSummary() + ". Rights: " + law.getRights();
                ttsHelper.speak(speechText);
                binding.btnTtsRead.setText(R.string.tts_stop);
                binding.btnTtsRead.setIconResource(R.drawable.ic_tts_stop);
            }
        });

        // Add Note Dialog
        binding.btnDetailAddNote.setOnClickListener(v -> showAddNoteDialog(law));

        // Font Zoom Controls
        binding.btnZoomIn.setOnClickListener(v -> {
            if (currentTextScale < 1.4f) {
                currentTextScale += 0.1f;
                applyTextScaling(currentTextScale);
            }
        });

        binding.btnZoomOut.setOnClickListener(v -> {
            if (currentTextScale > 0.85f) {
                currentTextScale -= 0.1f;
                applyTextScaling(currentTextScale);
            }
        });
    }

    private void applyTextScaling(float scale) {
        binding.tvDetailDescription.setTextSize(14f * scale);
        binding.tvDetailRights.setTextSize(14f * scale);
        binding.tvDetailDos.setTextSize(13f * scale);
        binding.tvDetailDonts.setTextSize(13f * scale);
    }

    private String formatBulletPoints(String text) {
        if (text == null) return "";
        String[] points = text.split(";");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < points.length; i++) {
            String p = points[i].trim();
            if (!p.isEmpty()) {
                sb.append("• ").append(p);
                if (i < points.length - 1) sb.append("\n\n");
            }
        }
        return sb.toString();
    }

    private void showAddNoteDialog(LawEntity law) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_note, null);
        EditText etNote = dialogView.findViewById(R.id.etNoteContent);

        new AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton(R.string.save_note, (dialog, which) -> {
                String noteText = etNote.getText().toString().trim();
                if (!noteText.isEmpty()) {
                    viewModel.saveNote(law.getId(), law.getTitle(), noteText);
                    Toast.makeText(requireContext(), "Personal note saved locally", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    @Override
    public void onSpeechStart() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (binding != null) {
                    binding.btnTtsRead.setText(R.string.tts_stop);
                    binding.btnTtsRead.setIconResource(R.drawable.ic_tts_stop);
                }
            });
        }
    }

    @Override
    public void onSpeechDone() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (binding != null) {
                    binding.btnTtsRead.setText(R.string.tts_speak);
                    binding.btnTtsRead.setIconResource(R.drawable.ic_tts);
                }
            });
        }
    }

    @Override
    public void onSpeechError() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (binding != null) {
                    binding.btnTtsRead.setText(R.string.tts_speak);
                    binding.btnTtsRead.setIconResource(R.drawable.ic_tts);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        if (ttsHelper != null) {
            ttsHelper.stop();
            ttsHelper.shutdown();
        }
        super.onDestroyView();
        binding = null;
    }
}
