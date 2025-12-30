package com.example.foodcalu.data.model;

import androidx.room.ColumnInfo;

public class MealRecordDetail {
    @ColumnInfo(name = "id")
    public long id;
    @ColumnInfo(name = "date")
    public long date;
    @ColumnInfo(name = "mealType")
    public String mealType;
    @ColumnInfo(name = "foodId")
    public long foodId;
    @ColumnInfo(name = "foodName")
    public String foodName;
    @ColumnInfo(name = "foodUnit")
    public String foodUnit;
    @ColumnInfo(name = "grams")
    public float grams;
    @ColumnInfo(name = "kcal")
    public float kcal;
    @ColumnInfo(name = "unit")
    public String unit;
    @ColumnInfo(name = "estimatedWeight")
    public float estimatedWeight;
    @ColumnInfo(name = "estimatedCalories")
    public float estimatedCalories;
    @ColumnInfo(name = "tags")
    public String tags;

    public MealRecordDetail(long id, long date, String mealType, long foodId, String foodName,
                            String foodUnit, float grams, float kcal, String unit,
                            float estimatedWeight, float estimatedCalories, String tags) {
        this.id = id;
        this.date = date;
        this.mealType = mealType;
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodUnit = foodUnit;
        this.grams = grams;
        this.kcal = kcal;
        this.unit = unit;
        this.estimatedWeight = estimatedWeight;
        this.estimatedCalories = estimatedCalories;
        this.tags = tags;
    }
}
