package com.example.foodcalu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodcalu.data.entity.WorkoutCategory;

import java.util.List;

@Dao
public interface WorkoutCategoryDao {
    @Query("SELECT * FROM workout_category ORDER BY id")
    LiveData<List<WorkoutCategory>> getAllCategories();

    @Query("SELECT * FROM workout_category WHERE id = :id LIMIT 1")
    WorkoutCategory getByIdSync(long id);

    @Query("SELECT COUNT(*) FROM workout_category")
    int getCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WorkoutCategory> categories);
}
