package com.openclassrooms.realestatemanager.ui;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
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
import android.widget.Toast;

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
import com.openclassrooms.realestatemanager.adapter.PhotoPropertyAdapter;
import com.openclassrooms.realestatemanager.databinding.ActivityAddPropertyBinding;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyPhotos;
import com.openclassrooms.realestatemanager.models.User;
import com.openclassrooms.realestatemanager.viewModel.RealEstateManagerViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddPropertyActivity extends AppCompatActivity {

    private static final long HOUSE_ID = 1;
    private ActivityAddPropertyBinding binding;

    private Spinner spCategory;
    private EditText etSurface, etPrix, etRooms, etBedRooms, etBathrooms, etDescription, etStreetNumber, etstreetName, etZipCode, etCity;
    private CheckBox cbSchool, cbBusiness, cbParks, cbPublicTransports;
    private Button btnAddphoto;
    private Button btnAddProperty;
    private LinearLayout llGallery;

    private String category, address, streetNumber, streetName, zipCode, city, description, dateOfEntry, dateSold;
    private float surface = 0, price = 0;
    private int nbRooms = 0, nbBathRooms = 0, nbBedRooms = 0;
    private boolean school, business, park, publicTransport;
    private List<PropertyPhotos> gallery;
    private boolean isInternetUp = true;

    private FirebaseUser user;
    private String propertyUserName;

    private RealEstateManagerViewModel realEstateManagerViewModel;
    private int GALLERY_REQUEST_CODE = 101;

    private Property propertyToAdd;
    private String picture;
    private Property propertyTest;

    private RecyclerView rvGallery;
    private PropertyPhotos photoToAdd;
    private List<PropertyPhotos> galleryToShow = new ArrayList<PropertyPhotos>();
    private PhotoPropertyAdapter adapter;

    private boolean writeStoragePermission, readStoragePermission;

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private String imageDescription;

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
        } else {
            binding.llOfflineAddActivityAddress.setVisibility(View.VISIBLE);
            binding.etAddActivityAddress.setVisibility(View.INVISIBLE);
        }
        System.out.println("/// " + isInternetUp);

        initInputs();
        addPhoto();
        addProperty();
//        showSelectedImages();
    }

    private void showSelectedImages() {
        adapter = new PhotoPropertyAdapter(galleryToShow, this);
        rvGallery.setAdapter(adapter);
        rvGallery.setLayoutManager(new LinearLayoutManager(AddPropertyActivity.this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void addProperty() {
        btnAddProperty = binding.btnAddProperty;

        btnAddProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserInputs();
                if (verifyInputs()) {
//                    propertyToAdd = new Property(category, price, surface, nbRooms, nbBathRooms, nbBedRooms, description, "disponible", new Date(System.currentTimeMillis()), new Date(), user.getDisplayName(), school, business, park, publicTransport);
                    propertyTest = new Property();

                }
            }
        });
    }

    private boolean verifyInputs() {
        Boolean isOk;
        if (category.isEmpty()) {
            Toast.makeText(this, "Veuillez indiquer la catégorie du bien", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (Float.toString(surface).isEmpty()) {
            Toast.makeText(this, "Veuillez saisir le surface du bien", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (streetNumber.isEmpty()) {
            Toast.makeText(this, "Veuillez saisir un numéro de rue", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (streetName.isEmpty()) {
            Toast.makeText(this, "Veuillez indiquer une rue", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (zipCode.isEmpty()) {
            Toast.makeText(this, "Veuillez indiquer le code postal", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (city.isEmpty()) {
            Toast.makeText(this, "Veuillez saisir une ville", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (description.isEmpty()) {
            Toast.makeText(this, "Veuillez indiquer une description", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (propertyUserName.isEmpty()) {
            Toast.makeText(this, "Veuillez vous connecter pour ajouter un bien", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (price == 0) {
            Toast.makeText(this, "Veuillez indiquer le prix du bien", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (nbRooms == 0) {
            Toast.makeText(this, "Veuillez indiquer le nombre de pièce", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (nbBedRooms == 0) {
            Toast.makeText(this, "Veuillez indiquer le nombre de chambre", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (nbBathRooms == 0) {
            Toast.makeText(this, "Veuillez indiquer le nombre de salle de bain", Toast.LENGTH_LONG).show();
            isOk = false;
        } else {
            isOk = true;
        }
        return isOk;
    }

    private void addPhoto() {
        btnAddphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a dialog
                if (!checkPermissions()) {
                    requestPermissions();
                }
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
            surface = Float.parseFloat(strArea);
        }

        String strPrice = etPrix.getText().toString();
        if (!TextUtils.isEmpty(strPrice)) {
            price = Float.parseFloat(strPrice);
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

        streetNumber = "";
        streetName = "";
        zipCode = "";
        city = "";
        if (isInternetUp) {
            getGoogleAddress();
        } else {
            streetNumber = etStreetNumber.getText().toString();
            streetName = etstreetName.getText().toString();
            zipCode = etZipCode.getText().toString();
            city = etCity.getText().toString();
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
        etStreetNumber = binding.etAddActivityStreetNumber;
        etstreetName = binding.etAddActivityStreetName;
        etZipCode = binding.etAddActivityZipcode;
        etCity = binding.etAddActivityCity;
        rvGallery = binding.rvGallery;

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
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS, Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
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

    public String getPathFromUri(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        //Cursor for access
        Cursor cursor = this.getContentResolver().query(uri, filePathColumn, null, null, null);
        //position on line
        cursor.moveToFirst();
        //get path
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imgPath = cursor.getString(columnIndex);
        cursor.close();
        this.picture = imgPath;

        return picture;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String photoUrl = getPathFromUri(selectedImage);
            System.out.println("/// : " + galleryToShow);
            //New dialog to add a description to the image
//            descriptionDialog();
            photoToAdd = new PropertyPhotos(1, "", photoUrl);
            galleryToShow.add(photoToAdd);
            showSelectedImages();
        }
    }

//    private void descriptionDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Description de l'image");
//
//// Set up the input
//        final EditText input = new EditText(this);
//        input.setInputType(InputType.TYPE_CLASS_TEXT);
//        builder.setView(input);
//
//// Set up the buttons
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                imageDescription = input.getText().toString();
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        builder.show();
//    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.realEstateManagerViewModel = new ViewModelProvider(this, viewModelFactory).get(RealEstateManagerViewModel.class);
        this.realEstateManagerViewModel.init(HOUSE_ID);
    }

    private boolean checkPermissions() {
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean allGranted = true;
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        allGranted = false;
                        break;
                    }
                }
                if (!allGranted) {
                    // Handle the case where not all permissions are granted
                }
            }
        }
    }
}