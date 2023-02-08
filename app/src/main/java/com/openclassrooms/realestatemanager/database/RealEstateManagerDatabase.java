package com.openclassrooms.realestatemanager.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassrooms.realestatemanager.database.dao.PropertyDao;
import com.openclassrooms.realestatemanager.database.dao.PropertyPhotosDao;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyPhotos;

@Database(entities = {Property.class, PropertyPhotos.class}, version = 1, exportSchema = false)
public abstract class RealEstateManagerDatabase extends RoomDatabase {

    private static volatile RealEstateManagerDatabase INSTANCE;

    public static RealEstateManagerDatabase getINSTANCE(Context context) {
        if (INSTANCE == null) {
            synchronized (RealEstateManagerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    RealEstateManagerDatabase.class, "MyDatabase.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    //Dao
    public abstract PropertyDao propertyDao();

    public abstract PropertyPhotosDao propertyPhotosDao();

    private static Callback prepopulateDatabase() {
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues propertyOne = new ContentValues();

                propertyOne.put("id", 1);
                propertyOne.put("category", "Maison");

                db.insert("Property", OnConflictStrategy.IGNORE, propertyOne);
            }
        };
    }

}
