package com.openclassrooms.realestatemanager.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.Property;

import java.util.List;

@Dao
public interface PropertyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createProperty(Property property);

    @Query("SELECT * FROM Property WHERE id = :propertyId")
    LiveData<Property> getProperty(long propertyId);

    @Query("SELECT * FROM Property")
    LiveData<List<Property>> getAll();

    @Query("UPDATE Property SET category = :category," +
            "price = :price," +
            "surface = :surface," +
            "address = :address," +
            "nbRooms = :nbRooms," +
            "nbBathrooms= :nbBathRooms," +
            "nbBedrooms = :nbBedRooms," +
            "description = :description," +
            "status = :status," +
            "agentName = :agentName," +
            "school = :school," +
            "business = :business," +
            "park = :park," +
            "publicTransport = :publicTransport," +
            "dateOfEntry = :dateOfEntry," +
            "dateSold = :dateSold," +
            "id = :id"
            )
    int updateProperty(String category, float price, float surface, String address,
                       int nbRooms, int nbBathRooms, int nbBedRooms, String description, String status, String agentName,
                       boolean school, boolean business, boolean park, boolean publicTransport, String dateOfEntry, String dateSold, long id);

    @Query("UPDATE PropertyPhotos SET photoUrl = :photos WHERE photosId =:id")
    int updateGallery(List<String> photos, long id);

    @Query("DELETE FROM Property")
    void deleteAllproperties();

    @Query("SELECT * FROM Property WHERE id = :propertyId")
    Cursor getPropertiesWithCursor(long propertyId);

    @Query("DELETE FROM Property WHERE id = :propertyId")
    int deleteProperty(long propertyId);

@Query("SELECT * FROM Property WHERE " +
            " price BETWEEN :miniPrice AND :maxiPrice " +
            " AND surface BETWEEN :miniSurface AND :maxiSurface" +
            " AND nbRooms BETWEEN :miniRoom AND :maxiRoom" +
            " AND school LIKE :school" +
            " AND business LIKE :business" +
            " AND publicTransport LIKE :publicTransport" +
            " AND park LIKE :park")
LiveData<List<Property>> getSearchedProperty(int miniPrice, int maxiPrice, int miniSurface, int maxiSurface, int miniRoom, int maxiRoom, boolean school, boolean business, boolean publicTransport, boolean park);
}
