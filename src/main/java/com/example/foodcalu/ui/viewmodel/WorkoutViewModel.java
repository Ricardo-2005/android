package com.example.foodcalu.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.foodcalu.data.entity.UserProfile;
import com.example.foodcalu.data.entity.WorkoutCategory;
import com.example.foodcalu.data.entity.WorkoutItem;
import com.example.foodcalu.data.model.CategoryStat;
import com.example.foodcalu.data.model.WorkoutDuration;
import com.example.foodcalu.repository.AppRepository;
import com.example.foodcalu.util.DateUtils;
import com.example.foodcalu.util.RecommendationEngine;
import com.example.foodcalu.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class WorkoutViewModel extends AndroidViewModel {
    private final AppRepository repository;
    private final MediatorLiveData<List<WorkoutItem>> recommended = new MediatorLiveData<>();

    private UserProfile cachedProfile;
    private List<WorkoutCategory> cachedCategories = new ArrayList<>();
    private List<WorkoutItem> cachedItems = new ArrayList<>();

    public WorkoutViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);

        recommended.setValue(new ArrayList<>());

        long userId = SessionManager.getUserId(getApplication());
        LiveData<UserProfile> profileLive = userId <= 0L
                ? new androidx.lifecycle.MutableLiveData<>(null)
                : repository.getUserProfile(userId);
        LiveData<List<WorkoutCategory>> categoryLive = repository.getWorkoutCategories();
        LiveData<List<WorkoutItem>> itemsLive = repository.getAllWorkoutItems();

        recommended.addSource(profileLive, profile -> {
            cachedProfile = profile;
            updateRecommendations();
        });
        recommended.addSource(categoryLive, categories -> {
            cachedCategories = categories == null ? new ArrayList<>() : categories;
            updateRecommendations();
        });
        recommended.addSource(itemsLive, items -> {
            cachedItems = items == null ? new ArrayList<>() : items;
            updateRecommendations();
        });
    }

    private void updateRecommendations() {
        recommended.setValue(RecommendationEngine.recommend(cachedProfile, cachedCategories, cachedItems));
    }

    public LiveData<List<WorkoutItem>> getRecommended() {
        return recommended;
    }

    public LiveData<List<WorkoutCategory>> getCategories() {
        return repository.getWorkoutCategories();
    }

    public LiveData<List<WorkoutDuration>> getWeekDurations() {
        long end = DateUtils.today();
        long start = DateUtils.addDays(end, -6);
        return repository.getWorkoutDurationBetween(start, end);
    }

    public LiveData<List<CategoryStat>> getWeekCategoryStats() {
        long end = DateUtils.today();
        long start = DateUtils.addDays(end, -6);
        return repository.getCategoryStatsBetween(start, end);
    }

    public LiveData<Integer> getWeekWorkoutCount() {
        long end = DateUtils.today();
        long start = DateUtils.addDays(end, -6);
        return repository.getWorkoutCountBetween(start, end);
    }

    public LiveData<Integer> getWeekWorkoutCalories() {
        long end = DateUtils.today();
        long start = DateUtils.addDays(end, -6);
        return repository.getWorkoutCaloriesBetween(start, end);
    }
}
