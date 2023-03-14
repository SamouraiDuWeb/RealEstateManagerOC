package com.openclassrooms.realestatemanager.ui.fragments;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.adapter.PhotoPropertyAdapter;
import com.openclassrooms.realestatemanager.databinding.FragmentDetailPropertyBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentListBinding;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyPhotos;
import com.openclassrooms.realestatemanager.ui.MapActivity;
import com.openclassrooms.realestatemanager.viewModel.RealEstateManagerViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {

    private long PROPERTY_ID = 1;
    private FragmentDetailPropertyBinding binding;
    private RealEstateManagerViewModel realEstateManagerViewModel;
    private PhotoPropertyAdapter adapter;
    private List<PropertyPhotos> gallery = new ArrayList<>();
    private long id;

    private TextView tvAddress, tvSurface, tvNbRooms, tvNbBathrooms, tvNbBedrooms, tvDescription, tvInerestPoints, tvAgentName, tvDateEntry;
    private ImageView ivMapView, ivEdit;
    private RecyclerView rvPropertyPhotos;
    private LinearLayout llSchool, llBusiness, llParks, llPublicTransports;

    private Property property;
    Bundle extras;

    public DetailFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailPropertyBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        configureViewModel();
        initInputs();
        if (property != null) {
            getInfos(property);
            if (Utils.isConnectingToInternet(getContext())) {
                initMap(property);
            }
        }
        return view;
    }

    private void getInfos(Property property) {
        System.out.println("/// " + property);

        String address = property.getSurface() + " m²";
        String nbRooms = "" + property.getNbRooms();
        String nbBathrooms = "" + property.getNbBathrooms();
        String nbBedrooms = "" + property.getNbBedrooms();
        boolean school = property.isSchool();
        boolean business = property.isBusiness();
        boolean parks = property.isPark();
        boolean publicTransport = property.isPublicTransport();
        String description = property.getDescription();
        String dateEntry = property.getDateOfEntry();
        String agentName = property.getAgentName();
        if (!school) {
            System.out.println("/// pas d'écoles a proximité");
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llSchool.getLayoutParams();
            params.width = (int) (0);
            llSchool.setLayoutParams(params);
        }
        if (!business) {
            System.out.println("/// pas de commerces a proximité");
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llBusiness.getLayoutParams();
            params.width = (int) (0);
            llBusiness.setLayoutParams(params);
        }
        if (!parks) {
            System.out.println("/// pas de parcs a proximité");
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llParks.getLayoutParams();
            params.width = (int) (0);
            llParks.setLayoutParams(params);
        }
        if (!publicTransport) {
            System.out.println("/// pas de transports public a proximité");
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llPublicTransports.getLayoutParams();
            params.width = (int) (0);
            llPublicTransports.setLayoutParams(params);
        }

        //set inputs
        tvAddress.setText(property.getAddress());
        tvSurface.setText(address);
        tvNbRooms.setText(nbRooms);
        tvNbBathrooms.setText(nbBathrooms);
        tvNbBedrooms.setText(nbBedrooms);
        tvDescription.setText(description);
        tvDateEntry.setText(dateEntry);
        tvAgentName.setText(agentName);
        getGalleryPropertyFromDatabase(property.getId());

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
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getActivity());
        this.realEstateManagerViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateManagerViewModel.class);
        this.realEstateManagerViewModel.init(PROPERTY_ID);
    }

    private void getGalleryPropertyFromDatabase(long propertyId) {
        adapter = new PhotoPropertyAdapter(gallery, this.getContext());
        rvPropertyPhotos.setAdapter(adapter);
        rvPropertyPhotos.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, true));
        this.realEstateManagerViewModel.getGallery(propertyId).observe(getActivity(), gallery -> {
            adapter.setData(gallery);
        });
        System.out.println("/// " + gallery);
    }

    private void initMap(Property property) {
        ImageView mapImageView = binding.mapImageView;
        String address = property.getAddress();

        Glide.with(this)
                .load(googleAddressToLatLng(address, "AIzaSyC3g3Y_iaBGYzzho-dJ-B1D4pA2pKD3PYw"))
                .centerCrop()
                .into(mapImageView);

        mapImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MapActivity.class);
                DetailFragment.this.startActivity(i);

            }
        });
    }

    //convert google adress to latitude and longitude
    public String googleAddressToLatLng(String address, String apiKey) {
        Geocoder coder = new Geocoder(getActivity());
        List<Address> addresses;
        try {
            addresses = coder.getFromLocationName(address, 10);
            if (addresses == null) {
            }
            Address location = addresses.get(0);
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            return "https://maps.googleapis.com/maps/api/staticmap?center=" + lat + "," + lng + "&zoom=15&size=200x200" +
                    "&markers=color:red%7C" + lat + "," + lng + "&sensor=false&key=" + apiKey;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onStart() {
        getGalleryPropertyFromDatabase(id);
        //Smartphone
        if (property != null) {
            this.updateData(property);
        }
        //Tablet
        this.updateDisplay(property);
        super.onStart();
    }

    //Update the display
    public void updateDisplay(Property property) {
        if (property == null) {
            getView().setVisibility(View.GONE);
        } else {
            getView().setVisibility(View.VISIBLE);
        }
    }

    //Listener
    public void onPropertyClick(Property property) {
        if (property != null) {
            this.property = property;
            //Use for modify
            this.id = property.getId();
        }
    }

    //Tablet display
    public void updateData(Property property) {
        getInfos(property);
        configureViewModel();
        initMap(property);
        getGalleryPropertyFromDatabase(property.getId());
    }
}
