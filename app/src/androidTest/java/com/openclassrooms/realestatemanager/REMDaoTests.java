package com.openclassrooms.realestatemanager;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import androidx.arch.core.executor.TaskExecutor;

import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyPhotos;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class REMDaoTests {
    //For data
    private RealEstateManagerDatabase database;

    //Data set for Test
    private static final long PROPERTY_ID = 51;
    private static final Property PROPERTY_DEMO = new Property( PROPERTY_ID, "Maison", 450000, 230, 4,
            4,
            2,
            "Jolie description de maison à vendre",
            "12 rue Alexander Fleming 75019 Paris",
            "Disponible",
            "15/03/2023",
            "",
            "LeoTesteur",
            "",
            true,
            true,
            true, false);
    private static final PropertyPhotos ILLUSTRATION_SALON = new PropertyPhotos(PROPERTY_ID, "Salon", " ");
    private static final PropertyPhotos ILLUSTRATION_CUISINE = new PropertyPhotos(PROPERTY_ID, "Cuisine", " ");

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                        RealEstateManagerDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        database.clearAllTables();
    }

    //PropertyDao
    @Test
    public void insertAndGetProperty() throws InterruptedException {
        //Before: Adding demo property
        this.database.propertyDao().createProperty(PROPERTY_DEMO);
        //Test
        Property property = LiveDataTestUtil.getValue(this.database.propertyDao().getProperty(PROPERTY_ID));
        assertTrue(property.getAddress().equals(PROPERTY_DEMO.getAddress()) && PROPERTY_DEMO.getId() == PROPERTY_ID);
    }

    @Test
    public void getPropertyWhenNoItemInserted() throws InterruptedException {
        //TEST
        List<Property> propertyList = LiveDataTestUtil.getValue(this.database.propertyDao().getAll());
        TestCase.assertTrue(propertyList.isEmpty());
    }

    @Test
    public void insertAndUpdateProperty() throws InterruptedException {
        // BEFORE : Adding demo Property. Get the property added & delete it.
        this.database.propertyDao().createProperty(PROPERTY_DEMO);
        // Get the property added & delete it.
        Property propertyAdded = LiveDataTestUtil.getValue(this.database.propertyDao().getProperty(PROPERTY_ID));
        propertyAdded.setSurface(130);
        this.database.propertyDao().updateProperty(propertyAdded.getCategory(), propertyAdded.getPrice(), propertyAdded.getSurface(), propertyAdded.getAddress(),
                propertyAdded.getNbRooms(), propertyAdded.getNbBathrooms(), propertyAdded.getNbBedrooms(), propertyAdded.getDescription(), propertyAdded.getStatus(), propertyAdded.getAgentName(),
                propertyAdded.isSchool(), propertyAdded.isBusiness(), propertyAdded.isPark(), propertyAdded.isPublicTransport(), propertyAdded.getDateOfEntry(), "");
        //TEST
        Property propertyToTest = LiveDataTestUtil.getValue(this.database.propertyDao().getProperty(PROPERTY_ID));
        assertEquals(130, propertyAdded.getSurface(), 0);
    }

    //Illustration Dao
    @Test
    public void insertAndGetIllustration() throws InterruptedException {
        //Before: Adding demo property and demo illustration
        this.database.propertyDao().createProperty(PROPERTY_DEMO);
        this.database.propertyPhotosDao().createPropertyPhoto(ILLUSTRATION_SALON);
        this.database.propertyPhotosDao().createPropertyPhoto(ILLUSTRATION_CUISINE);

        //Test
        List<PropertyPhotos> gallery = LiveDataTestUtil.getValue(this.database.propertyPhotosDao().getGallery(PROPERTY_ID));
        Assert.assertEquals(2, gallery.size());
    }

    @Test
    public void getGalleryWhenNoItemInserted() throws InterruptedException {
        //TEST
        List<PropertyPhotos> gallery = LiveDataTestUtil.getValue(this.database.propertyPhotosDao().getGallery(PROPERTY_ID));
        TestCase.assertTrue(gallery.isEmpty());
    }
}
