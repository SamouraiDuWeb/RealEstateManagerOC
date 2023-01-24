package com.openclassrooms.realestatemanager.viewModel.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.PropertyPhotosDao;
import com.openclassrooms.realestatemanager.models.PropertyPhotos;

import java.util.List;

public class PropertyPhotosDataRepository {

    private final PropertyPhotosDao propertyPhotosDao;

    public PropertyPhotosDataRepository(PropertyPhotosDao propertyPhotosDao) {
        this.propertyPhotosDao = propertyPhotosDao;
    }

    //Create illustration
    public void createIllustration(PropertyPhotos illustration) {
        propertyPhotosDao.createPropertyPhoto(illustration);
    }

    //Get gallery
    public LiveData<List<PropertyPhotos>> getGallery(long propertyId) {
        return this.propertyPhotosDao.getGallery(propertyId);
    }
}
