package com.example.foodcalu.data.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_profile", indices = {@Index(value = "userId", unique = true)})
public class UserProfile {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public long userId;

    public String nickname;
    public String gender;
    public int age;
    public float heightCm;
    public float weightKg;
    public String activityLevel;
    public String goal;
    public long updatedAt;

    public UserProfile(long id, long userId, String nickname, String gender, int age, float heightCm, float weightKg,
                       String activityLevel, String goal, long updatedAt) {
        this.id = id;
        this.userId = userId;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.heightCm = heightCm;
        this.weightKg = weightKg;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.updatedAt = updatedAt;
    }
}
