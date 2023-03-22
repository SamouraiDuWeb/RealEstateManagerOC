package com.openclassrooms.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.provider.REMContentProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(AndroidJUnit4.class)
public class REMContentProviderTests {

    //DATA set for test
    private static final long PROPERTY_ID = 1;
    //For DATA
    private ContentResolver contentResolver;

    @Before
    public void setUp() {
        InstrumentationRegistry.getInstrumentation().getTargetContext().deleteDatabase("Property.db");
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(), RealEstateManagerDatabase.class)
                .allowMainThreadQueries()
                .build();
        contentResolver = InstrumentationRegistry.getInstrumentation().getContext().getContentResolver();
    }

    @Test
    public void getPropertyWhenNoPropertyInserted() {
        final Cursor cursor = contentResolver.query(ContentUris.withAppendedId(REMContentProvider.URI_PROPERTY, PROPERTY_ID),
                null, null, null, null);
        assertThat(cursor, notNullValue());
        if (cursor != null) {
            assertThat(cursor.getCount(), is(1));
            cursor.close();
        }
    }

    @Test
    public void insertAndGetProperty() {
        // BEFORE : Adding demo property
        final Uri propertyUri = contentResolver.insert(REMContentProvider.URI_PROPERTY, generateProperty());
        // TEST
        final Cursor cursor =
                contentResolver.query(ContentUris.withAppendedId(REMContentProvider.URI_PROPERTY, PROPERTY_ID),
                        null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
        assertThat(cursor.moveToFirst(), is(true));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("category")), is("Maison"));
        contentResolver.delete(Objects.requireNonNull(propertyUri), null, null);
    }

    private ContentValues generateProperty() {

        ContentValues values = new ContentValues();

        values.put("id", 20);
        values.put("category", "Maison");
        values.put("address", "25 Allée du Docteur Calmet 93260 Les Lilas");
        values.put("price", "720000");
        values.put("surface", "450m²");
        values.put("nbRooms", 2);
        values.put("nbBathrooms", 1);
        values.put("nbBedrooms", 3);
        values.put("description", "\"Superbe propriété de 450m², idéale pour les familles nombreuses ou les amateurs d'espace. Cette spacieuse maison offre de grandes pièces lumineuses et bien agencées, ainsi que plusieurs espaces de vie confortables. Le jardin paysagé et arboré est un véritable havre de paix et offre un cadre idyllique pour se détendre en toute tranquillité. De plus, la propriété est située dans un quartier résidentiel calme et sécurisé, à proximité de toutes les commodités. Une occasion unique d'acquérir une propriété de prestige dans un environnement privilégié.");
        values.put("status", "disponible");
        values.put("agentName", "Leoo");
        values.put("school", true);
        values.put("business", true);
        values.put("park", true);
        values.put("publicTransport", true);
        values.put("dateOfEntry", Utils.getGoodFormatDate());

        return values;
    }
}
