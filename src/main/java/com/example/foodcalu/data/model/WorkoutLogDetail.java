package com.example.foodcalu.data.model;

import androidx.room.ColumnInfo;

public class WorkoutLogDetail {
    @ColumnInfo(name = "id")
    public long id;
    @ColumnInfo(name = "date")
    public long date;
    @ColumnInfo(name = "durationMin")
    public int durationMin;
    @ColumnInfo(name = "completed")
    public boolean completed;
    @ColumnInfo(name = "note")
    public String note;
    @ColumnInfo(name = "workoutItemId")
    public long workoutItemId;
    @ColumnInfo(name = "itemName")
    public String itemName;
    @ColumnInfo(name = "categoryName")
    public String categoryName;
    @ColumnInfo(name = "kcalBurnedEstimate")
    public int kcalBurnedEstimate;

    public WorkoutLogDetail(long id, long date, int durationMin, boolean completed, String note,
                            long workoutItemId, String itemName, String categoryName, int kcalBurnedEstimate) {
        this.id = id;
        this.date = date;
        this.durationMin = durationMin;
        this.completed = completed;
        this.note = note;
        this.workoutItemId = workoutItemId;
        this.itemName = itemName;
        this.categoryName = categoryName;
        this.kcalBurnedEstimate = kcalBurnedEstimate;
    }
}
