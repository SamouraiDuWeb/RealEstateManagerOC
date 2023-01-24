package com.openclassrooms.realestatemanager.injection;

import android.content.Context;

import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.viewModel.repositories.PropertyDataRepository;
import com.openclassrooms.realestatemanager.viewModel.repositories.PropertyPhotosDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    public static PropertyDataRepository providePropertyDataSource(Context context) {
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getINSTANCE(context);
        return new PropertyDataRepository(database.propertyDao());
    }

    public static PropertyPhotosDataRepository providePropertyPhotosDataSource(Context context) {
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getINSTANCE(context);
        return new PropertyPhotosDataRepository(database.propertyPhotosDao());
    }

    public static Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        PropertyDataRepository dataSourceProperty = providePropertyDataSource(context);
        PropertyPhotosDataRepository dataSourcePropertyPhotos = providePropertyPhotosDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceProperty, dataSourcePropertyPhotos, executor);
    }
}
