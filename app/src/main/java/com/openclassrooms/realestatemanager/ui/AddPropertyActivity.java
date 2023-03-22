package com.openclassrooms.realestatemanager.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
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
import com.openclassrooms.realestatemanager.viewModel.RealEstateManagerViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddPropertyActivity extends AppCompatActivity {

    private static final long PROPERTY_ID = 1;
    private ActivityAddPropertyBinding binding;

    private Spinner spCategory;
    private EditText etSurface, etPrix, etRooms, etBedRooms, etBathrooms, etDescription, etStreetNumber, etstreetName, etZipCode, etCity;
    private CheckBox cbSchool, cbBusiness, cbParks, cbPublicTransports;
    private Button btnAddphoto;
    private Button btnAddProperty;
    private LinearLayout llGallery;
    private CheckBox cbStatus;
    private AutocompleteSupportFragment autocompleteFragment;
    private EditText etAddress;

    private String category, address, streetNumber, streetName, zipCode, city, description, dateOfEntry, dateSold, status = "disponible";
    private float surface = 0, price = 0;
    private int nbRooms = 0, nbBathRooms = 0, nbBedRooms = 0;
    private boolean school, business, park, publicTransport;
    private List<PropertyPhotos> gallery;
    private boolean isInternetUp = true;

    private FirebaseUser user;
    private String propertyUserName;

    private RealEstateManagerViewModel realEstateManagerViewModel;
    private int GALLERY_REQUEST_CODE = 101;
    private int ILLUSTRATION_REQUEST_CODE = 102;
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

    private Bundle extras;
    private Property propertyToEdit;
    private long id;
    private Button btnIllustration;
    private String illustration;
    private ImageView ivIllustration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddPropertyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureViewModel();
        setToolbar();
        initInputs();

        Places.initialize(getApplicationContext(), "AIzaSyC3g3Y_iaBGYzzho-dJ-B1D4pA2pKD3PYw");

        extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getLong("id", -1);
            propertyToEdit = (Property) extras.get("current_property");
        }
        if (id == -1 || id == 0) {
            binding.btnAddActivityAddPhoto.setVisibility(View.GONE);
            binding.rvGallery.setVisibility(View.GONE);
            binding.tvStatus.setVisibility(View.GONE);
            binding.cbStatus.setVisibility(View.GONE);
        } else {
            getCurrentProperty(id);
            binding.btnAddProperty.setText("Modifier le bien");
            initImages(id);
        }


        //test with internet down
//        isInternetUp = false;
        isInternetUp = Utils.isInternetAvailable(this);
        showGoogleAdress(isInternetUp);
        System.out.println("/// " + isInternetUp);
        getGoogleAddress();

        addPhoto();
        addProperty();
