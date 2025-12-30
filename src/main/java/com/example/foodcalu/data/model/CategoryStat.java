package com.example.foodcalu.data.model;

import androidx.room.ColumnInfo;

public class CategoryStat {
    @ColumnInfo(name = "categoryName")
    public String categoryName;
    @ColumnInfo(name = "totalDuration")
    public int totalDuration;

    public CategoryStat(String categoryName, int totalDuration) {
        this.categoryName = categoryName;
        this.totalDuration = totalDuration;
    }
}
