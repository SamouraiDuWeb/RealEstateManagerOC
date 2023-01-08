package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.PropertyPhotos;

import java.util.List;

public interface PropertyPhotosDao {

    @Insert
    long createPropertyPhoto(PropertyPhotos photos);

    @Query("SELECT * FROM PropertyPhotos WHERE propertyId = :propertyId")
    LiveData<List<PropertyPhotos>> getGallery(long propertyId);

}
