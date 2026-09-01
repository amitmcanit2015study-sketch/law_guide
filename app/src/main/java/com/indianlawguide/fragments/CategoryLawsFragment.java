package com.indianlawguide.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.indianlawguide.R;
import com.indianlawguide.adapters.LawCardAdapter;
import com.indianlawguide.constants.AppConstants;
import com.indianlawguide.database.entities.LawEntity;
import com.indianlawguide.databinding.FragmentCategoryLawsBinding;
import com.indianlawguide.repository.LawRepository;

public class CategoryLawsFragment extends Fragment {

    private FragmentCategoryLawsBinding binding;
    private LawCardAdapter adapter;
    private String categoryName = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCategoryLawsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            categoryName = getArguments().getString(AppConstants.ARG_CATEGORY_NAME, "Category Laws");
        }

        binding.toolbarCategory.setTitle(categoryName);
        binding.toolbarCategory.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        adapter = new LawCardAdapter(this::onLawClicked);
        binding.rvCategoryLaws.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvCategoryLaws.setAdapter(adapter);

        LawRepository repository = new LawRepository(requireActivity().getApplication());
        repository.getLawsByCategory(categoryName).observe(getViewLifecycleOwner(), laws -> {
            if (laws != null && !laws.isEmpty()) {
                adapter.setLaws(laws);
                binding.tvEmptyState.setVisibility(View.GONE);
                binding.rvCategoryLaws.setVisibility(View.VISIBLE);
            } else {
                binding.tvEmptyState.setVisibility(View.VISIBLE);
                binding.rvCategoryLaws.setVisibility(View.GONE);
            }
        });
    }

    private void onLawClicked(LawEntity law) {
        Bundle args = new Bundle();
        args.putLong(AppConstants.ARG_LAW_ID, law.getId());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_category_to_detail, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
