package com.openclassrooms.realestatemanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapter.PhotoPropertyAdapter;
import com.openclassrooms.realestatemanager.databinding.ActivityAddPropertyBinding;
import com.openclassrooms.realestatemanager.databinding.ActivityDetailPropertyBinding;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyPhotos;
import com.openclassrooms.realestatemanager.viewModel.RealEstateManagerViewModel;

import java.util.ArrayList;
import java.util.List;

public class DetailProperty extends AppCompatActivity {

    private long HOUSE_ID = 1;
    private ActivityDetailPropertyBinding binding;
    private RealEstateManagerViewModel realEstateManagerViewModel;
    private PhotoPropertyAdapter adapter;
    private List<PropertyPhotos> gallery = new ArrayList<>();

    private TextView tvAddress, tvSurface, tvNbRooms, tvNbBathrooms, tvNbBedrooms, tvDescription, tvInerestPoints, tvAgentName, tvDateEntry;
    private ImageView ivMapView, ivEdit;
    private RecyclerView rvPropertyPhotos;
    private LinearLayout llSchool, llBusiness, llParks, llPublicTransports;

    private Property property;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailPropertyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        extras = getIntent().getExtras();
        property = (Property) extras.get("detail property");
        HOUSE_ID = property.getId();
        configureViewModel();
        setToolbar();
        initInputs();
        getInfos();
    }

    private void getInfos() {
        System.out.println("/// " + property);

        String address = property.getSurface() + " m²";
        String nbRooms = "" + property.getNbRooms();
        String nbBathrooms = "" + property.getNbBathrooms();
        String nbBedrooms = "" + property.getNbBedrooms();
        boolean school = property.isSchool();
        boolean business = property.isBusiness();
        boolean parks = property.isPark();
        boolean publicTransport = property.isPublicTransport();
        if (!school) {
            System.out.println("/// pas d'écoles a proximité");
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llSchool.getLayoutParams();
            params.width = (int) (0);
            llSchool.setLayoutParams(params);
        }


        //set inputs
        tvAddress.setText(property.getAddress());
        tvSurface.setText(address);
        tvNbRooms.setText(nbRooms);
        tvNbBathrooms.setText(nbBathrooms);
        getGalleryPropertyFromDatabase(property.getId());

    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailProperty.this, AddPropertyActivity.class);
                intent.putExtra("current_property", property);
                intent.putExtra("id", property.getId());
                DetailProperty.this.startActivity(intent);
            }
        });
    }

    private void initInputs() {
        tvAddress = binding.tvPropertyAddressText;
        tvSurface = binding.tvDetailSurfaceText;
        tvNbRooms = binding.tvDetailNbRoomText;
        tvNbBathrooms = binding.tvDetailNbBathroomText;
        tvNbBedrooms = binding.tvDetailNbBedroomText;
        tvDescription = binding.tvDetailDescriptionText;
        tvAgentName = binding.tvDetailAgentName;
        tvDateEntry = binding.tvDetailDateEntryText;
        ivMapView = binding.mapImageView;
        llSchool = binding.llSchool;
        llBusiness = binding.llBusiness;
        llParks = binding.llParks;
        llPublicTransports = binding.llPublicTransports;
        rvPropertyPhotos = binding.rvDetailPhotos;
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(DetailProperty.this);
        this.realEstateManagerViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateManagerViewModel.class);
        this.realEstateManagerViewModel.init(HOUSE_ID);
    }

    private void getGalleryPropertyFromDatabase(long propertyId) {
        adapter = new PhotoPropertyAdapter(gallery, this);
        rvPropertyPhotos.setAdapter(adapter);
        rvPropertyPhotos.setLayoutManager(new LinearLayoutManager(this));
        this.realEstateManagerViewModel.getAllGallery().observe(this, gallery -> {
            adapter.setData(gallery);
        });
        System.out.println("/// " + gallery);
    }

    private void initMap() {
        ImageView mapImageView = binding.mapImageView;
        String mapUrl = "https://maps.googleapis.com/maps/api/staticmap?" +
                "center=40.748817,-73.985428&zoom=12&size=400x400&key=" + "AIzaSyC3g3Y_iaBGYzzho-dJ-B1D4pA2pKD3PYw";

        Glide.with(this)
                .load(mapUrl)
                .centerCrop()
                .into(mapImageView);
    }
}
