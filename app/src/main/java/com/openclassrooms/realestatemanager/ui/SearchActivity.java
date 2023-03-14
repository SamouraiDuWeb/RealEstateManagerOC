package com.openclassrooms.realestatemanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.openclassrooms.realestatemanager.databinding.SearchActivityBinding;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.viewModel.RealEstateManagerViewModel;

import java.io.Serializable;
import java.util.List;

public class SearchActivity extends AppCompatActivity  {

    private static final long HOUSE_ID = 1;
    public static final String BUNDLE_RESULT_LIST = "BUNDLE_RESULT_LIST";
    private SearchActivityBinding binding;
    private RealEstateManagerViewModel realEstateManagerViewModel;

    private EditText etCp, etMinPrice, etMaxPrice, etMinsurface, etMaxSurface, etMinNbRooms, etMaxNbRooms, etMinPhotos, etMaxPhotos;
    private CheckBox cbSchool, cbCommerce, cbTransports, cbParks, cbSold;
    private Button btnFilter;

    //For Data
    private String zipCode;
    private int miniPrice;
    private int maxiPrice;
    private boolean school;
    private boolean business;
    private boolean publicTransport;
    private boolean park;
    private int miniSurface;
    private int maxiSurface;
    private int miniRoom;
    private int maxiRoom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SearchActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureViewModel();
        initView();
        btnFilter.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        getInputs();
        if (checkInput()) {
            realEstateManagerViewModel.getSearchedProperty(
                    miniPrice,
                    maxiPrice,
                    miniSurface,
                    maxiSurface,
                    miniRoom,
                    maxiRoom,
                    school,
                    business,
                    publicTransport,
                    park
            ).observe(this, this::getSearchedList);
        }
    }

    public void getSearchedList(List<Property> properties) {
        if (properties.isEmpty()) {
            Toast.makeText(this, "Aucun bien ne correspond", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent();
            intent.putExtra(BUNDLE_RESULT_LIST, (Serializable) properties);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private boolean checkInput() {
        Boolean isOk;

        if (miniPrice == 0) {
            Toast.makeText(this, "Saisir le prix minimum du bien", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (miniPrice == 0) {
            Toast.makeText(this, "Saisir le prix maximum du bien", Toast.LENGTH_LONG).show();
            isOk = false;
        } else {
            isOk = true;
        }
        return isOk;
    }

    private void getInputs() {
        String miniPriceInput = etMinPrice.getText().toString();
        String maxiPriceInput = etMaxPrice.getText().toString();
        String miniSurfaceInput = etMinsurface.getText().toString();
        String maxiSurfaceInput = etMaxSurface.getText().toString();
        String miniRoomInput = etMinNbRooms.getText().toString();
        String maxiRoomInput = etMaxNbRooms.getText().toString();

        if (!TextUtils.isEmpty(miniPriceInput)) {
            miniPrice = Integer.parseInt(miniPriceInput);
        }
        if (!TextUtils.isEmpty(maxiPriceInput)) {
            maxiPrice = Integer.parseInt(maxiPriceInput);
        }
        if (!TextUtils.isEmpty(miniSurfaceInput)) {
            miniSurface = Integer.parseInt(miniSurfaceInput);
        }
        if (!TextUtils.isEmpty(maxiSurfaceInput)) {
            maxiSurface = Integer.parseInt(maxiSurfaceInput);
        }

        if (!TextUtils.isEmpty(miniRoomInput)) {
            miniRoom = Integer.parseInt(miniRoomInput);
        }
        if (!TextUtils.isEmpty(maxiRoomInput)) {
            maxiRoom = Integer.parseInt(maxiRoomInput);
        }
    }

    private void initView() {
        etCp = binding.etSearchActivityDistrict;
        etMinPrice = binding.etSearchActivityPriceMini;
        etMaxPrice = binding.etSearchActivityPriceMax;
        etMinsurface = binding.etSearchActivityMiniArea;
        etMaxSurface = binding.etSearchActivityMaxiArea;
        etMinNbRooms = binding.etSearchActivityMiniRoom;
        etMaxNbRooms = binding.etSearchActivityMaxiRoom;
        etMinPhotos = binding.etNbPhotos;
        btnFilter = binding.btSearchActivityFilter;
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.realEstateManagerViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateManagerViewModel.class);
        this.realEstateManagerViewModel.init(HOUSE_ID);
    }

}
