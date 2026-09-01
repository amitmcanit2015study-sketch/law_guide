package com.indianlawguide.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indianlawguide.databinding.ItemOnlineSearchCardBinding;
import com.indianlawguide.utils.OnlineLegalSearchHelper.OnlineLegalItem;

import java.util.ArrayList;
import java.util.List;

public class OnlineSearchAdapter extends RecyclerView.Adapter<OnlineSearchAdapter.OnlineViewHolder> {

    private final List<OnlineLegalItem> items = new ArrayList<>();

    public OnlineSearchAdapter() {
    }

    public void setItems(List<OnlineLegalItem> list) {
        this.items.clear();
        if (list != null) {
            this.items.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OnlineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOnlineSearchCardBinding binding = ItemOnlineSearchCardBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false
        );
        return new OnlineViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class OnlineViewHolder extends RecyclerView.ViewHolder {

        private final ItemOnlineSearchCardBinding binding;

        OnlineViewHolder(ItemOnlineSearchCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(OnlineLegalItem item) {
            binding.tvOnlineTitle.setText(item.getTitle());
            binding.tvOnlineSnippet.setText(item.getSnippet());

            binding.cardOnlineItem.setOnClickListener(v -> {
                Context context = v.getContext();
                String url = item.getSourceUrl();
                if (url != null && !url.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    try {
                        context.startActivity(intent);
                    } catch (Exception e) {
                        // Web browser not available
                    }
                }
            });
        }
    }
}
