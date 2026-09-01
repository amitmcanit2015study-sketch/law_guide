package com.indianlawguide.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indianlawguide.databinding.ItemCategoryCardBinding;
import com.indianlawguide.models.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(CategoryModel category);
    }

    private final List<CategoryModel> categories = new ArrayList<>();
    private final OnCategoryClickListener listener;

    public CategoryAdapter(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public void setCategories(List<CategoryModel> list) {
        this.categories.clear();
        if (list != null) {
            this.categories.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryCardBinding binding = ItemCategoryCardBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false
        );
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private final ItemCategoryCardBinding binding;

        CategoryViewHolder(ItemCategoryCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(CategoryModel item) {
            binding.tvCategoryEmoji.setText(item.getIconEmoji());
            binding.tvCategoryTitle.setText(item.getName());
            binding.tvLawCountBadge.setText(item.getLawCount() + " Topics");

            binding.cardCategory.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCategoryClick(item);
                }
            });
        }
    }
}
