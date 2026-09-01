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
import com.indianlawguide.databinding.FragmentFavoritesBinding;
import com.indianlawguide.repository.LawRepository;

public class FavoritesFragment extends Fragment {

    private FragmentFavoritesBinding binding;
    private LawCardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new LawCardAdapter(this::onLawClicked);
        binding.rvFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFavorites.setAdapter(adapter);

        LawRepository repository = new LawRepository(requireActivity().getApplication());
        repository.getFavoriteLaws().observe(getViewLifecycleOwner(), laws -> {
            if (laws != null && !laws.isEmpty()) {
                adapter.setLaws(laws);
                binding.layoutEmptyFavorites.setVisibility(View.GONE);
                binding.rvFavorites.setVisibility(View.VISIBLE);
            } else {
                binding.layoutEmptyFavorites.setVisibility(View.VISIBLE);
                binding.rvFavorites.setVisibility(View.GONE);
            }
        });
    }

    private void onLawClicked(LawEntity law) {
        Bundle args = new Bundle();
        args.putLong(AppConstants.ARG_LAW_ID, law.getId());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_favorites_to_detail, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
