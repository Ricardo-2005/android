package com.example.foodcalu.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "diet_record")
public class DietRecord {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public long date;

    @NonNull
    @ColumnInfo(name = "raw_input")
    public String rawInput;

    @NonNull
    @ColumnInfo(name = "food_name")
    public String foodName;

    @ColumnInfo(name = "estimated_weight_g")
    public float estimatedWeightG;

    @ColumnInfo(name = "estimated_calories")
    public float estimatedCalories;

    public DietRecord(long id,
                      long date,
                      @NonNull String rawInput,
                      @NonNull String foodName,
                      float estimatedWeightG,
                      float estimatedCalories) {
        this.id = id;
        this.date = date;
        this.rawInput = rawInput;
        this.foodName = foodName;
        this.estimatedWeightG = estimatedWeightG;
        this.estimatedCalories = estimatedCalories;
    }
}
