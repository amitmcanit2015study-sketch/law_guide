package com.indianlawguide.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.indianlawguide.adapters.EmergencyAdapter;
import com.indianlawguide.databinding.FragmentEmergencyBinding;
import com.indianlawguide.repository.EmergencyRepository;

public class EmergencyFragment extends Fragment {

    private FragmentEmergencyBinding binding;
    private EmergencyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEmergencyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new EmergencyAdapter();
        binding.rvEmergencies.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvEmergencies.setAdapter(adapter);

        EmergencyRepository repository = new EmergencyRepository(requireActivity().getApplication());
        repository.getAllEmergencies().observe(getViewLifecycleOwner(), emergencies -> {
            if (emergencies != null) {
                adapter.setEmergencies(emergencies);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