//        showSelectedImages();
    }

    private void showGoogleAdress(boolean isInternetUp) {
        if (isInternetUp) {
            binding.llOfflineAddActivityAddress.setVisibility(View.INVISIBLE);
        } else {
            binding.llOfflineAddActivityAddress.setVisibility(View.VISIBLE);
            binding.etAddActivityAddress.setVisibility(View.INVISIBLE);
        }

    }

    private void initImages(long id) {
        adapter = new PhotoPropertyAdapter(galleryToShow, this);
        rvGallery.setAdapter(adapter);
        rvGallery.setLayoutManager(new LinearLayoutManager(AddPropertyActivity.this, LinearLayoutManager.HORIZONTAL, false));
        this.realEstateManagerViewModel.getGallery(id).observe(this, gallery -> {
            adapter.setData(gallery);
        });
    }

    private void getCurrentProperty(long id) {
        this.realEstateManagerViewModel.getProperty(id).observe(this, this::prepopulateTextView);
    }

    private void prepopulateTextView(Property property) {
        String cat = property.getCategory();
        ArrayAdapter myAdapter = (ArrayAdapter) spCategory.getAdapter();
        int spPos = myAdapter.getPosition(cat);
        final String adresse = property.getAddress() + "\n";
        Pattern pattern = Pattern.compile("(\\d{1,4}\\s)" +
                "(([a-zA-Z-éÉèÈàÀùÙâÂêÊîÎôÔûÛïÏëËüÜçÇæœ'.]*\\s)*)" +
                "(\\d{1,5}\\s)" +
                "([a-zA-Z-éÉèÈàÀùÙâÂêÊîÎôÔûÛïÏëËüÜçÇæœ'.]*\\s)");
        System.out.println("/// " + adresse);
        Matcher matcher = pattern.matcher(adresse);
        if (matcher.find()) {
            etStreetNumber.setText(String.valueOf(matcher.group(1)));
            etstreetName.setText(String.valueOf(matcher.group(2)));
            etZipCode.setText(String.valueOf(matcher.group(4)));
            etCity.setText(String.valueOf(matcher.group(5)));
        }
        spCategory.setSelection(spPos);
        etSurface.setText(String.valueOf(property.getSurface()));
        etPrix.setText(String.valueOf(property.getPrice()));
        etRooms.setText(String.valueOf(property.getNbRooms()));
        etBathrooms.setText(String.valueOf(property.getNbBathrooms()));
        etBedRooms.setText(String.valueOf(property.getNbBedrooms()));
        cbSchool.setChecked(property.isSchool());
        cbBusiness.setChecked(property.isBusiness());
        cbParks.setChecked(property.isPark());
        cbPublicTransports.setChecked(property.isPublicTransport());
        etDescription.setText(String.valueOf(property.getDescription()));
        Glide.with(this).load(property.getIllustration()).into(ivIllustration);
        autocompleteFragment.setText(property.getAddress());
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
                if (id == 0 ||id == -1) {
//                    realEstateManagerViewModel.deleteAllProperties();
                    if (verifyInputs()) {
                        Date date = Calendar.getInstance().getTime();
                        dateOfEntry = new SimpleDateFormat("dd-MM-yyyy").format(date);
                        propertyTest = new Property();
                        propertyTest.setDateOfEntry(dateOfEntry);
                        propertyTest.setCategory(category);
                        propertyTest.setPrice(price);
                        propertyTest.setSurface(surface);
                        propertyTest.setNbRooms(nbRooms);
                        propertyTest.setNbBathrooms(nbBathRooms);
                        propertyTest.setNbBedrooms(nbBedRooms);
                        propertyTest.setDescription(description);
                        propertyTest.setStatus("disponible");
                        propertyTest.setAgentName(propertyUserName);
                        propertyTest.setSchool(school);
                        propertyTest.setBusiness(business);
                        propertyTest.setPark(park);
                        propertyTest.setAddress(address);
                        propertyTest.setIllustration(illustration);
                        propertyTest.setPublicTransport(publicTransport);
                        propertyTest.setIllustration(illustration);
                        realEstateManagerViewModel.createProperty(propertyTest);
                        System.out.println("/// success" + propertyTest);
                        startActivity(new Intent(AddPropertyActivity.this, MainActivity.class));
                        finish();
                    }
                } else {
                    if (verifyInputs()) {
                        addPhotos(id);
                        updateProperty(propertyToEdit);
                        System.out.println("/// success update" + propertyTest);
                        startActivity(new Intent(AddPropertyActivity.this, MainActivity.class));
                        finish();
                    }
                }
            }
        });
    }

    private void updateProperty(Property property) {
        if(cbStatus.isChecked()) {
            Date date = Calendar.getInstance().getTime();
            dateSold = new SimpleDateFormat("dd-MM-yyyy").format(date);
            status = "Vendu";
        }
        EditText autocompleteEditText = (EditText) autocompleteFragment.getView().findViewById(getResources().getIdentifier("places_autocomplete_search_input", "id", getPackageName()));
        address = autocompleteEditText.getText().toString();

        realEstateManagerViewModel.updateProperty(category, price,surface, address,
                nbRooms, nbBathRooms, nbBedRooms, description, status, "Leoo",
                school, business, park, publicTransport, dateOfEntry, dateSold);
    }

    private void addPhotos(long propertyId) {
        PropertyPhotos photoToAddToDb;
        System.out.println("/// " + galleryToShow.size());
        for (int i = 0; i < galleryToShow.size(); i++) {
            photoToAddToDb = galleryToShow.get(i);
            photoToAddToDb.setPropertyId(propertyId);
            photoToAddToDb.setPhotoUrl(galleryToShow.get(i).getPhotoUrl());
            photoToAddToDb.setPhotoDescription(galleryToShow.get(i).getPhotoDescription());
            realEstateManagerViewModel.createPropertyPhoto(photoToAddToDb);
        }
    }

    private boolean verifyInputs() {
        Boolean isOk;
        propertyUserName = "Leoo";
        if (category.isEmpty() || category.equals("Selectionnez une categorie")) {
            Toast.makeText(this, "Veuillez indiquer la catégorie du bien", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (Float.toString(surface).isEmpty()) {
            Toast.makeText(this, "Veuillez saisir le surface du bien", Toast.LENGTH_LONG).show();
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

        btnIllustration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a dialog
                if (!checkPermissions()) {
                    requestPermissions();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(AddPropertyActivity.this);
                builder.setTitle("Add illustration");
                builder.setMessage("Select a source to add illustration");

                // Set the positive button to open the gallery
                builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Open the gallery
                        Intent galleryIntent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent2, ILLUSTRATION_REQUEST_CODE);
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

        if (!isInternetUp) {
            streetNumber = etStreetNumber.getText().toString();
            streetName = etstreetName.getText().toString();
            zipCode = etZipCode.getText().toString();
            city = etCity.getText().toString();
            address = "" + streetNumber + " " + streetName + " " + zipCode + " " + city;
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
        cbStatus = binding.cbStatus;
        btnIllustration = binding.btnAddActivityAddIllustration;
        ivIllustration = binding.ivIllustration;
        autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void getGoogleAddress() {

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
                System.out.println("/// " + address);
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
            //New dialog to add a description to the image
            photoToAdd = new PropertyPhotos(1651, "", photoUrl);
            descriptionDialog(photoToAdd);
            galleryToShow.add(photoToAdd);
            showSelectedImages();
        } else if (requestCode == ILLUSTRATION_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            illustration = getPathFromUri(selectedImage);
            Glide.with(AddPropertyActivity.this).load(data).load(selectedImage).into(ivIllustration);
        }
    }

    private void descriptionDialog(PropertyPhotos photoToAdd) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Description de l'image");

// Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                imageDescription = input.getText().toString();
                photoToAdd.setPhotoDescription(imageDescription);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.realEstateManagerViewModel = new ViewModelProvider(this, viewModelFactory).get(RealEstateManagerViewModel.class);
        this.realEstateManagerViewModel.init(PROPERTY_ID);
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