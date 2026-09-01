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
import com.indianlawguide.constants.AppConstants;
import com.indianlawguide.database.entities.LawEntity;
import com.indianlawguide.databinding.FragmentSearchBinding;
import com.indianlawguide.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.Locale;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;
    private LawCardAdapter adapter;

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

        setupRecyclerView();
        setupSearchInput();
        setupRecentSearches();
        setupObservers();

        if (getArguments() != null && getArguments().getBoolean("startVoice", false)) {
            startVoiceRecognition();
        }
    }

    private void setupRecyclerView() {
        adapter = new LawCardAdapter(this::onLawClicked);
        binding.rvSearchResults.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvSearchResults.setAdapter(adapter);
    }

    private void setupSearchInput() {
        binding.etSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString();
                binding.btnClearSearch.setVisibility(query.isEmpty() ? View.GONE : View.VISIBLE);
                adapter.setHighlightQuery(query);
                viewModel.setQuery(query);

                if (query.isEmpty()) {
                    loadRecentSearches();
                } else {
                    binding.layoutRecentSearches.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.btnClearSearch.setOnClickListener(v -> binding.etSearchInput.setText(""));

        binding.btnVoiceSearch.setOnClickListener(v -> startVoiceRecognition());
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
                adapter.setLaws(new ArrayList<>());
                binding.layoutEmptyResults.setVisibility(View.GONE);
            } else if (results != null && !results.isEmpty()) {
                adapter.setLaws(results);
                binding.layoutEmptyResults.setVisibility(View.GONE);
                binding.rvSearchResults.setVisibility(View.VISIBLE);
                viewModel.saveRecentSearch(query);
            } else {
                adapter.setLaws(new ArrayList<>());
                binding.layoutEmptyResults.setVisibility(View.VISIBLE);
                binding.rvSearchResults.setVisibility(View.GONE);
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
