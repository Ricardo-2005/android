package com.example.foodcalu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodcalu.data.entity.MealRecord;
import com.example.foodcalu.data.model.DailyKcal;
import com.example.foodcalu.data.model.MealRecordDetail;

import java.util.List;

@Dao
public interface MealRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MealRecord record);

    @Query("SELECT mr.id, mr.date, mr.mealType, mr.foodId, f.name AS foodName, f.unit AS foodUnit, " +
            "mr.grams, mr.kcal, mr.unit, mr.estimatedWeight, mr.estimatedCalories, f.tags " +
            "FROM meal_record mr INNER JOIN food f ON mr.foodId = f.id " +
            "WHERE mr.date = :date ORDER BY mr.createdAt DESC")
    LiveData<List<MealRecordDetail>> getMealsForDate(long date);

    @Query("SELECT IFNULL(SUM(kcal), 0) FROM meal_record WHERE date = :date")
    LiveData<Float> getDailyKcal(long date);

    @Query("SELECT date, IFNULL(SUM(kcal), 0) AS totalKcal FROM meal_record " +
            "WHERE date BETWEEN :start AND :end GROUP BY date ORDER BY date")
    LiveData<List<DailyKcal>> getDailyTotalsBetween(long start, long end);

    @Query("DELETE FROM meal_record")
    void deleteAll();
}
