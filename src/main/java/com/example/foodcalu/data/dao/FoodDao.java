package com.example.foodcalu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodcalu.data.entity.Food;

import java.util.List;

@Dao
public interface FoodDao {
    @Query("SELECT * FROM food ORDER BY name")
    LiveData<List<Food>> getAllFoods();

    @Query("SELECT * FROM food WHERE name LIKE :keyword ORDER BY name")
    LiveData<List<Food>> searchFoods(String keyword);

    @Query("SELECT * FROM food WHERE id = :id LIMIT 1")
    LiveData<Food> getFoodById(long id);

    @Query("SELECT COUNT(*) FROM food")
    int getCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Food> foods);
}
