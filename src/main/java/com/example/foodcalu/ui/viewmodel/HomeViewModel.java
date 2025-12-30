package com.example.foodcalu.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.foodcalu.data.entity.UserProfile;
import com.example.foodcalu.repository.AppRepository;
import com.example.foodcalu.repository.DietRepository;
import com.example.foodcalu.util.DateUtils;
import com.example.foodcalu.util.SessionManager;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final AppRepository repository;
    private final DietRepository dietRepository;
    private final long today;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
        dietRepository = new DietRepository(application);
        today = DateUtils.today();
    }

    public LiveData<UserProfile> getUserProfile() {
        long userId = SessionManager.getUserId(getApplication());
        if (userId <= 0L) {
            return new androidx.lifecycle.MutableLiveData<>(null);
        }
        return repository.getUserProfile(userId);
    }

    public LiveData<Float> getTodayKcal() {
        return dietRepository.getTotalCaloriesByDate(today);
    }

    public LiveData<Integer> getTodayWorkoutCount() {
        return repository.getDailyCompletedWorkouts(today);
    }

    public LiveData<Integer> getTodayWorkoutDuration() {
        return repository.getDailyWorkoutDuration(today);
    }

}
