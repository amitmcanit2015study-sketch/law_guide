package com.indianlawguide.adapters;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indianlawguide.database.entities.NoteEntity;
import com.indianlawguide.databinding.ItemNoteCardBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    public interface OnNoteActionListener {
        void onNoteClick(NoteEntity note);
        void onDeleteNote(NoteEntity note);
    }

    private final List<NoteEntity> notes = new ArrayList<>();
    private final OnNoteActionListener listener;

    public NoteAdapter(OnNoteActionListener listener) {
        this.listener = listener;
    }

    public void setNotes(List<NoteEntity> list) {
        this.notes.clear();
        if (list != null) {
            this.notes.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNoteCardBinding binding = ItemNoteCardBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false
        );
        return new NoteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.bind(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        private final ItemNoteCardBinding binding;

        NoteViewHolder(ItemNoteCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(NoteEntity item) {
            binding.tvNoteLawTitle.setText(item.getLawTitle());
            binding.tvNoteContent.setText(item.getNote());

            String dateStr = DateFormat.format("dd MMM yyyy, hh:mm a", new Date(item.getCreatedAt())).toString();
            binding.tvNoteDate.setText(dateStr);

            binding.btnDeleteNote.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteNote(item);
                }
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onNoteClick(item);
                }
            });
        }
    }
}
