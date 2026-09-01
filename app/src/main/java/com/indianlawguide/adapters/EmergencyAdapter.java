package com.indianlawguide.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indianlawguide.R;
import com.indianlawguide.database.entities.EmergencyEntity;
import com.indianlawguide.databinding.ItemEmergencyCardBinding;

import java.util.ArrayList;
import java.util.List;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.EmergencyViewHolder> {

    private final List<EmergencyEntity> emergencies = new ArrayList<>();

    public EmergencyAdapter() {
    }

    public void setEmergencies(List<EmergencyEntity> list) {
        this.emergencies.clear();
        if (list != null) {
            this.emergencies.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EmergencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEmergencyCardBinding binding = ItemEmergencyCardBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false
        );
        return new EmergencyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyViewHolder holder, int position) {
        holder.bind(emergencies.get(position));
    }

    @Override
    public int getItemCount() {
        return emergencies.size();
    }

    class EmergencyViewHolder extends RecyclerView.ViewHolder {

        private final ItemEmergencyCardBinding binding;

        EmergencyViewHolder(ItemEmergencyCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(EmergencyEntity item) {
            binding.tvEmergencyNumber.setText(item.getNumber());
            binding.tvEmergencyName.setText(item.getName());
            binding.tvEmergencyDescription.setText(item.getDescription());

            Context context = itemView.getContext();

            binding.btnCopyNumber.setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard != null) {
                    ClipData clip = ClipData.newPlainText("Emergency Number", item.getNumber());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, context.getString(R.string.copied_to_clipboard, item.getNumber()), Toast.LENGTH_SHORT).show();
                }
            });

            binding.btnDialNumber.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + item.getNumber().replaceAll("[^0-9]", "")));
                context.startActivity(intent);
            });
        }
    }
}
