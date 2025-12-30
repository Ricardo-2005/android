package com.example.foodcalu.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "workout_log",
        foreignKeys = @ForeignKey(
                entity = WorkoutItem.class,
                parentColumns = "id",
                childColumns = "workoutItemId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("workoutItemId"), @Index("date")}
)
public class WorkoutLog {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public long date;
    public long workoutItemId;
    public int durationMin;
    public boolean completed;
    public String note;
    public int kcalBurnedEstimate;

    public WorkoutLog(long id, long date, long workoutItemId, int durationMin, boolean completed,
                      String note, int kcalBurnedEstimate) {
        this.id = id;
        this.date = date;
        this.workoutItemId = workoutItemId;
        this.durationMin = durationMin;
        this.completed = completed;
        this.note = note;
        this.kcalBurnedEstimate = kcalBurnedEstimate;
    }
}
