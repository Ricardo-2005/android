package com.example.foodcalu.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "food")
public class Food {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
    public String unit;
    public float kcalPer100g;
    public float protein;
    public float fat;
    public float carbs;
    public String tags;
    public String imageUrl;

    public Food(long id, String name, String unit, float kcalPer100g, float protein, float fat,
                float carbs, String tags, String imageUrl) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.kcalPer100g = kcalPer100g;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.tags = tags;
        this.imageUrl = imageUrl;
    }
}
