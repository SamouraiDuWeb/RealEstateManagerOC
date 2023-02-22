package com.openclassrooms.realestatemanager.viewModel.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.PropertyDao;
import com.openclassrooms.realestatemanager.database.dao.UserDao;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.User;

public class UserDataRepository {

    private final UserDao userDao;

    public UserDataRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    //Create user
    public void createUser(User user) {
        userDao.createUser(user);
    }

    //Get property
    public LiveData<User> getUser(long userId) {
        return this.userDao.getUser(userId);
    }

    //Update property
    public void updateUser(String name, String email, String password) {
        userDao.updateUser(name, email, email);
    }
}
