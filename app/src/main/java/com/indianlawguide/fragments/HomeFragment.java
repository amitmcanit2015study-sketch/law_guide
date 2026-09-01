package com.indianlawguide.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.indianlawguide.R;
import com.indianlawguide.adapters.CategoryAdapter;
import com.indianlawguide.constants.AppConstants;
import com.indianlawguide.database.entities.LawEntity;
import com.indianlawguide.databinding.FragmentHomeBinding;
import com.indianlawguide.models.CategoryModel;
import com.indianlawguide.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private CategoryAdapter categoryAdapter;
    private LawEntity currentDailyLaw;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        setupRecyclerView();
        setupObservers();
        setupClickListeners();
    }

    private void setupRecyclerView() {
        categoryAdapter = new CategoryAdapter(this::onCategoryClicked);
        int spanCount = getResources().getConfiguration().screenWidthDp >= 600 ? 3 : 2;
        binding.rvCategories.setLayoutManager(new GridLayoutManager(requireContext(), spanCount));
        binding.rvCategories.setAdapter(categoryAdapter);
    }

    private void setupObservers() {
        viewModel.getRandomDailyLaw().observe(getViewLifecycleOwner(), law -> {
            if (law != null) {
                currentDailyLaw = law;
                binding.tvDailyLawTitle.setText(law.getTitle());
                binding.tvDailyLawSummary.setText(law.getSummary());
                binding.tvDailyLawCategory.setText(law.getCategory());
            }
        });

        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                categoryAdapter.setCategories(categories);
            }
        });
    }

    private void setupClickListeners() {
        // Search bar & Voice trigger
        binding.searchBarTrigger.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home_to_search);
        });

        binding.btnVoiceSearchTrigger.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putBoolean("startVoice", true);
            Navigation.findNavController(v).navigate(R.id.action_home_to_search, args);
        });

        // 112 Quick Helpline
        binding.btnQuickEmergency.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:112"));
            startActivity(intent);
        });

        // Daily Law Card
        binding.cardDailyLaw.setOnClickListener(v -> {
            if (currentDailyLaw != null) {
                Bundle args = new Bundle();
                args.putLong(AppConstants.ARG_LAW_ID, currentDailyLaw.getId());
                Navigation.findNavController(v).navigate(R.id.action_home_to_detail, args);
            }
        });

        // Quick Tool Buttons
        binding.btnQuickQuiz.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home_to_quiz);
        });

        binding.btnQuickFaq.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home_to_faq);
        });

        binding.btnQuickHistory.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home_to_history);
        });
    }

    private void onCategoryClicked(CategoryModel category) {
        Bundle args = new Bundle();
        args.putString(AppConstants.ARG_CATEGORY_NAME, category.getName());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_home_to_category, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
