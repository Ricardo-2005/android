package com.example.foodcalu.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foodcalu.data.dao.UserAccountDao;
import com.example.foodcalu.data.dao.UserProfileDao;
import com.example.foodcalu.data.dao.WorkoutCategoryDao;
import com.example.foodcalu.data.dao.WorkoutItemDao;
import com.example.foodcalu.data.dao.WorkoutLogDao;
import com.example.foodcalu.data.db.AppDatabase;
import com.example.foodcalu.data.entity.Food;
import com.example.foodcalu.data.entity.MealRecord;
import com.example.foodcalu.data.entity.UserAccount;
import com.example.foodcalu.data.entity.UserProfile;
import com.example.foodcalu.data.entity.WorkoutCategory;
import com.example.foodcalu.data.entity.WorkoutItem;
import com.example.foodcalu.data.entity.WorkoutLog;
import com.example.foodcalu.data.model.CategoryStat;
import com.example.foodcalu.data.model.DailyKcal;
import com.example.foodcalu.data.model.MealRecordDetail;
import com.example.foodcalu.data.model.WorkoutDuration;
import com.example.foodcalu.data.model.WorkoutLogDetail;

import java.util.Collections;
import java.util.List;

public class AppRepository {
    private static volatile AppRepository INSTANCE;

    private final UserProfileDao userProfileDao;
    private final UserAccountDao userAccountDao;
    private final WorkoutCategoryDao workoutCategoryDao;
    private final WorkoutItemDao workoutItemDao;
    private final WorkoutLogDao workoutLogDao;

    private AppRepository(AppDatabase db) {
        userProfileDao = db.userProfileDao();
        userAccountDao = db.userAccountDao();
        workoutCategoryDao = db.workoutCategoryDao();
        workoutItemDao = db.workoutItemDao();
        workoutLogDao = db.workoutLogDao();
    }

    public static AppRepository getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppRepository(AppDatabase.getInstance(context));
                }
            }
        }
        return INSTANCE;
    }

    public LiveData<UserProfile> getUserProfile(long userId) {
        return userProfileDao.getProfile(userId);
    }

    public UserAccount getAccountByUsernameSync(String username) {
        return userAccountDao.getByUsername(username);
    }

    public UserAccount loginSync(String username, String password) {
        return userAccountDao.login(username, password);
    }

    public long insertAccountSync(UserAccount account) {
        return userAccountDao.insert(account);
    }

    public void saveUserProfile(UserProfile profile) {
        AppDatabase.DB_EXECUTOR.execute(() -> userProfileDao.insert(profile));
    }

    public LiveData<List<Food>> getFoods() {
        return new MutableLiveData<>(Collections.emptyList());
    }

    public LiveData<List<Food>> searchFoods(String keyword) {
        return new MutableLiveData<>(Collections.emptyList());
    }

    public LiveData<Food> getFoodById(long id) {
        return new MutableLiveData<>();
    }

    public void addMealRecord(MealRecord record) {
    }

    public LiveData<List<MealRecordDetail>> getMealsForDate(long date) {
        return new MutableLiveData<>(Collections.emptyList());
    }

    public LiveData<Float> getDailyKcal(long date) {
        return new MutableLiveData<>(0f);
    }

    public LiveData<List<DailyKcal>> getDailyKcalBetween(long start, long end) {
        return new MutableLiveData<>(Collections.emptyList());
    }

    public LiveData<List<WorkoutCategory>> getWorkoutCategories() {
        return workoutCategoryDao.getAllCategories();
    }

    public LiveData<List<WorkoutItem>> getAllWorkoutItems() {
        return workoutItemDao.getAllItems();
    }

    public LiveData<WorkoutItem> getWorkoutItemById(long id) {
        return workoutItemDao.getItemById(id);
    }

    public LiveData<List<WorkoutItem>> getWorkoutItemsByCategory(long categoryId) {
        return workoutItemDao.getItemsByCategory(categoryId);
    }

    public void addWorkoutLog(WorkoutLog log) {
        AppDatabase.DB_EXECUTOR.execute(() -> workoutLogDao.insert(log));
    }

    public LiveData<List<WorkoutLogDetail>> getWorkoutLogsForDate(long date) {
        return workoutLogDao.getLogsForDate(date);
    }

    public LiveData<Integer> getDailyWorkoutDuration(long date) {
        return workoutLogDao.getDailyDuration(date);
    }

    public LiveData<Integer> getDailyCompletedWorkouts(long date) {
        return workoutLogDao.getDailyCompletedCount(date);
    }

    public LiveData<List<WorkoutDuration>> getWorkoutDurationBetween(long start, long end) {
        return workoutLogDao.getDailyDurationBetween(start, end);
    }

    public LiveData<List<CategoryStat>> getCategoryStatsBetween(long start, long end) {
        return workoutLogDao.getCategoryStatsBetween(start, end);
    }

    public LiveData<Integer> getWorkoutCountBetween(long start, long end) {
        return workoutLogDao.getWorkoutCountBetween(start, end);
    }

    public LiveData<Integer> getWorkoutCaloriesBetween(long start, long end) {
        return workoutLogDao.getWorkoutCaloriesBetween(start, end);
    }
}
