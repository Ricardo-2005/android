package com.example.foodcalu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodcalu.data.entity.WorkoutItem;

import java.util.List;

@Dao
public interface WorkoutItemDao {
    @Query("SELECT * FROM workout_item WHERE categoryId = :categoryId ORDER BY name")
    LiveData<List<WorkoutItem>> getItemsByCategory(long categoryId);

    @Query("SELECT * FROM workout_item ORDER BY name")
    LiveData<List<WorkoutItem>> getAllItems();

    @Query("SELECT * FROM workout_item WHERE id = :id LIMIT 1")
    LiveData<WorkoutItem> getItemById(long id);

    @Query("SELECT COUNT(*) FROM workout_item")
    int getCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WorkoutItem> items);
}
