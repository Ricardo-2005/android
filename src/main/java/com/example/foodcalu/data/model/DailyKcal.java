package com.example.foodcalu.data.model;

import androidx.room.ColumnInfo;

public class DailyKcal {
    @ColumnInfo(name = "date")
    public long date;

    @ColumnInfo(name = "totalKcal")
    public float totalKcal;

    public DailyKcal(long date, float totalKcal) {
        this.date = date;
        this.totalKcal = totalKcal;
    }
}
