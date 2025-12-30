package com.example.foodcalu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodcalu.R;
import com.example.foodcalu.data.entity.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    public interface OnFoodClickListener {
        void onFoodClick(Food food);
    }

    private final List<Food> items = new ArrayList<>();
    private final OnFoodClickListener listener;

    public FoodAdapter(OnFoodClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Food> foods) {
        items.clear();
        if (foods != null) {
            items.addAll(foods);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = items.get(position);
        holder.name.setText(food.name);
        holder.meta.setText(String.format("%s 千卡/100克", Math.round(food.kcalPer100g)));
        holder.tags.setText(food.tags == null ? "" : food.tags);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFoodClick(food);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView meta;
        TextView tags;

        FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_food_name);
            meta = itemView.findViewById(R.id.text_food_meta);
            tags = itemView.findViewById(R.id.text_food_tags);
        }
    }
}
