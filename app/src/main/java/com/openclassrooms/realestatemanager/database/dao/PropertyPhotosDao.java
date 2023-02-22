package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.PropertyPhotos;

import java.util.List;

@Dao
public interface PropertyPhotosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createPropertyPhoto(PropertyPhotos photos);

    @Query("SELECT * FROM PropertyPhotos WHERE propertyId = :propertyId")
    LiveData<List<PropertyPhotos>> getGallery(long propertyId);

    @Query("SELECT * FROM PropertyPhotos ")
    LiveData<List<PropertyPhotos>> getAllGallery();
}
