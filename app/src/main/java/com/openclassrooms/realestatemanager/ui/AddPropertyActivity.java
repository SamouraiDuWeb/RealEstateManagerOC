package com.openclassrooms.realestatemanager.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.databinding.ActivityAddPropertyBinding;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.PropertyPhotos;
import com.openclassrooms.realestatemanager.models.User;
import com.openclassrooms.realestatemanager.viewModel.RealEstateManagerViewModel;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddPropertyActivity extends AppCompatActivity {

    private static final long HOUSE_ID = 1;
    private ActivityAddPropertyBinding binding;

    private Spinner spCategory;
    private EditText etSurface, etPrix, etRooms, etBedRooms, etBathrooms, etDescription;
    private CheckBox cbSchool, cbBusiness, cbParks, cbPublicTransports;
    private Button btnAddphoto;
    private LinearLayout llGallery;

    private String category, address, streetNumber, streetName, zipCode, city, description, dateOfEntry, dateSold;
    private float surface, price;
    private int nbRooms, nbBathRooms, nbBedRooms;
    private boolean school, business, park, publicTransport;
    private List<PropertyPhotos> gallery;
    private boolean isInternetUp = true;

    private FirebaseUser user;
    private String propertyUserName;

    private RealEstateManagerViewModel realEstateManagerViewModel;
    private int GALLERY_REQUEST_CODE = 101;

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
        addPhoto();
        verifyInputs();
    }

    private void verifyInputs() {

    }

    private void addPhoto() {
        btnAddphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(AddPropertyActivity.this);
                builder.setTitle("Add Photo");
                builder.setMessage("Select a source to add photo");

                // Set the positive button to open the gallery
                builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Open the gallery
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
                    }
                });
                builder.create().show();
            }
        });
    }

    private void getUserInputs() {
        category = spCategory.getSelectedItem().toString();

        String strArea = etSurface.getText().toString();
        if (!TextUtils.isEmpty(strArea)) {
            surface = Integer.parseInt(strArea);
        }

        String strPrice = etPrix.getText().toString();
        if (!TextUtils.isEmpty(strPrice)) {
            price = Integer.parseInt(strPrice);
        }

        String strNumberOfRooms = etRooms.getText().toString();
        if (!TextUtils.isEmpty(strNumberOfRooms)) {
            nbRooms = Integer.parseInt(strNumberOfRooms);
        }

        String strNumberOfBedRooms = etBedRooms.getText().toString();
        if (!TextUtils.isEmpty(strNumberOfBedRooms)) {
            nbBedRooms = Integer.parseInt(strNumberOfBedRooms);
        }

        String strNumberOfBathrooms = etBathrooms.getText().toString();
        if (!TextUtils.isEmpty(strNumberOfBathrooms)) {
            nbBathRooms = Integer.parseInt(strNumberOfBathrooms);
        }

        school = cbSchool.isChecked();

        business = cbBusiness.isChecked();

        park = cbParks.isChecked();

        publicTransport = cbPublicTransports.isChecked();

        description = etDescription.getText().toString();

        Date date = Calendar.getInstance().getTime();
        dateOfEntry = new SimpleDateFormat("dd-MM-yyyy").format(date);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            propertyUserName = user.getDisplayName().toString();
        }
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
        btnAddphoto = binding.btnAddActivityAddPhoto;
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
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS,Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // do something with the selected place
                List<AddressComponent> addressComponents = place.getAddressComponents().asList();
                autocompleteFragment.setText("" + addressComponents);
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
                address = "" + streetNumber + " " + streetName + " " + zipCode + " " + city;
            }

            @Override
            public void onError(Status status) {
                // handle error
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            // Do something with the selected image (e.g. display it in an ImageView)
        }
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.realEstateManagerViewModel = new ViewModelProvider(this, viewModelFactory).get(RealEstateManagerViewModel.class);
        this.realEstateManagerViewModel.init(HOUSE_ID);
    }
}