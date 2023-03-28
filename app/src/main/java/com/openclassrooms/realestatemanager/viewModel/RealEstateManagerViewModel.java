package com.openclassrooms.realestatemanager.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyPhotos;
import com.openclassrooms.realestatemanager.models.User;
import com.openclassrooms.realestatemanager.viewModel.repositories.PropertyDataRepository;
import com.openclassrooms.realestatemanager.viewModel.repositories.PropertyPhotosDataRepository;
import com.openclassrooms.realestatemanager.viewModel.repositories.UserDataRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

public class RealEstateManagerViewModel extends ViewModel {

    //Repositories
    private final PropertyDataRepository propertyDataSource;
    private final PropertyPhotosDataRepository propertyPhotosDataSource;
    private final UserDataRepository userDataSource;
    private final Executor executor;

    //Data
    private LiveData<Property> currentProperty;

    public RealEstateManagerViewModel(PropertyDataRepository propertyDataSource, PropertyPhotosDataRepository propertyPhotosDataSource, UserDataRepository userDataSource, Executor executor) {
        this.propertyDataSource = propertyDataSource;
        this.propertyPhotosDataSource = propertyPhotosDataSource;
        this.userDataSource = userDataSource;
        this.executor = executor;
    }

    public void init(long propertyId) {
        if (this.currentProperty != null) {
            return;
        }
        currentProperty = propertyDataSource.getProperty(propertyId);
    }

    //For property
    public long createProperty(Property property) {
        AtomicLong id = new AtomicLong();
        executor.execute(() -> {
           id.set(propertyDataSource.createProperty(property));
        });
        return id.get();
    }

    public void deleteAllProperties() {
        executor.execute(() -> {
            propertyDataSource.deleteAllproperties();
        });
    }

    public void updateProperty(String category, float price, float surface, String address,
                               int nbRooms, int nbBathRooms, int nbBedRooms, String description, String status, String agentName,
                               boolean school, boolean business, boolean park, boolean publicTransport, String dateOfEntry, String dateSold, long id) {
        executor.execute(() -> {
            propertyDataSource.updateProperty(category, price, surface, address,
                    nbRooms, nbBathRooms, nbBedRooms, description, status, agentName,
                    school, business, park, publicTransport, dateOfEntry, dateSold, id);
        });
    }

    public void deleteProperty(long propertyId) {
        executor.execute(() -> {
            propertyDataSource.deleteProperty(propertyId);
        });
    }

    public LiveData<Property> getProperty(long propertyId) {
        return propertyDataSource.getProperty(propertyId);
    }

    public LiveData<List<Property>> getAll() {
        return propertyDataSource.getAll();
    }

    //For user
    public void createUser(User user) {
        executor.execute(() -> {
            userDataSource.createUser(user);
        });
    }

    //For propertyPhoto
    public void createPropertyPhoto(PropertyPhotos propertyPhotos) {
        executor.execute(() -> {
            propertyPhotosDataSource.createPropertyPhoto(propertyPhotos);
            System.out.println("/// photo ajoutée !");
        });
    }

    public LiveData<List<PropertyPhotos>> getGallery(long propertyId) {
        return propertyPhotosDataSource.getGallery(propertyId);
    }

    public LiveData<List<PropertyPhotos>> getAllGallery() {
        return propertyPhotosDataSource.getAllGallery();
    }

    public void deletePhotoProperty(long id) {
        executor.execute(() -> {
        propertyPhotosDataSource.deletePhotoProperty(id);
        });
    }

    public LiveData<List<Property>> getSearchedProperty(
                                                        int miniPrice,
                                                        int maxiPrice,
                                                        int miniSurface,
                                                        int maxiSurface,
                                                        int miniRoom,
                                                        int maxiRoom,
                                                        boolean school,
                                                        boolean business,
                                                        boolean publicTransport,
                                                        boolean park) {
        return propertyDataSource.getSearchedProperty(
                    miniPrice,
                    maxiPrice,
                    miniSurface,
                    maxiSurface,
                    miniRoom,
                    maxiRoom,
                    school,
                    business,
                    publicTransport,
                    park);
    }

    public void insertPropertyAndPhotos(Property property, List<PropertyPhotos> photos) {
        long propertyId = property.getId();
        for (PropertyPhotos photo : photos) {
            photo.setPropertyId(propertyId);
        }
        propertyDataSource.insertPropertyAndPhotos(property, photos);
    }
}
