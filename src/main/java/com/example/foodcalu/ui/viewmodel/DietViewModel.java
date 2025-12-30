package com.example.foodcalu.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.foodcalu.data.entity.DietRecord;
import com.example.foodcalu.repository.DietRepository;

import java.util.List;

public class DietViewModel extends AndroidViewModel {
    private final DietRepository repository;
    private final MutableLiveData<Long> selectedDate = new MutableLiveData<>();
    private final LiveData<List<DietRecord>> records;
    private final LiveData<Float> totalCalories;

    public DietViewModel(@NonNull Application application) {
        super(application);
        repository = new DietRepository(application);
        records = Transformations.switchMap(selectedDate, repository::getRecordsByDate);
        totalCalories = Transformations.switchMap(selectedDate, repository::getTotalCaloriesByDate);
    }

    public void setDate(long date) {
        selectedDate.setValue(date);
    }

    public LiveData<List<DietRecord>> getRecords() {
        return records;
    }

    public LiveData<Float> getTotalCalories() {
        return totalCalories;
    }

    public void insertRecords(List<DietRecord> records) {
        repository.insertRecords(records);
    }
}
