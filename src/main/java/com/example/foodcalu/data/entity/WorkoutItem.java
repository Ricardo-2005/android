package com.example.foodcalu.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "workout_item",
        foreignKeys = @ForeignKey(
                entity = WorkoutCategory.class,
                parentColumns = "id",
                childColumns = "categoryId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("categoryId")}
)
public class WorkoutItem {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public long categoryId;
    public String name;
    public String difficulty;
    public String targetMuscle;
    public String suggestion;
    public int kcalEstimate;
    public String videoUrl;
    public String coverUrl;

    public WorkoutItem(long id, long categoryId, String name, String difficulty, String targetMuscle,
                       String suggestion, int kcalEstimate, String videoUrl, String coverUrl) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.difficulty = difficulty;
        this.targetMuscle = targetMuscle;
        this.suggestion = suggestion;
        this.kcalEstimate = kcalEstimate;
        this.videoUrl = videoUrl;
        this.coverUrl = coverUrl;
    }
}
