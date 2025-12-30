package com.example.foodcalu.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.foodcalu.data.entity.UserProfile;
import com.example.foodcalu.repository.AppRepository;

public class ProfileViewModel extends AndroidViewModel {
    private final AppRepository repository;
    private final MutableLiveData<Long> userIdLiveData = new MutableLiveData<>();
    private final LiveData<UserProfile> profileLiveData;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
        profileLiveData = Transformations.switchMap(userIdLiveData, repository::getUserProfile);
    }

    public void setUserId(long userId) {
        userIdLiveData.setValue(userId);
    }

    public LiveData<UserProfile> getUserProfile() {
        return profileLiveData;
    }

    public void saveProfile(UserProfile profile) {
        repository.saveUserProfile(profile);
    }
}
