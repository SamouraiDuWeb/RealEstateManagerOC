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
import com.openclassrooms.realestatemanager.database.dao.UserDao;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyPhotos;
import com.openclassrooms.realestatemanager.models.User;

@Database(entities = {Property.class, PropertyPhotos.class, User.class}, version = 5, exportSchema = false)
public abstract class RealEstateManagerDatabase extends RoomDatabase {

    private static volatile RealEstateManagerDatabase INSTANCE;

    public static RealEstateManagerDatabase getINSTANCE(Context context) {
        if (INSTANCE == null) {
            synchronized (RealEstateManagerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    RealEstateManagerDatabase.class, "MyDatabase.db")
                            .fallbackToDestructiveMigration()
                            .addCallback(prepopulateDatabase())
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    //Dao
    public abstract PropertyDao propertyDao();

    public abstract PropertyPhotosDao propertyPhotosDao();

    public abstract UserDao userDao();

    private static Callback prepopulateDatabase() {
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues propertyOne = new ContentValues();

                propertyOne.put("id", 20);
                propertyOne.put("category", "Maison");
                propertyOne.put("address", "25 Allée du Docteur Calmet 93260 Les Lilas");
                propertyOne.put("price", "720000");
                propertyOne.put("surface", "450m²");
                propertyOne.put("nbRooms", 2);
                propertyOne.put("nbBathrooms", 1);
                propertyOne.put("nbBedrooms", 3);
                propertyOne.put("description", "\"Superbe propriété de 450m², idéale pour les familles nombreuses ou les amateurs d'espace. Cette spacieuse maison offre de grandes pièces lumineuses et bien agencées, ainsi que plusieurs espaces de vie confortables. Le jardin paysagé et arboré est un véritable havre de paix et offre un cadre idyllique pour se détendre en toute tranquillité. De plus, la propriété est située dans un quartier résidentiel calme et sécurisé, à proximité de toutes les commodités. Une occasion unique d'acquérir une propriété de prestige dans un environnement privilégié.");
                propertyOne.put("status", "disponible");
                propertyOne.put("agentName", "Leoo");
                propertyOne.put("school", true);
                propertyOne.put("business", true);
                propertyOne.put("park", true);
                propertyOne.put("publicTransport", true);
                propertyOne.put("dateOfEntry", System.currentTimeMillis());

                db.insert("Property", OnConflictStrategy.IGNORE, propertyOne);

                ContentValues propertyTwo = new ContentValues();

                propertyTwo.put("id", 21);
                propertyTwo.put("category", "Maison");
                propertyTwo.put("address", "25 Allée du Docteur Calmet 93260 Les Lilas");
                propertyTwo.put("price", "720000");
                propertyTwo.put("surface", "450m²");
                propertyTwo.put("nbRooms", 2);
                propertyTwo.put("nbBathrooms", 1);
                propertyTwo.put("nbBedrooms", 3);
                propertyTwo.put("description", "\"Superbe propriété de 450m², idéale pour les familles nombreuses ou les amateurs d'espace. Cette spacieuse maison offre de grandes pièces lumineuses et bien agencées, ainsi que plusieurs espaces de vie confortables. Le jardin paysagé et arboré est un véritable havre de paix et offre un cadre idyllique pour se détendre en toute tranquillité. De plus, la propriété est située dans un quartier résidentiel calme et sécurisé, à proximité de toutes les commodités. Une occasion unique d'acquérir une propriété de prestige dans un environnement privilégié.");
                propertyTwo.put("status", "disponible");
                propertyTwo.put("agentName", "Leoo");
                propertyTwo.put("school", true);
                propertyTwo.put("business", true);
                propertyTwo.put("park", true);
                propertyTwo.put("publicTransport", true);
                propertyTwo.put("dateOfEntry", System.currentTimeMillis());


                db.insert("Property", OnConflictStrategy.IGNORE, propertyTwo);
            }
        };
    }

}
