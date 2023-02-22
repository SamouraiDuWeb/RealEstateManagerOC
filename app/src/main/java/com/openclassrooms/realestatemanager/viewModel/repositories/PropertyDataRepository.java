package com.openclassrooms.realestatemanager.viewModel.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.PropertyDao;
import com.openclassrooms.realestatemanager.models.Property;

import java.util.List;

public class PropertyDataRepository {

    private final PropertyDao propertyDao;

    public PropertyDataRepository(PropertyDao propertyDao) {
        this.propertyDao = propertyDao;
    }

    //Create property
    public void createProperty(Property property) {
        propertyDao.createProperty(property);
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
            String category, float price, float surface, String  address,
            int nbRooms, int nbBathRooms, int nbBedRooms,String  description,String  status,String agentName,
            boolean school, boolean business, boolean park, boolean publicTransport, String dateOfEntry, String dateSold
    ) {
        propertyDao.updateProperty(category, price,surface, address,
                nbRooms, nbBathRooms, nbBedRooms, description, status, agentName,
                school, business, park, publicTransport, dateOfEntry, dateSold);
    }
}
