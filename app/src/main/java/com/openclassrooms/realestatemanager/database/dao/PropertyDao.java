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

    @Query("SELECT * FROM Property WHERE id = :houseId")
    Cursor getPropertyWithCursor(long houseId);

    @Query("SELECT * FROM Property")
    LiveData<List<Property>> getAll();

    @Query("UPDATE Property SET category = :category," +
            "price = :price," +
            "address = :address," +
            "nbRooms = :numberOfRooms," +
            "nbBathrooms= :numberOfBathrooms," +
            "nbBedrooms = :numberOfBedRooms," +
            "school = :school," +
            "business = :business," +
            "publicTransport = :publicTransport," +
            "park = :park," +
            "description = :description," +
            "status = :available " +
            "WHERE id = :id")
    int updateHouse(String category,
                    int price,
                    PropertyAddress address,
                    int numberOfRooms,
                    int numberOfBathrooms,
                    int numberOfBedRooms,
                    boolean school,
                    boolean business,
                    boolean publicTransport,
                    boolean park,
                    String description,
                    boolean available,
                    long id);

    @Query("UPDATE Property SET photoUrl = :photos WHERE id =:id") // hum je sais pas
    int updateIllustration(PropertyPhotos photos, long id);

    @Query("DELETE FROM Property WHERE id = :houseId")
    int deleteHouse(long houseId);
}
