package com.openclassrooms.realestatemanager.viewModel.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.PropertyDao;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyAddress;
import com.openclassrooms.realestatemanager.models.PropertyPhotos;
import com.openclassrooms.realestatemanager.models.User;

import java.util.ArrayList;
import java.util.Date;
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

    //Get property
    public LiveData<Property> getProperty(long propertyId) {
        return this.propertyDao.getProperty(propertyId);
    }

    //Get all
    public LiveData<List<Property>> getAll() {
        return this.propertyDao.getAll();
    }

    //Update illustration for description picture
    public void updatePropertyPhotos(List<String> gallery, long id) {
        propertyDao.updateGallery(gallery, id);
    }

    //Update property
    public void updateProperty(
            long id,
            String category,
            float price,
            float surface,
            int nbRooms,
            int nbBathrooms,
            int nbBedrooms,
            String description,
            String address,
            String agentName,
            boolean school,
            boolean business,
            boolean park,
            boolean publicTransport
    ) {
        propertyDao.updateProperty(category, price,
        surface, nbRooms, nbBathrooms,
        nbBedrooms, school, business,
        publicTransport, park, address,
                description, id, agentName);
    }
}
