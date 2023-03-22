package com.openclassrooms.realestatemanager.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.database.dao.PropertyDao;
import com.openclassrooms.realestatemanager.database.dao.PropertyPhotosDao;
import com.openclassrooms.realestatemanager.database.dao.UserDao;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyPhotos;
import com.openclassrooms.realestatemanager.models.User;

@Database(entities = {Property.class, PropertyPhotos.class, User.class}, version = 6, exportSchema = false)
public abstract class RealEstateManagerDatabase extends RoomDatabase {

    private static volatile RealEstateManagerDatabase INSTANCE;

    public static RealEstateManagerDatabase getINSTANCE(Context context) {
        if (INSTANCE == null) {
            synchronized (RealEstateManagerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    RealEstateManagerDatabase.class, "MyDatabase.db")
                            .fallbackToDestructiveMigration()
//                            .addCallback(prepopulateDatabase())
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
                propertyOne.put("dateOfEntry", Utils.getGoodFormatDate()
                );

                db.insert("Property", OnConflictStrategy.IGNORE, propertyOne);

                ContentValues propertyTwo = new ContentValues();

                propertyTwo.put("id", 21);
                propertyTwo.put("category", "Maison");
                propertyTwo.put("address", "12 rue Alexander Fleming 75019 Paris");
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
                propertyTwo.put("dateOfEntry", Utils.getGoodFormatDate());


                db.insert("Property", OnConflictStrategy.IGNORE, propertyTwo);

                ContentValues propertyThree = new ContentValues();

                propertyThree.put("id", 22);
                propertyThree.put("category", "Appartement");
                propertyThree.put("address", "70 Av. des Bateliers 93210 Saint-Denis");
                propertyThree.put("price", "520000");
                propertyThree.put("surface", "80m²");
                propertyThree.put("nbRooms", 2);
                propertyThree.put("nbBathrooms", 1);
                propertyThree.put("nbBedrooms", 3);
                propertyThree.put("description", "\"Superbe propriété de 80m², idéale pour les familles nombreuses ou les amateurs d'espace. Cette spacieuse maison offre de grandes pièces lumineuses et bien agencées, ainsi que plusieurs espaces de vie confortables. Le jardin paysagé et arboré est un véritable havre de paix et offre un cadre idyllique pour se détendre en toute tranquillité. De plus, la propriété est située dans un quartier résidentiel calme et sécurisé, à proximité de toutes les commodités. Une occasion unique d'acquérir une propriété de prestige dans un environnement privilégié.");
                propertyThree.put("status", "disponible");
                propertyThree.put("agentName", "Leoo");
                propertyThree.put("school", true);
                propertyThree.put("business", true);
                propertyThree.put("park", true);
                propertyThree.put("publicTransport", true);
                propertyThree.put("dateOfEntry", Utils.getGoodFormatDate());


                db.insert("Property", OnConflictStrategy.IGNORE, propertyThree);

                ContentValues propertyFour = new ContentValues();

                propertyFour.put("id", 23);
                propertyFour.put("category", "Appartement");
                propertyFour.put("address", "99 Rue de Penthièvre 75008 Paris");
                propertyFour.put("price", "340000");
                propertyFour.put("surface", "120m²");
                propertyFour.put("nbRooms", 3);
                propertyFour.put("nbBathrooms", 2);
                propertyFour.put("nbBedrooms", 3);
                propertyFour.put("description", "\"Superbe propriété de 120m², idéale pour les familles nombreuses ou les amateurs d'espace. Cette spacieuse maison offre de grandes pièces lumineuses et bien agencées, ainsi que plusieurs espaces de vie confortables. Le jardin paysagé et arboré est un véritable havre de paix et offre un cadre idyllique pour se détendre en toute tranquillité. De plus, la propriété est située dans un quartier résidentiel calme et sécurisé, à proximité de toutes les commodités. Une occasion unique d'acquérir une propriété de prestige dans un environnement privilégié.");
                propertyFour.put("status", "disponible");
                propertyFour.put("agentName", "Leoo");
                propertyFour.put("school", true);
                propertyFour.put("business", true);
                propertyFour.put("park", true);
                propertyFour.put("publicTransport", true);
                propertyFour.put("dateOfEntry", Utils.getGoodFormatDate());


                db.insert("Property", OnConflictStrategy.IGNORE, propertyFour);

                ContentValues propertyFive = new ContentValues();

                propertyFive.put("id", 24);
                propertyFive.put("category", "Maison");
                propertyFive.put("address", "55 Rue Raymond Poincaré 93110 Rosny-sous-Bois");
                propertyFive.put("price", "450000");
                propertyFive.put("surface", "200m²");
                propertyFive.put("nbRooms", 3);
                propertyFive.put("nbBathrooms", 2);
                propertyFive.put("nbBedrooms", 3);
                propertyFive.put("description", "\"Superbe propriété de 200m², idéale pour les familles nombreuses ou les amateurs d'espace. Cette spacieuse maison offre de grandes pièces lumineuses et bien agencées, ainsi que plusieurs espaces de vie confortables. Le jardin paysagé et arboré est un véritable havre de paix et offre un cadre idyllique pour se détendre en toute tranquillité. De plus, la propriété est située dans un quartier résidentiel calme et sécurisé, à proximité de toutes les commodités. Une occasion unique d'acquérir une propriété de prestige dans un environnement privilégié.");
                propertyFive.put("status", "disponible");
                propertyFive.put("agentName", "Leoo");
                propertyFive.put("school", true);
                propertyFive.put("business", false);
                propertyFive.put("park", true);
                propertyFive.put("publicTransport", false);
                propertyFive.put("dateOfEntry", Utils.getGoodFormatDate());


                db.insert("Property", OnConflictStrategy.IGNORE, propertyFive);

                ContentValues propertySix = new ContentValues();

                propertySix.put("id", 25);
                propertySix.put("category", "Duplex");
                propertySix.put("address", "68 Rue du Faubourg Saint-Honoré 75008 Paris");
                propertySix.put("price", "1200000");
                propertySix.put("surface", "200m²");
                propertySix.put("nbRooms", 3);
                propertySix.put("nbBathrooms", 2);
                propertySix.put("nbBedrooms", 3);
                propertySix.put("description", "\"Superbe propriété de 200m², idéale pour les familles nombreuses ou les amateurs d'espace. Cette spacieuse maison offre de grandes pièces lumineuses et bien agencées, ainsi que plusieurs espaces de vie confortables. Le jardin paysagé et arboré est un véritable havre de paix et offre un cadre idyllique pour se détendre en toute tranquillité. De plus, la propriété est située dans un quartier résidentiel calme et sécurisé, à proximité de toutes les commodités. Une occasion unique d'acquérir une propriété de prestige dans un environnement privilégié.");
                propertySix.put("status", "disponible");
                propertySix.put("agentName", "Leoo");
                propertySix.put("school", false);
                propertySix.put("business", true);
                propertySix.put("park", false);
                propertySix.put("publicTransport", true);
                propertySix.put("dateOfEntry", Utils.getGoodFormatDate());


                db.insert("Property", OnConflictStrategy.IGNORE, propertySix);

                ContentValues propertySeven = new ContentValues();

                propertySeven.put("id", 26);
                propertySeven.put("category", "Appartement");
                propertySeven.put("address", "87 Av. Ferdinand de Lesseps 91420 Morangis");
                propertySeven.put("price", "700000");
                propertySeven.put("surface", "300m²");
                propertySeven.put("nbRooms", 3);
                propertySeven.put("nbBathrooms", 2);
                propertySeven.put("nbBedrooms", 3);
                propertySeven.put("description", "\"Superbe propriété de 300m², idéale pour les familles nombreuses ou les amateurs d'espace. Cette spacieuse maison offre de grandes pièces lumineuses et bien agencées, ainsi que plusieurs espaces de vie confortables. Le jardin paysagé et arboré est un véritable havre de paix et offre un cadre idyllique pour se détendre en toute tranquillité. De plus, la propriété est située dans un quartier résidentiel calme et sécurisé, à proximité de toutes les commodités. Une occasion unique d'acquérir une propriété de prestige dans un environnement privilégié.");
                propertySeven.put("status", "disponible");
                propertySeven.put("agentName", "Leoo");
                propertySeven.put("school", false);
                propertySeven.put("business", true);
                propertySeven.put("park", false);
                propertySeven.put("publicTransport", true);
                propertySeven.put("dateOfEntry", Utils.getGoodFormatDate());


                db.insert("Property", OnConflictStrategy.IGNORE, propertySeven);

                ContentValues propertyEight = new ContentValues();

                propertyEight.put("id", 27);
                propertyEight.put("category", "Propriété Commerciale");
                propertyEight.put("address", "24 Av de Marlioz 73100 Aix-les-Bains");
                propertyEight.put("price", "200000");
                propertyEight.put("surface", "600m²");
                propertyEight.put("nbRooms", 3);
                propertyEight.put("nbBathrooms", 2);
                propertyEight.put("nbBedrooms", 3);
                propertyEight.put("description", "\"Superbe propriété de 600m², idéale pour les familles nombreuses ou les amateurs d'espace. Cette spacieuse maison offre de grandes pièces lumineuses et bien agencées, ainsi que plusieurs espaces de vie confortables. Le jardin paysagé et arboré est un véritable havre de paix et offre un cadre idyllique pour se détendre en toute tranquillité. De plus, la propriété est située dans un quartier résidentiel calme et sécurisé, à proximité de toutes les commodités. Une occasion unique d'acquérir une propriété de prestige dans un environnement privilégié.");
                propertyEight.put("status", "disponible");
                propertyEight.put("agentName", "Leoo");
                propertyEight.put("school", false);
                propertyEight.put("business", true);
                propertyEight.put("park", false);
                propertyEight.put("publicTransport", true);
                propertyEight.put("dateOfEntry", Utils.getGoodFormatDate());


                db.insert("Property", OnConflictStrategy.IGNORE, propertyEight);

                ContentValues propertyNine = new ContentValues();

                propertyNine.put("id", 28);
                propertyNine.put("category", "Propriété Industrielle");
                propertyNine.put("address", "Rue Hubert de l'Isle 33240 Saint-André-de-Cubzac");
                propertyNine.put("price", "400000");
                propertyNine.put("surface", "100m²");
                propertyNine.put("nbRooms", 3);
                propertyNine.put("nbBathrooms", 2);
                propertyNine.put("nbBedrooms", 3);
                propertyNine.put("description", "\"Superbe propriété de 100m², idéale pour les familles nombreuses ou les amateurs d'espace. Cette spacieuse maison offre de grandes pièces lumineuses et bien agencées, ainsi que plusieurs espaces de vie confortables. Le jardin paysagé et arboré est un véritable havre de paix et offre un cadre idyllique pour se détendre en toute tranquillité. De plus, la propriété est située dans un quartier résidentiel calme et sécurisé, à proximité de toutes les commodités. Une occasion unique d'acquérir une propriété de prestige dans un environnement privilégié.");
                propertyNine.put("status", "disponible");
                propertyNine.put("agentName", "Leoo");
                propertyNine.put("school", true);
                propertyNine.put("business", false);
                propertyNine.put("park", false);
                propertyNine.put("publicTransport", true);
                propertyNine.put("dateOfEntry", Utils.getGoodFormatDate());


                db.insert("Property", OnConflictStrategy.IGNORE, propertyNine);

                ContentValues propertyTen = new ContentValues();

                propertyTen.put("id", 29);
                propertyTen.put("category", "Maison");
                propertyTen.put("address", "82 Rue de la Grande Fusterie 84000 Avignon");
                propertyTen.put("price", "350000");
                propertyTen.put("surface", "120m²");
                propertyTen.put("nbRooms", 3);
                propertyTen.put("nbBathrooms", 2);
                propertyTen.put("nbBedrooms", 3);
                propertyTen.put("description", "\"Superbe propriété de 120m², idéale pour les familles nombreuses ou les amateurs d'espace. Cette spacieuse maison offre de grandes pièces lumineuses et bien agencées, ainsi que plusieurs espaces de vie confortables. Le jardin paysagé et arboré est un véritable havre de paix et offre un cadre idyllique pour se détendre en toute tranquillité. De plus, la propriété est située dans un quartier résidentiel calme et sécurisé, à proximité de toutes les commodités. Une occasion unique d'acquérir une propriété de prestige dans un environnement privilégié.");
                propertyTen.put("status", "disponible");
                propertyTen.put("agentName", "Leoo");
                propertyTen.put("school", true);
                propertyTen.put("business", true);
                propertyTen.put("park", true);
                propertyTen.put("publicTransport", true);
                propertyTen.put("dateOfEntry", Utils.getGoodFormatDate());


                db.insert("Property", OnConflictStrategy.IGNORE, propertyTen);
            }
        };
    }
}
