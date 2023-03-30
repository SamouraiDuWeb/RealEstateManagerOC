package com.openclassrooms.realestatemanager.viewModel.repositories;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.openclassrooms.realestatemanager.database.dao.PropertyDao;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyPhotos;

import java.util.List;

public class PropertyDataRepository {

    private final PropertyDao propertyDao;

    public PropertyDataRepository(PropertyDao propertyDao) {
        this.propertyDao = propertyDao;
    }

    //Create property
    public long createProperty(Property property) {
        return propertyDao.createProperty(property);
    }

    //Create property
    public void deleteAllproperties() {
        propertyDao.deleteAllproperties();
    }

    //Get property
    public LiveData<Property> getProperty(long propertyId) {
        return this.propertyDao.getProperty(propertyId);
    }

    //Get all
    public LiveData<List<Property>> getAll() {
        return this.propertyDao.getAll();
    }

    //Update propertyPhoto for description picture
    public void updatePropertyPhotos(List<String> gallery, long id) {
        propertyDao.updateGallery(gallery, id);
    }

    //Update property
    public void updateProperty(
            String category, float price, float surface, String address,
            int nbRooms, int nbBathRooms, int nbBedRooms, String description, String status, String agentName,
            boolean school, boolean business, boolean park, boolean publicTransport, String dateOfEntry, String dateSold, long id
    ) {
        propertyDao.updateProperty(category, price, surface, address,
                nbRooms, nbBathRooms, nbBedRooms, description, status, agentName,
                school, business, park, publicTransport, dateOfEntry, dateSold, id);
    }

    public void deleteProperty(long propertyId) {
        propertyDao.deleteProperty(propertyId);
    }


    public void insertPropertyAndPhotos(Property property, List<PropertyPhotos> photos) {
        long propertyId = createProperty(property);
        for (PropertyPhotos photo : photos) {
            photo.setPropertyId(propertyId);
        }
        propertyDao.insertPhotos(photos);
    }
}
