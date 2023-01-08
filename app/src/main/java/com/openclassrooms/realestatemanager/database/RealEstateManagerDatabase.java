package com.openclassrooms.realestatemanager.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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

    private static Callback prepopulateDatabase() {
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues propertyOne = new ContentValues();

                propertyOne.put("id", 1);
                propertyOne.put("category", "Maison");
                propertyOne.put("price", 900000);
                propertyOne.put("numberOfRooms", 8);
                propertyOne.put("numberOfBathrooms", 4);
                propertyOne.put("numberOfBedrooms", 4);
                propertyOne.put("school", false);
                propertyOne.put("shopping", false);
                propertyOne.put("publicTransport", true);
                propertyOne.put("swimmingPool", true);
                propertyOne.put("description", "Maison individuelle 170 m² Carrez sur parcelle de 800 m². Orientée sud/ouest, sans vis-à-vis, dans quartier résidentiel très calme. Proche de toutes commodités : commerces, écoles, collège, gare SCNF… sont accessibles à pied (pas plus de 10 min).");
                propertyOne.put("propertyPhotos", "");
                propertyOne.put("address", "15 route de Hourton, 33160 Saint-Aubin-de-Médoc");
                propertyOne.put("available", "disponible");
                propertyOne.put("dateOfEntry", "20/01/2021");
                propertyOne.put("dateOfSale", "null");
                propertyOne.put("realEstateAgent", "");

                db.insert("Property", OnConflictStrategy.IGNORE, propertyOne);
            }
        };
    }

}
