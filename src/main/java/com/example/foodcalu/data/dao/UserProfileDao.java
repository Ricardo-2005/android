package com.example.foodcalu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodcalu.data.entity.UserProfile;

@Dao
public interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE userId = :userId LIMIT 1")
    LiveData<UserProfile> getProfile(long userId);

    @Query("SELECT * FROM user_profile WHERE userId = :userId LIMIT 1")
    UserProfile getProfileSync(long userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserProfile profile);

    @Update
    void update(UserProfile profile);
}
