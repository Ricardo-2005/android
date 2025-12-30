package com.example.foodcalu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodcalu.R;
import com.example.foodcalu.data.entity.DietRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DietRecordAdapter extends RecyclerView.Adapter<DietRecordAdapter.DietRecordViewHolder> {
    private final List<DietRecord> items = new ArrayList<>();

    public void setItems(List<DietRecord> records) {
        items.clear();
        if (records != null) {
            items.addAll(records);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DietRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_diet_record, parent, false);
        return new DietRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DietRecordViewHolder holder, int position) {
        DietRecord record = items.get(position);
        holder.nameText.setText(record.foodName);
        String weightText = record.estimatedWeightG > 0f
                ? String.format(Locale.CHINA, "≈%.0fg", record.estimatedWeightG)
                : "≈-";
        String calorieText = String.format(Locale.CHINA, "%.0f kcal", record.estimatedCalories);
        holder.detailText.setText(weightText + "   " + calorieText);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class DietRecordViewHolder extends RecyclerView.ViewHolder {
        final TextView nameText;
        final TextView detailText;

        DietRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.text_food_name);
            detailText = itemView.findViewById(R.id.text_food_detail);
        }
    }
}
