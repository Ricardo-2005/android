package com.example.foodcalu.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workout_category")
public class WorkoutCategory {
    @PrimaryKey
    public long id;

    public String name;
    public String desc;

    public WorkoutCategory(long id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }
}
