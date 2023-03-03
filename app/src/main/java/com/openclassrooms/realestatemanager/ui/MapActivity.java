package com.openclassrooms.realestatemanager.ui;

import static com.google.android.material.internal.ContextUtils.getActivity;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
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
import com.google.android.gms.tasks.Task;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.MapActivityBinding;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.viewModel.RealEstateManagerViewModel;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, Serializable {

    private MapActivityBinding binding;

    public static final String BUNDLE_HOUSE_CLICKED = "BUNDLE_HOUSE_CLICKED";
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
    }

    private void updateList(List<Property> properties) {
        propertyList = new ArrayList<>();
        propertyList.addAll(properties);
        setMarker();
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
            intent.putExtra(BUNDLE_HOUSE_CLICKED, property);
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
        checkCondition();
        getAllPropertyFromDatabase();
        googleMap.setOnMarkerClickListener(this);
    }

    private void checkCondition() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }
        Toast.makeText(this, "Vous n'êtes pas géolocalisé, activez la géolocalistion! ", Toast.LENGTH_LONG).show();
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        //Initialize location manager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //When location service is enabled
            //Get Last Location
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //Initialize location
                    Location location = task.getResult();
                    //Check condition
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lng = location.getLongitude();
                        currentPosition = new LatLng(lat, lng);
                    }
                }
            });
        }
    }


}
