package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createUser(User user);

    @Query("SELECT * FROM User WHERE userId = :userId")
    LiveData<User> getUser(long userId);

    @Query("SELECT * FROM User")
    LiveData<List<User>> getAll();

    @Query("UPDATE User SET name = :name, " +
                            "email = :email, " +
                            "password = :password")
    int updateUser(String name, String email, String password);
}
