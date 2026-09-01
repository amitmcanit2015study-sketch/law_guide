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

import com.indianlawguide.adapters.FaqAdapter;
import com.indianlawguide.databinding.FragmentFaqBinding;
import com.indianlawguide.repository.EmergencyRepository;

public class FaqFragment extends Fragment {

    private FragmentFaqBinding binding;
    private FaqAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFaqBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.toolbarFaq.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        adapter = new FaqAdapter();
        binding.rvFaqs.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFaqs.setAdapter(adapter);

        EmergencyRepository repository = new EmergencyRepository(requireActivity().getApplication());
        repository.getAllFaqs().observe(getViewLifecycleOwner(), faqs -> {
            if (faqs != null) {
                adapter.setFaqs(faqs);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
