package com.openclassrooms.realestatemanager.injection;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.viewModel.RealEstateManagerViewModel;
import com.openclassrooms.realestatemanager.viewModel.repositories.PropertyDataRepository;
import com.openclassrooms.realestatemanager.viewModel.repositories.PropertyPhotosDataRepository;
import com.openclassrooms.realestatemanager.viewModel.repositories.UserDataRepository;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final PropertyDataRepository propertyDataSource;
    private final PropertyPhotosDataRepository propertyPhotosDataSource;
    private final UserDataRepository userDataSource;
    private final Executor executor;

    public ViewModelFactory(PropertyDataRepository propertyDataSource, PropertyPhotosDataRepository propertyPhotosDataSource, UserDataRepository userDataSource, Executor executor) {
        this.propertyDataSource = propertyDataSource;
        this.propertyPhotosDataSource = propertyPhotosDataSource;
        this.userDataSource = userDataSource;
        this.executor = executor;
    }

    @androidx.annotation.NonNull
    @Override
    public <T extends ViewModel> T create(@androidx.annotation.NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RealEstateManagerViewModel.class)) {

            return (T) new RealEstateManagerViewModel(propertyDataSource, propertyPhotosDataSource, userDataSource, executor);
        }
        throw new IllegalArgumentException("Unknow ViewModel class");
    }
}
