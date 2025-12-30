package com.example.foodcalu.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.foodcalu.data.entity.Food;
import com.example.foodcalu.repository.AppRepository;

import java.util.List;

public class AddMealViewModel extends AndroidViewModel {
    private final AppRepository repository;

    public AddMealViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
    }

    public LiveData<List<Food>> getFoods() {
        return repository.getFoods();
    }
}
