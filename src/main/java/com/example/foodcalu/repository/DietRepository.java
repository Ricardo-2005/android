package com.example.foodcalu.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.foodcalu.data.dao.DietRecordDao;
import com.example.foodcalu.data.db.AppDatabase;
import com.example.foodcalu.data.entity.DietRecord;

import java.util.List;

public class DietRepository {
    private final DietRecordDao dietRecordDao;

    public DietRepository(Context context) {
        dietRecordDao = AppDatabase.getInstance(context).dietRecordDao();
    }

    public LiveData<List<DietRecord>> getRecordsByDate(long date) {
        return dietRecordDao.getRecordsByDate(date);
    }

    public LiveData<Float> getTotalCaloriesByDate(long date) {
        return dietRecordDao.getTotalCaloriesByDate(date);
    }

    public void insertRecords(List<DietRecord> records) {
        AppDatabase.DB_EXECUTOR.execute(() -> dietRecordDao.insertAll(records));
    }
}
