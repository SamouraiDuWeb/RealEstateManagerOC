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
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.database.dao.PropertyDao;
import com.openclassrooms.realestatemanager.databinding.SearchActivityBinding;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.viewModel.RealEstateManagerViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity  {

    private static final long PROPERTY_ID = 1;
    public static final String BUNDLE_RESULT_LIST = "BUNDLE_RESULT_LIST";
    private SearchActivityBinding binding;
    private RealEstateManagerViewModel realEstateManagerViewModel;

    private EditText etMinPrice, etMaxPrice, etMinsurface, etMaxSurface, etMinNbRooms, etMaxNbRooms;
    private CheckBox cbSchool, cbCommerce, cbTransports, cbParks;
    private Button btnFilter;

    //For Data
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
        launchQuery();
    }

    private void launchQuery() {

        String queryString = "";

        List<Object> args = new ArrayList<>();

        boolean containsCondition = false;

        queryString += "SELECT * FROM Property";

        if(!String.valueOf(etMinPrice.getText()).equals("") && !String.valueOf(etMaxPrice.getText()).equals("")) {
            miniPrice = Integer.parseInt(String.valueOf(etMinPrice.getText()));
            maxiPrice = Integer.parseInt(String.valueOf(etMaxPrice.getText()));
            queryString += " WHERE";
            queryString += " price BETWEEN ? AND ?";
            args.add(miniPrice);
            args.add(maxiPrice);
            containsCondition = true;
        }

        if(!String.valueOf(etMinsurface.getText()).equals("") && !String.valueOf(etMaxSurface.getText()).equals("")) {
            miniSurface = Integer.parseInt(String.valueOf(etMinsurface.getText()));
            maxiSurface = Integer.parseInt(String.valueOf(etMaxSurface.getText()));
            queryString += " AND";
            queryString += " surface BETWEEN ? AND ?";
            args.add(miniSurface);
            args.add(maxiSurface);
            containsCondition = true;
        }

        if(!String.valueOf(etMinNbRooms.getText()).equals("") && !String.valueOf(etMaxNbRooms.getText()).equals("")) {
            miniRoom = Integer.parseInt(String.valueOf(etMinNbRooms.getText()));
            maxiRoom = Integer.parseInt(String.valueOf(etMaxNbRooms.getText()));
            queryString += " AND";
            queryString += " nbRooms BETWEEN ? AND ?";
            args.add(miniRoom);
            args.add(maxiRoom);
            containsCondition = true;
        }

        queryString += ";";

        System.out.println("/// " + queryString);

        SimpleSQLiteQuery query = new SimpleSQLiteQuery(queryString, args.toArray());
        List<Property> properties = RealEstateManagerDatabase.getINSTANCE(this).propertyDao().getSearchedProperty(query);
        getSearchedList(properties);
    }

    public void getSearchedList(List<Property> properties) {
        if (properties.isEmpty()) {
            Toast.makeText(this, "Aucun bien ne correspond", Toast.LENGTH_LONG).show();
            System.out.println("/// " + properties);
        } else {
            Intent intent = new Intent();
            intent.putExtra(BUNDLE_RESULT_LIST, (Serializable) properties);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void initView() {
        etMinPrice = binding.etSearchActivityPriceMini;
        etMaxPrice = binding.etSearchActivityPriceMax;
        etMinsurface = binding.etSearchActivityMiniArea;
        etMaxSurface = binding.etSearchActivityMaxiArea;
        etMinNbRooms = binding.etSearchActivityMiniRoom;
        etMaxNbRooms = binding.etSearchActivityMaxiRoom;
        btnFilter = binding.btSearchActivityFilter;
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.realEstateManagerViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateManagerViewModel.class);
        this.realEstateManagerViewModel.init(PROPERTY_ID);
    }

}
