package com.indianlawguide.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.card.MaterialCardView;
import com.indianlawguide.R;
import com.indianlawguide.constants.AppConstants;
import com.indianlawguide.database.entities.QuizEntity;
import com.indianlawguide.databinding.FragmentQuizPlayBinding;
import com.indianlawguide.viewmodel.QuizViewModel;

import java.util.ArrayList;
import java.util.List;

public class QuizPlayFragment extends Fragment {

    private FragmentQuizPlayBinding binding;
    private QuizViewModel viewModel;
    private final List<QuizEntity> questionList = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;
    private boolean isOptionSelected = false;
    private String category = "All";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQuizPlayBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            category = getArguments().getString(AppConstants.ARG_QUIZ_CATEGORY, "All");
        }

        viewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        binding.toolbarQuizPlay.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        viewModel.getQuizzes(category).observe(getViewLifecycleOwner(), quizzes -> {
            if (quizzes != null && !quizzes.isEmpty()) {
                questionList.clear();
                questionList.addAll(quizzes);
                binding.progressBarQuiz.setMax(questionList.size());
                currentIndex = 0;
                score = 0;
                displayCurrentQuestion();
            }
        });

        binding.btnNextQuestion.setOnClickListener(v -> {
            currentIndex++;
            if (currentIndex < questionList.size()) {
                displayCurrentQuestion();
            } else {
                showResults();
            }
        });

        binding.btnRestartQuiz.setOnClickListener(v -> {
            currentIndex = 0;
            score = 0;
            binding.layoutQuizResults.setVisibility(View.GONE);
            binding.layoutQuestionContainer.setVisibility(View.VISIBLE);
            displayCurrentQuestion();
        });
    }

    private void displayCurrentQuestion() {
        if (questionList.isEmpty() || currentIndex >= questionList.size()) return;

        QuizEntity current = questionList.get(currentIndex);
        isOptionSelected = false;

        binding.progressBarQuiz.setProgress(currentIndex + 1);
        binding.tvQuestionCounter.setText("Question " + (currentIndex + 1) + " of " + questionList.size());
        binding.tvQuizCategoryBadge.setText(current.getCategory());
        binding.tvQuestionText.setText(current.getQuestion());

        binding.tvQuizFeedback.setVisibility(View.INVISIBLE);
        binding.btnNextQuestion.setEnabled(false);
        binding.btnNextQuestion.setText(currentIndex == questionList.size() - 1 ? R.string.quiz_finish : R.string.quiz_next);

        setupOption(binding.optionCard1.getRoot(), "A", current.getOption1(), 1, current.getAnswer());
        setupOption(binding.optionCard2.getRoot(), "B", current.getOption2(), 2, current.getAnswer());
        setupOption(binding.optionCard3.getRoot(), "C", current.getOption3(), 3, current.getAnswer());
        setupOption(binding.optionCard4.getRoot(), "D", current.getOption4(), 4, current.getAnswer());
    }

    private void setupOption(View cardView, String letter, String text, int optionNum, int correctOption) {
        MaterialCardView card = (MaterialCardView) cardView;
        TextView tvIndex = cardView.findViewById(R.id.tvOptionIndex);
        TextView tvText = cardView.findViewById(R.id.tvOptionText);
        ImageView imgFeedback = cardView.findViewById(R.id.imgOptionFeedback);

        tvIndex.setText(letter);
        tvText.setText(text);
        imgFeedback.setVisibility(View.GONE);

        card.setStrokeColor(Color.parseColor("#73777F"));
        card.setStrokeWidth(1);
        card.setCardBackgroundColor(Color.TRANSPARENT);

        card.setOnClickListener(v -> {
            if (isOptionSelected) return;
            isOptionSelected = true;

            binding.btnNextQuestion.setEnabled(true);

            if (optionNum == correctOption) {
                score++;
                card.setStrokeColor(Color.parseColor("#2E7D32"));
                card.setCardBackgroundColor(Color.parseColor("#E8F5E9"));
                imgFeedback.setVisibility(View.VISIBLE);
                imgFeedback.setImageResource(R.drawable.ic_check_circle);

                binding.tvQuizFeedback.setText("✓ Correct Answer!");
                binding.tvQuizFeedback.setTextColor(Color.parseColor("#2E7D32"));
            } else {
                card.setStrokeColor(Color.parseColor("#C62828"));
                card.setCardBackgroundColor(Color.parseColor("#FFEBEE"));
                imgFeedback.setVisibility(View.VISIBLE);
                imgFeedback.setImageResource(R.drawable.ic_cancel);

                highlightCorrectOption(correctOption);
                binding.tvQuizFeedback.setText("✕ Incorrect. Option " + getOptionLetter(correctOption) + " is correct.");
                binding.tvQuizFeedback.setTextColor(Color.parseColor("#C62828"));
            }
            binding.tvQuizFeedback.setVisibility(View.VISIBLE);
        });
    }

    private void highlightCorrectOption(int correctOption) {
        View target = null;
        if (correctOption == 1) target = binding.optionCard1.getRoot();
        else if (correctOption == 2) target = binding.optionCard2.getRoot();
        else if (correctOption == 3) target = binding.optionCard3.getRoot();
        else if (correctOption == 4) target = binding.optionCard4.getRoot();

        if (target != null) {
            MaterialCardView card = (MaterialCardView) target;
            card.setStrokeColor(Color.parseColor("#2E7D32"));
            card.setCardBackgroundColor(Color.parseColor("#E8F5E9"));
            ImageView imgFeedback = target.findViewById(R.id.imgOptionFeedback);
            imgFeedback.setVisibility(View.VISIBLE);
            imgFeedback.setImageResource(R.drawable.ic_check_circle);
        }
    }

    private String getOptionLetter(int num) {
        switch (num) {
            case 1: return "A";
            case 2: return "B";
            case 3: return "C";
            case 4: return "D";
            default: return "";
        }
    }

    private void showResults() {
        binding.layoutQuestionContainer.setVisibility(View.GONE);
        binding.layoutQuizResults.setVisibility(View.VISIBLE);

        binding.tvFinalScore.setText(score + " / " + questionList.size());

        int percent = (int) (((float) score / questionList.size()) * 100);
        if (percent >= 80) {
            binding.tvResultFeedback.setText("🎉 Outstanding Legal Awareness!\nYou possess a strong understanding of Indian legal rights.");
        } else if (percent >= 50) {
            binding.tvResultFeedback.setText("👍 Good Effort!\nKeep exploring the categories to learn more statutory protections.");
        } else {
            binding.tvResultFeedback.setText("📚 Keep Learning!\nReview the fundamental rights and citizen protections in the app.");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
