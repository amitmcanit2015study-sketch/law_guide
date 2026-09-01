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
import com.indianlawguide.adapters.NoteAdapter;
import com.indianlawguide.constants.AppConstants;
import com.indianlawguide.database.entities.NoteEntity;
import com.indianlawguide.databinding.FragmentNotesBinding;
import com.indianlawguide.viewmodel.NotesViewModel;

public class NotesFragment extends Fragment implements NoteAdapter.OnNoteActionListener {

    private FragmentNotesBinding binding;
    private NotesViewModel viewModel;
    private NoteAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        adapter = new NoteAdapter(this);
        binding.rvNotes.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvNotes.setAdapter(adapter);

        viewModel.getAllNotes().observe(getViewLifecycleOwner(), notes -> {
            if (notes != null && !notes.isEmpty()) {
                adapter.setNotes(notes);
                binding.layoutEmptyNotes.setVisibility(View.GONE);
                binding.rvNotes.setVisibility(View.VISIBLE);
            } else {
                binding.layoutEmptyNotes.setVisibility(View.VISIBLE);
                binding.rvNotes.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onNoteClick(NoteEntity note) {
        Bundle args = new Bundle();
        args.putLong(AppConstants.ARG_LAW_ID, note.getLawId());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_notes_to_detail, args);
    }

    @Override
    public void onDeleteNote(NoteEntity note) {
        viewModel.deleteNote(note);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
