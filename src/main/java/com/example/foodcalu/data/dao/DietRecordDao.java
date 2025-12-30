package com.example.foodcalu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodcalu.data.entity.DietRecord;

import java.util.List;

@Dao
public interface DietRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DietRecord> records);

    @Query("SELECT * FROM diet_record WHERE date = :date ORDER BY id DESC")
    LiveData<List<DietRecord>> getRecordsByDate(long date);

    @Query("SELECT IFNULL(SUM(estimated_calories), 0) FROM diet_record WHERE date = :date")
    LiveData<Float> getTotalCaloriesByDate(long date);
}