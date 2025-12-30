package com.example.foodcalu.data.model;

import androidx.room.ColumnInfo;

public class WorkoutDuration {
    @ColumnInfo(name = "date")
    public long date;
    @ColumnInfo(name = "totalDuration")
    public int totalDuration;

    public WorkoutDuration(long date, int totalDuration) {
        this.date = date;
        this.totalDuration = totalDuration;
    }
}
