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
                propertyOne.put("address", "25 All??e du Docteur Calmet 93260 Les Lilas");
                propertyOne.put("price", "720000");
                propertyOne.put("surface", "450m??");
                propertyOne.put("nbRooms", 2);
                propertyOne.put("nbBathrooms", 1);
                propertyOne.put("nbBedrooms", 3);
                propertyOne.put("description", "\"Superbe propri??t?? de 450m??, id??ale pour les familles nombreuses ou les amateurs d'espace. Cette spacieuse maison offre de grandes pi??ces lumineuses et bien agenc??es, ainsi que plusieurs espaces de vie confortables. Le jardin paysag?? et arbor?? est un v??ritable havre de paix et offre un cadre idyllique pour se d??tendre en toute tranquillit??. De plus, la propri??t?? est situ??e dans un quartier r??sidentiel calme et s??curis??, ?? proximit?? de toutes les commodit??s. Une occasion unique d'acqu??rir une propri??t?? de prestige dans un environnement privil??gi??.");
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
                propertyTwo.put("address", "25 All??e du Docteur Calmet 93260 Les Lilas");
                propertyTwo.put("price", "720000");
                propertyTwo.put("surface", "450m??");
                propertyTwo.put("nbRooms", 2);
                propertyTwo.put("nbBathrooms", 1);
                propertyTwo.put("nbBedrooms", 3);
                propertyTwo.put("description", "\"Superbe propri??t?? de 450m??, id??ale pour les familles nombreuses ou les amateurs d'espace. Cette spacieuse maison offre de grandes pi??ces lumineuses et bien agenc??es, ainsi que plusieurs espaces de vie confortables. Le jardin paysag?? et arbor?? est un v??ritable havre de paix et offre un cadre idyllique pour se d??tendre en toute tranquillit??. De plus, la propri??t?? est situ??e dans un quartier r??sidentiel calme et s??curis??, ?? proximit?? de toutes les commodit??s. Une occasion unique d'acqu??rir une propri??t?? de prestige dans un environnement privil??gi??.");
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
