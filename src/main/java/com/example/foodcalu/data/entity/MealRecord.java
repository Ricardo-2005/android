package com.example.foodcalu.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "meal_record",
        foreignKeys = @ForeignKey(
                entity = Food.class,
                parentColumns = "id",
                childColumns = "foodId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("foodId"), @Index("date")}
)
public class MealRecord {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public long date;
    public String mealType;
    public long foodId;
    public float grams;
    public float kcal;
    public String unit;
    public float estimatedWeight;
    public float estimatedCalories;
    public long createdAt;

    public MealRecord(long id, long date, String mealType, long foodId, float grams, float kcal,
                      String unit, float estimatedWeight, float estimatedCalories, long createdAt) {
        this.id = id;
        this.date = date;
        this.mealType = mealType;
        this.foodId = foodId;
        this.grams = grams;
        this.kcal = kcal;
        this.unit = unit;
        this.estimatedWeight = estimatedWeight;
        this.estimatedCalories = estimatedCalories;
        this.createdAt = createdAt;
    }
}
