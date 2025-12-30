package com.example.foodcalu.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.foodcalu.data.entity.Food;
import com.example.foodcalu.data.model.DailyKcal;
import com.example.foodcalu.data.model.MealRecordDetail;
import com.example.foodcalu.repository.AppRepository;
import com.example.foodcalu.util.DateUtils;

import java.util.List;

public class FoodViewModel extends AndroidViewModel {
    private static class RangeParams {
        final long start;
        final long end;

        RangeParams(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }

    private final AppRepository repository;
    private final MutableLiveData<Long> selectedDate = new MutableLiveData<>();
    private final MutableLiveData<Integer> rangeDays = new MutableLiveData<>();
    private final MediatorLiveData<RangeParams> rangeParams = new MediatorLiveData<>();
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>();
    private final LiveData<List<Food>> foodsLive;

    public FoodViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
        selectedDate.setValue(DateUtils.today());
        rangeDays.setValue(7);
        searchQuery.setValue("");
        rangeParams.addSource(selectedDate, value -> updateRangeParams());
        rangeParams.addSource(rangeDays, value -> updateRangeParams());
        updateRangeParams();

        foodsLive = Transformations.switchMap(searchQuery, query -> {
            if (query == null || query.trim().isEmpty()) {
                return repository.getFoods();
            }
            return repository.searchFoods("%" + query.trim() + "%");
        });
    }

    public LiveData<List<Food>> getFoods() {
        return foodsLive;
    }

    public LiveData<List<MealRecordDetail>> getMealsForDate() {
        return Transformations.switchMap(selectedDate, repository::getMealsForDate);
    }

    public LiveData<Float> getDailyKcal() {
        return Transformations.switchMap(selectedDate, repository::getDailyKcal);
    }

    public LiveData<List<DailyKcal>> getTrendData() {
        return Transformations.switchMap(rangeParams, p -> repository.getDailyKcalBetween(p.start, p.end));
    }

    public void setSelectedDate(long date) {
        selectedDate.setValue(date);
    }

    public LiveData<Long> getSelectedDate() {
        return selectedDate;
    }

    public void setRangeDays(int days) {
        rangeDays.setValue(days);
    }

    public LiveData<Integer> getRangeDays() {
        return rangeDays;
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    private void updateRangeParams() {
        long end = selectedDate.getValue() == null ? DateUtils.today() : selectedDate.getValue();
        int days = rangeDays.getValue() == null ? 7 : rangeDays.getValue();
        long start = DateUtils.addDays(end, -(days - 1));
        rangeParams.setValue(new RangeParams(start, end));
    }
}
