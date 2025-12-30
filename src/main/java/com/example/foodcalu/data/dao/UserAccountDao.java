package com.example.foodcalu.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodcalu.data.entity.UserAccount;

@Dao
public interface UserAccountDao {
    @Query("SELECT * FROM user_account WHERE username = :username LIMIT 1")
    UserAccount getByUsername(String username);

    @Query("SELECT * FROM user_account WHERE username = :username AND password = :password LIMIT 1")
    UserAccount login(String username, String password);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(UserAccount account);
}
