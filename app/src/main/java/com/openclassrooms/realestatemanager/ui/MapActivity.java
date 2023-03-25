package com.openclassrooms.realestatemanager.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.MapActivityBinding;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.viewModel.RealEstateManagerViewModel;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, Serializable {

    private MapActivityBinding binding;

    public static final String BUNDLE_PROPERTY_CLICKED = "BUNDLE_PROPERTY_CLICKED";
    private static final long PROPERTY_ID = 1;
    private GoogleMap googleMap;
    private FusedLocationProviderClient client;
    private RealEstateManagerViewModel realEstateManagerViewModel;
    private List<Property> propertyList = new ArrayList<>();
    private LatLng propertyLatLng;
    private LatLng currentPosition;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MapActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        //Async map
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }
        checkAndRequestPermissions();

        //Initialize location client
        client = LocationServices.getFusedLocationProviderClient(this);

        this.configureViewModel();
        this.getAllPropertyFromDatabase();
        this.setToolBar();

    }

    private void setToolBar() {
        Toolbar toolbar = binding.toolbarMap;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void getAllPropertyFromDatabase() {
        this.realEstateManagerViewModel.getAll().observe(this, this::updateList);
        // does not get all the properties
    }

    private void updateList(List<Property> properties) {
        propertyList = new ArrayList<>();
        propertyList.addAll(properties);
    }

    private void setMarker() {
        for (Property property : propertyList) {
            String address = property.getAddress();
            Geocoder coder = new Geocoder(this);
            List<Address> addresses;
            try {
                addresses = coder.getFromLocationName(address, 10);
                if (addresses == null) {
                }
                Address location = addresses.get(0);
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                propertyLatLng = new LatLng(lat, lng);
                if (propertyLatLng != null) {
                    marker = googleMap.addMarker(new MarkerOptions()
                            .position(propertyLatLng));
                    marker.setTag(property.getId());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (currentPosition != null) {
            marker = googleMap.addMarker(new MarkerOptions()
                    .position(currentPosition)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Ma position"));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 12));
        } else if (propertyLatLng != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(propertyLatLng, 12));
        }
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.realEstateManagerViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateManagerViewModel.class);
        this.realEstateManagerViewModel.init(PROPERTY_ID);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Long id = (Long) marker.getTag();
        if (id != null) {
            Property property = getPropertyById(id);
            Intent intent = new Intent();
            intent.putExtra(BUNDLE_PROPERTY_CLICKED, property);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Ceci est ma position", Toast.LENGTH_LONG).show();
        }

        return false;
    }

    private Property getPropertyById(Long id) {
        for (Property property : propertyList) {
            if (property.getId() == id)
                return property;
        }
        return null;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        checkCondition();
        getAllPropertyFromDatabase();
        googleMap.setOnMarkerClickListener(this);
    }

    private void checkAndRequestPermissions() {
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                            } else {
                                // No location access granted.
                            }
                        }
                );

// ...

// Before you perform the actual permission request, check whether your app
// already has the permissions, and whether your app needs to show a permission
// rationale dialog. For more details, see Request permissions.
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

//    private void addUserMarker(LatLng currentPosition) {
//        Marker userMarker;
//        userMarker = googleMap.addMarker(new MarkerOptions()
//                .position(currentPosition));
//        userMarker.setTag("Vous êtes ici");
//    }

    private void checkCondition() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            Toast.makeText(this, "Vous n'êtes pas géolocalisé, activez la géolocalistion! ", Toast.LENGTH_LONG).show();
        }
    }

//    @SuppressLint("MissingPermission")
//    private void getCurrentLocation() {
//        //Initialize location manager
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        //check condition
//        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//            //When location service is enabled
//            //Get Last Location
//            System.out.println("/// " + client.getLastLocation());
//            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//                @Override
//                public void onComplete(@NonNull Task<Location> task) {
//                    //Initialize location
//                    Location location = task.getResult();
//                    //Check condition
//                    if (location != null) {
//                        double lat = location.getLatitude();
//                        double lng = location.getLongitude();
//                        currentPosition = new LatLng(lat, lng);
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    System.out.println("//!\\ " + e.getMessage());
//                }
//            });
//        }
//    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        FusedLocationProviderClient mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            final Task<Location> location = mfusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    System.out.println("/// " + "find location");
                    try {
                        //Initialize location
                        Location location1 = task.getResult();
                        //Check condition
                        if (location1 != null) {
                            double lat = location1.getLatitude();
                            double lng = location1.getLongitude();
                            currentPosition = new LatLng(lat, lng);
                        }
                        setMarker();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Please turn on your GPS", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    System.out.println("/// " + "Cannot find current location");
                }
            });
        } catch (SecurityException e) {
            System.out.println("getDeviceLocation: Security Exception" + e.getMessage());
        }
    }

}
