package com.indianlawguide.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.chip.Chip;
import com.indianlawguide.R;
import com.indianlawguide.adapters.LawCardAdapter;
import com.indianlawguide.adapters.OnlineSearchAdapter;
import com.indianlawguide.constants.AppConstants;
import com.indianlawguide.database.entities.LawEntity;
import com.indianlawguide.databinding.FragmentSearchBinding;
import com.indianlawguide.utils.AiLegalEngine;
import com.indianlawguide.utils.OnlineLegalSearchHelper;
import com.indianlawguide.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;
    private LawCardAdapter offlineAdapter;
    private OnlineSearchAdapter onlineAdapter;

    private final ActivityResultLauncher<Intent> voiceLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                ArrayList<String> matches = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (matches != null && !matches.isEmpty()) {
                    String spokenText = matches.get(0);
                    binding.etSearchInput.setText(spokenText);
                    binding.etSearchInput.setSelection(spokenText.length());
                }
            }
        }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        setupRecyclerViews();
        setupSearchInput();
        setupRecentSearches();
        setupObservers();

        if (getArguments() != null && getArguments().getBoolean("startVoice", false)) {
            startVoiceRecognition();
        }
    }

    private void setupRecyclerViews() {
        // Offline Recycler
        offlineAdapter = new LawCardAdapter(this::onLawClicked);
        binding.rvOfflineResults.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvOfflineResults.setAdapter(offlineAdapter);

        // Online Recycler
        onlineAdapter = new OnlineSearchAdapter();
        binding.rvOnlineResults.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvOnlineResults.setAdapter(onlineAdapter);
    }

    private void setupSearchInput() {
        binding.etSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                binding.btnClearSearch.setVisibility(query.isEmpty() ? View.GONE : View.VISIBLE);
                offlineAdapter.setHighlightQuery(query);
                viewModel.setQuery(query);

                if (query.isEmpty()) {
                    hideAllResults();
                    loadRecentSearches();
                } else {
                    binding.layoutRecentSearches.setVisibility(View.GONE);
                    executeAiAndOnlineSearch(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.btnClearSearch.setOnClickListener(v -> binding.etSearchInput.setText(""));
        binding.btnVoiceSearch.setOnClickListener(v -> startVoiceRecognition());
    }

    private void hideAllResults() {
        binding.cardAiResponse.setVisibility(View.GONE);
        binding.layoutOfflineSection.setVisibility(View.GONE);
        binding.layoutOnlineSection.setVisibility(View.GONE);
        binding.layoutEmptyResults.setVisibility(View.GONE);
    }

    private void executeAiAndOnlineSearch(String query) {
        // 1. AI Legal Engine Summary
        AiLegalEngine.AiResponse aiResp = AiLegalEngine.generateLegalAdvice(query);
        if (aiResp != null) {
            binding.cardAiResponse.setVisibility(View.VISIBLE);
            binding.tvAiVerdict.setText(aiResp.getVerdict());
            binding.tvAiSection.setText("• Statutory Basis: " + aiResp.getLegalSection());
            binding.tvAiAdvice.setText(aiResp.getActionableAdvice());
        } else {
            binding.cardAiResponse.setVisibility(View.GONE);
        }

        // 2. Online Live Search (if connected)
        OnlineLegalSearchHelper.searchOnline(requireContext(), query, new OnlineLegalSearchHelper.SearchCallback() {
            @Override
            public void onOnlineResults(List<OnlineLegalSearchHelper.OnlineLegalItem> results) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (binding != null && !binding.etSearchInput.getText().toString().trim().isEmpty()) {
                            binding.layoutOnlineSection.setVisibility(View.VISIBLE);
                            binding.tvOnlineStatusBadge.setText("Live Web Results (" + results.size() + ")");
                            onlineAdapter.setItems(results);
                        }
                    });
                }
            }

            @Override
            public void onOfflineMode() {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (binding != null && !binding.etSearchInput.getText().toString().trim().isEmpty()) {
                            binding.layoutOnlineSection.setVisibility(View.VISIBLE);
                            binding.tvOnlineStatusBadge.setText("Offline Mode");
                            List<OnlineLegalSearchHelper.OnlineLegalItem> offlineNotice = new ArrayList<>();
                            offlineNotice.add(new OnlineLegalSearchHelper.OnlineLegalItem(
                                "Offline Search Active",
                                "Device is currently offline. All primary Indian laws, sections, and citizen rights are fully searchable from the local database.",
                                ""
                            ));
                            onlineAdapter.setItems(offlineNotice);
                        }
                    });
                }
            }

            @Override
            public void onError(String message) {
                // Ignore network errors gracefully
            }
        });
    }

    private void setupRecentSearches() {
        binding.btnClearRecent.setOnClickListener(v -> {
            viewModel.clearRecentSearches();
            loadRecentSearches();
        });
        loadRecentSearches();
    }

    private void loadRecentSearches() {
        binding.chipGroupRecent.removeAllViews();
        String recentStr = viewModel.getRecentSearches();
        if (recentStr.isEmpty()) {
            binding.layoutRecentSearches.setVisibility(View.GONE);
            return;
        }

        String[] queries = recentStr.split(",");
        boolean hasItems = false;
        for (String q : queries) {
            String trimmed = q.trim();
            if (!trimmed.isEmpty()) {
                hasItems = true;
                Chip chip = new Chip(requireContext());
                chip.setText(trimmed);
                chip.setCheckable(false);
                chip.setClickable(true);
                chip.setOnClickListener(v -> {
                    binding.etSearchInput.setText(trimmed);
                    binding.etSearchInput.setSelection(trimmed.length());
                });
                binding.chipGroupRecent.addView(chip);
            }
        }
        binding.layoutRecentSearches.setVisibility(hasItems ? View.VISIBLE : View.GONE);
    }

    private void setupObservers() {
        viewModel.getSearchResults().observe(getViewLifecycleOwner(), results -> {
            String query = binding.etSearchInput.getText().toString().trim();
            if (query.isEmpty()) {
                offlineAdapter.setLaws(new ArrayList<>());
                binding.layoutOfflineSection.setVisibility(View.GONE);
            } else if (results != null && !results.isEmpty()) {
                offlineAdapter.setLaws(results);
                binding.layoutOfflineSection.setVisibility(View.VISIBLE);
                binding.tvOfflineCountBadge.setText(results.size() + " Found");
                binding.layoutEmptyResults.setVisibility(View.GONE);
                viewModel.saveRecentSearch(query);
            } else {
                offlineAdapter.setLaws(new ArrayList<>());
                binding.layoutOfflineSection.setVisibility(View.GONE);
            }
        });
    }

    private void startVoiceRecognition() {
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.voice_search_prompt));
            voiceLauncher.launch(intent);
        } catch (Exception e) {
            // Speech recognizer not available
        }
    }

    private void onLawClicked(LawEntity law) {
        Bundle args = new Bundle();
        args.putLong(AppConstants.ARG_LAW_ID, law.getId());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_search_to_detail, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
