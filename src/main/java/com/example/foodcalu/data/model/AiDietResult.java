package com.example.foodcalu.data.model;

import java.util.List;

public class AiDietResult {
    public final List<AiDietItem> items;
    public final float totalCalories;
    public final String note;

    public AiDietResult(List<AiDietItem> items, float totalCalories, String note) {
        this.items = items;
        this.totalCalories = totalCalories;
        this.note = note;
    }
}
