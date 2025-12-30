package com.example.foodcalu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodcalu.R;
import com.example.foodcalu.data.entity.WorkoutItem;

import java.util.ArrayList;
import java.util.List;

public class WorkoutItemAdapter extends RecyclerView.Adapter<WorkoutItemAdapter.WorkoutViewHolder> {
    public interface OnWorkoutClickListener {
        void onWorkoutClick(WorkoutItem item);
    }

    private final List<WorkoutItem> items = new ArrayList<>();
    private final OnWorkoutClickListener listener;

    public WorkoutItemAdapter(OnWorkoutClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<WorkoutItem> list) {
        items.clear();
        if (list != null) {
            items.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout_item, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        WorkoutItem item = items.get(position);
        holder.name.setText(item.name);
        holder.meta.setText(String.format("%s | %s", item.difficulty, item.targetMuscle));
        holder.suggestion.setText(item.suggestion);
        holder.kcal.setText(String.format("约%d 千卡", item.kcalEstimate));
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onWorkoutClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView meta;
        TextView suggestion;
        TextView kcal;

        WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_workout_name);
            meta = itemView.findViewById(R.id.text_workout_meta);
            suggestion = itemView.findViewById(R.id.text_workout_suggestion);
            kcal = itemView.findViewById(R.id.text_workout_kcal);
        }
    }
}
