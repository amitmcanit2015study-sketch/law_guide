package com.indianlawguide.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.indianlawguide.R;
import com.indianlawguide.adapters.LawCardAdapter;
import com.indianlawguide.constants.AppConstants;
import com.indianlawguide.database.entities.LawEntity;
import com.indianlawguide.databinding.FragmentHistoryBinding;
import com.indianlawguide.repository.LawRepository;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private LawCardAdapter adapter;
    private LawRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = new LawRepository(requireActivity().getApplication());

        binding.toolbarHistory.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        binding.btnClearHistory.setOnClickListener(v -> repository.clearHistory());

        adapter = new LawCardAdapter(this::onLawClicked);
        binding.rvHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvHistory.setAdapter(adapter);

        repository.getRecentlyViewedLaws().observe(getViewLifecycleOwner(), laws -> {
            if (laws != null && !laws.isEmpty()) {
                adapter.setLaws(laws);
                binding.layoutEmptyHistory.setVisibility(View.GONE);
                binding.rvHistory.setVisibility(View.VISIBLE);
                binding.btnClearHistory.setVisibility(View.VISIBLE);
            } else {
                binding.layoutEmptyHistory.setVisibility(View.VISIBLE);
                binding.rvHistory.setVisibility(View.GONE);
                binding.btnClearHistory.setVisibility(View.GONE);
            }
        });
    }

    private void onLawClicked(LawEntity law) {
        Bundle args = new Bundle();
        args.putLong(AppConstants.ARG_LAW_ID, law.getId());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_history_to_detail, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
