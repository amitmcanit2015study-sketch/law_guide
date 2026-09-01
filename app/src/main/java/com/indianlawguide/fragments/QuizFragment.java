package com.indianlawguide.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.indianlawguide.R;
import com.indianlawguide.constants.AppConstants;
import com.indianlawguide.databinding.FragmentQuizBinding;

public class QuizFragment extends Fragment {

    private FragmentQuizBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.toolbarQuiz.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        binding.btnStartAllQuiz.setOnClickListener(v -> startQuiz("All"));
        binding.btnStartPoliceQuiz.setOnClickListener(v -> startQuiz("Police Rights"));
        binding.btnStartTrafficQuiz.setOnClickListener(v -> startQuiz("Traffic Laws"));
        binding.btnStartWomenCyberQuiz.setOnClickListener(v -> startQuiz("Women Rights"));
    }

    private void startQuiz(String category) {
        Bundle args = new Bundle();
        args.putString(AppConstants.ARG_QUIZ_CATEGORY, category);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_quiz_to_play, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
