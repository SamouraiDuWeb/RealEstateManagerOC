package com.openclassrooms.realestatemanager.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyAddress;
import com.openclassrooms.realestatemanager.models.User;

import java.util.List;

public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createUser(User user);

    @Query("SELECT * FROM User WHERE userId = :userId")
    LiveData<User> getUser(long userId);

    @Query("SELECT * FROM User")
    LiveData<List<Property>> getAll();

    @Query("UPDATE Property SET userId = :userId," +
            "name = :name," +
            "email= :email," +
            "password= :password WHERE userId = :userId")
    int updateHouse(long userId, String name,
                    String email, String password);

    @Query("DELETE FROM User WHERE userId = :userId")
    int deleteHouse(long userId);
}
