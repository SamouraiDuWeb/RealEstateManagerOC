package com.openclassrooms.realestatemanager.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.viewModel.repositories.PropertyDataRepository;
import com.openclassrooms.realestatemanager.viewModel.repositories.PropertyPhotosDataRepository;

import java.util.concurrent.Executor;

public class RealEstateManagerViewModel extends ViewModel {

    //Repositories
    private final PropertyDataRepository propertyDataSource;
    private final PropertyPhotosDataRepository propertyPhotosDataSource;
    private final Executor executor;

    //Data
    private LiveData<Property> currentProperty;

    public RealEstateManagerViewModel(PropertyDataRepository propertyDataSource, PropertyPhotosDataRepository propertyPhotosDataSource, Executor executor) {
        this.propertyDataSource = propertyDataSource;
        this.propertyPhotosDataSource = propertyPhotosDataSource;
        this.executor = executor;
    }

    public void init(long propertyId) {
        if (this.currentProperty != null) {
            return;
        }
        currentProperty = propertyDataSource.getProperty(propertyId);
    }

    //For property
    public void createProperty(Property property) {
        executor.execute(() -> {
            propertyDataSource.createProperty(property);
        });
    }
}
