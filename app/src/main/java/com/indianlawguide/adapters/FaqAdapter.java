package com.indianlawguide.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indianlawguide.database.entities.FaqEntity;
import com.indianlawguide.databinding.ItemFaqCardBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.FaqViewHolder> {

    private final List<FaqEntity> faqs = new ArrayList<>();
    private final Set<Long> expandedItemIds = new HashSet<>();

    public FaqAdapter() {
    }

    public void setFaqs(List<FaqEntity> list) {
        this.faqs.clear();
        if (list != null) {
            this.faqs.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FaqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFaqCardBinding binding = ItemFaqCardBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false
        );
        return new FaqViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FaqViewHolder holder, int position) {
        holder.bind(faqs.get(position));
    }

    @Override
    public int getItemCount() {
        return faqs.size();
    }

    class FaqViewHolder extends RecyclerView.ViewHolder {

        private final ItemFaqCardBinding binding;

        FaqViewHolder(ItemFaqCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(FaqEntity item) {
            binding.tvFaqQuestion.setText(item.getQuestion());
            binding.tvFaqAnswer.setText(item.getAnswer());

            boolean isExpanded = expandedItemIds.contains(item.getId());
            binding.tvFaqAnswer.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            binding.imgFaqExpand.setRotation(isExpanded ? 180f : 0f);

            binding.cardFaq.setOnClickListener(v -> {
                if (isExpanded) {
                    expandedItemIds.remove(item.getId());
                } else {
                    expandedItemIds.add(item.getId());
                }
                notifyItemChanged(getAdapterPosition());
            });
        }
    }
}
