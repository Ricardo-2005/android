package com.example.foodcalu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodcalu.data.entity.WorkoutLog;
import com.example.foodcalu.data.model.CategoryStat;
import com.example.foodcalu.data.model.WorkoutDuration;
import com.example.foodcalu.data.model.WorkoutLogDetail;

import java.util.List;

@Dao
public interface WorkoutLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WorkoutLog log);

    @Query("SELECT wl.id, wl.date, wl.durationMin, wl.completed, wl.note, wl.workoutItemId, wi.name AS itemName, wc.name AS categoryName, wl.kcalBurnedEstimate " +
            "FROM workout_log wl INNER JOIN workout_item wi ON wl.workoutItemId = wi.id " +
            "INNER JOIN workout_category wc ON wi.categoryId = wc.id " +
            "WHERE wl.date = :date ORDER BY wl.id DESC")
    LiveData<List<WorkoutLogDetail>> getLogsForDate(long date);

    @Query("SELECT IFNULL(SUM(durationMin), 0) FROM workout_log WHERE date = :date")
    LiveData<Integer> getDailyDuration(long date);

    @Query("SELECT COUNT(*) FROM workout_log WHERE date = :date AND completed = 1")
    LiveData<Integer> getDailyCompletedCount(long date);

    @Query("SELECT date, IFNULL(SUM(durationMin), 0) AS totalDuration FROM workout_log " +
            "WHERE date BETWEEN :start AND :end GROUP BY date ORDER BY date")
    LiveData<List<WorkoutDuration>> getDailyDurationBetween(long start, long end);

    @Query("SELECT wc.name AS categoryName, IFNULL(SUM(wl.durationMin), 0) AS totalDuration " +
            "FROM workout_log wl INNER JOIN workout_item wi ON wl.workoutItemId = wi.id " +
            "INNER JOIN workout_category wc ON wi.categoryId = wc.id " +
            "WHERE wl.date BETWEEN :start AND :end GROUP BY wc.name")
    LiveData<List<CategoryStat>> getCategoryStatsBetween(long start, long end);

    @Query("SELECT COUNT(*) FROM workout_log WHERE date BETWEEN :start AND :end")
    LiveData<Integer> getWorkoutCountBetween(long start, long end);

    @Query("SELECT IFNULL(SUM(kcalBurnedEstimate), 0) FROM workout_log WHERE date BETWEEN :start AND :end")
    LiveData<Integer> getWorkoutCaloriesBetween(long start, long end);

    @Query("DELETE FROM workout_log")
    void deleteAll();
}
