package com.example.foodcalu.data.model;

public class AiDietItem {
    public final String food;
    public final float estimatedWeightG;
    public final float caloriesKcal;

    public AiDietItem(String food, float estimatedWeightG, float caloriesKcal) {
        this.food = food;
        this.estimatedWeightG = estimatedWeightG;
        this.caloriesKcal = caloriesKcal;
    }
}
