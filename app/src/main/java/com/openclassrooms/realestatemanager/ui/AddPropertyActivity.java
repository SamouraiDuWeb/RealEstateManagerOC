package com.openclassrooms.realestatemanager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.databinding.ActivityAddPropertyBinding;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.PropertyPhotos;
import com.openclassrooms.realestatemanager.viewModel.RealEstateManagerViewModel;

import java.util.Arrays;
import java.util.List;

public class AddPropertyActivity extends AppCompatActivity {

    private static final long HOUSE_ID = 1;
    private ActivityAddPropertyBinding binding;

    private Spinner spCategory;
    private EditText etSurface, etPrix, etRooms, etBedRooms, etBathrooms, etDescription;
    private CheckBox cbSchool, cbBusiness, cbParks, cbPublicTransports;
    private Button btnAddphoto;
    private LinearLayout llGallery;

    private String category, address, streetNumber, streetName, zipCode, city;

    private List<PropertyPhotos> gallery;

    private boolean isInternetUp = true;

    private RealEstateManagerViewModel realEstateManagerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddPropertyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureViewModel();
        setToolbar();

        Places.initialize(getApplicationContext(), "AIzaSyCrqJLy46KdQ7fp3LH8IJZaFW2tAM2bAIM");

        isInternetUp = Utils.isInternetAvailable(this);

        if (isInternetUp) {
            binding.llOfflineAddActivityAddress.setVisibility(View.INVISIBLE);
            getGoogleAddress();
        } else {
            binding.llOfflineAddActivityAddress.setVisibility(View.VISIBLE);
            binding.etAddActivityAddress.setVisibility(View.INVISIBLE);
        }
        System.out.println("/// " + isInternetUp);

        initInputs();
    }

    private void initInputs() {
        spCategory = binding.spAddActivityCategory;
        etSurface = binding.etAddActivityArea;
        etPrix = binding.etAddActivityPrice;
        etRooms = binding.etAddActivityRoom;
        etBathrooms = binding.etAddActivityBathroom;
        etBedRooms = binding.etAddActivityBedroom;
        etDescription = binding.etAddActivityDescription;
        cbBusiness = binding.cbAddPropertyBusiness;
        cbSchool = binding.cbAddPropertySchool;
        cbParks = binding.cbAddPropertyPark;
        cbPublicTransports = binding.cbAddPropertyPublicTransport;
    }


    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void getGoogleAddress() {

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setCountry("FR");
        autocompleteFragment.setHint("Recherche");
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // do something with the selected place
                List<AddressComponent> addressComponents = place.getAddressComponents().asList();

                // Iterate through the list of address components
                for (AddressComponent addressComponent : addressComponents) {
                    // Check if the address component is a street number
                    if (addressComponent.getTypes().contains("street_number")) {
                        streetNumber = addressComponent.getShortName();
                    }
                    // Check if the address component is a route (street name)
                    if (addressComponent.getTypes().contains("route")) {
                        streetName = addressComponent.getShortName();
                    }
                    // Check if the address component is a postal_code
                    if (addressComponent.getTypes().contains("postal_code")) {
                        zipCode = addressComponent.getShortName();
                    }
                    // Check if the address component is a locality (city)
                    if (addressComponent.getTypes().contains("locality")) {
                        city = addressComponent.getShortName();
                    }
                }
            }

            @Override
            public void onError(Status status) {
                // handle error
            }
        });
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.realEstateManagerViewModel = new ViewModelProvider(this, viewModelFactory).get(RealEstateManagerViewModel.class);
        this.realEstateManagerViewModel.init(HOUSE_ID);
    }


}