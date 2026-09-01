package com.indianlawguide.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indianlawguide.database.entities.LawEntity;
import com.indianlawguide.databinding.ItemLawCardBinding;
import com.indianlawguide.utils.TextHighlightHelper;

import java.util.ArrayList;
import java.util.List;

public class LawCardAdapter extends RecyclerView.Adapter<LawCardAdapter.LawViewHolder> {

    public interface OnLawClickListener {
        void onLawClick(LawEntity law);
    }

    private final List<LawEntity> laws = new ArrayList<>();
    private final OnLawClickListener listener;
    private String highlightQuery = "";

    public LawCardAdapter(OnLawClickListener listener) {
        this.listener = listener;
    }

    public void setLaws(List<LawEntity> list) {
        this.laws.clear();
        if (list != null) {
            this.laws.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void setHighlightQuery(String query) {
        this.highlightQuery = query == null ? "" : query.trim();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LawViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLawCardBinding binding = ItemLawCardBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false
        );
        return new LawViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LawViewHolder holder, int position) {
        holder.bind(laws.get(position));
    }

    @Override
    public int getItemCount() {
        return laws.size();
    }

    class LawViewHolder extends RecyclerView.ViewHolder {

        private final ItemLawCardBinding binding;

        LawViewHolder(ItemLawCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(LawEntity item) {
            binding.tvCategoryPill.setText(item.getCategory());
            binding.tvLawSectionBadge.setText(item.getSection());

            if (!highlightQuery.isEmpty()) {
                binding.tvLawTitle.setText(TextHighlightHelper.highlightText(item.getTitle(), highlightQuery));
                binding.tvLawSummary.setText(TextHighlightHelper.highlightText(item.getSummary(), highlightQuery));
            } else {
                binding.tvLawTitle.setText(item.getTitle());
                binding.tvLawSummary.setText(item.getSummary());
            }

            binding.cardLaw.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onLawClick(item);
                }
            });
        }
    }
}
