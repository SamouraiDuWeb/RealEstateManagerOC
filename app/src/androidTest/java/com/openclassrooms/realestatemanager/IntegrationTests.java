package com.openclassrooms.realestatemanager;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.testng.annotations.Test;

public class IntegrationTests {

    Context context = ApplicationProvider.getApplicationContext();

    @Test
    public void testInternetAvailability() {
        assertEquals(true, Utils.isInternetAvailable(context));
    }
}
