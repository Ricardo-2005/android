package com.example.foodcalu.data.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_account", indices = {@Index(value = "username", unique = true)})
public class UserAccount {
    @PrimaryKey(autoGenerate = true)
    public long userId;

    public String username;
    public String password;
    public long createTime;

    public UserAccount(long userId, String username, String password, long createTime) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.createTime = createTime;
    }
}
