package com.openclassrooms.realestatemanager.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyAddress;
import com.openclassrooms.realestatemanager.models.PropertyPhotos;
import com.openclassrooms.realestatemanager.models.User;

import java.util.List;

@Dao
public interface PropertyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createProperty(Property property);

    @Query("SELECT * FROM Property WHERE id = :propertyId")
    LiveData<Property> getProperty(long propertyId);

    @Query("SELECT * FROM Property WHERE id = :propertyId")
    Cursor getPropertyWithCursor(long propertyId);

    @Query("SELECT * FROM Property")
    LiveData<List<Property>> getAll();

    @Query("UPDATE Property SET category = :category," +
            "price = :price," +
            "surface = :surface," +
            "nbRooms = :numberOfRooms," +
            "nbBathrooms= :numberOfBathrooms," +
            "nbBedrooms = :numberOfBedRooms," +
            "school = :school," +
            "business = :business," +
            "publicTransport = :publicTransport," +
            "park = :park," +
            "description = :description," +
            "address = :address," +
            "agentName = :agentName")
    int updateProperty(
            String category, float price,
                       float surface, int numberOfRooms, int numberOfBathrooms,
                       int numberOfBedRooms, boolean school, boolean business,
                       boolean publicTransport, boolean park, String address,
                       String description, String agentName);

    @Query("UPDATE PropertyPhotos SET photoUrl = :photos WHERE photosId =:id")
    int updateGallery(List<String> photos, long id);

    @Query("DELETE FROM Property WHERE id = :propertyId")
    int deleteProperty(long propertyId);

}
