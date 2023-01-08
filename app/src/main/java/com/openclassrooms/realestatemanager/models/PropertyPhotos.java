package com.openclassrooms.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Entity(indices = {@Index("propertyId")},
        foreignKeys = @ForeignKey(entity = Property.class,
                parentColumns = "id",
                childColumns = "propertyId"))

public class PropertyPhotos {

    @PrimaryKey(autoGenerate = true)
    private long photosId;

    private long propertyId;

    private String photoDescription;

    private String photoUrl;

    public PropertyPhotos() {
    }

    public PropertyPhotos(long photosId, long propertyId, String photoDescription, String photoUrl) {
        this.photosId = photosId;
        this.propertyId = propertyId;
        this.photoDescription = photoDescription;
        this.photoUrl = photoUrl;
    }

    public long getPhotosId() {
        return photosId;
    }

    public void setPhotosId(long photosId) {
        this.photosId = photosId;
    }

    public String getPhotoDescription() {
        return photoDescription;
    }

    public void setPhotoDescription(String photoDescription) {
        this.photoDescription = photoDescription;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }
}
