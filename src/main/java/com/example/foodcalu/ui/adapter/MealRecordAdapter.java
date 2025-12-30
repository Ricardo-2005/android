package com.example.foodcalu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodcalu.R;
import com.example.foodcalu.data.model.MealRecordDetail;
import com.example.foodcalu.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class MealRecordAdapter extends RecyclerView.Adapter<MealRecordAdapter.RecordViewHolder> {
    private final List<MealRecordDetail> items = new ArrayList<>();

    public void submitList(List<MealRecordDetail> records) {
        items.clear();
        if (records != null) {
            items.addAll(records);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_record, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        MealRecordDetail record = items.get(position);
        holder.name.setText(record.foodName);
        holder.mealType.setText(record.mealType);

        float showWeight = record.estimatedWeight > 0f ? record.estimatedWeight : record.grams;
        if (record.unit != null && !"克".equals(record.unit)) {
            holder.grams.setText(String.format("约%.0f克（%s）", showWeight, record.unit));
        } else {
            holder.grams.setText(String.format("%.0f克", showWeight));
        }

        float showKcal = record.estimatedCalories > 0f ? record.estimatedCalories : record.kcal;
        holder.kcal.setText(String.format("%.0f 千卡", showKcal));
        holder.date.setText(DateUtils.formatDate(record.date));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView mealType;
        TextView grams;
        TextView kcal;
        TextView date;

        RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_meal_food);
            mealType = itemView.findViewById(R.id.text_meal_type);
            grams = itemView.findViewById(R.id.text_meal_grams);
            kcal = itemView.findViewById(R.id.text_meal_kcal);
            date = itemView.findViewById(R.id.text_meal_date);
        }
    }
}
