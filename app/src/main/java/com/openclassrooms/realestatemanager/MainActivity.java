package com.openclassrooms.realestatemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.ui.AddPropertyActivity;
import com.openclassrooms.realestatemanager.ui.LoginActivity;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        initBottomNavigation();

        initPlaceApi();

        setListeners();
    }

    private void setListeners() {
        binding.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open search edittext w/ Google autocomplete
            }
        });

        binding.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddPropertyActivity.class));
            }
        });
    }

    private void initPlaceApi() {
        // Initialize the SDK
        Places.initialize(getApplicationContext(), "AIzaSyCrqJLy46KdQ7fp3LH8IJZaFW2tAM2bAIM");
        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);
    }

    private void initBottomNavigation() {

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_map, R.id.navigation_list, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        Toolbar toolbar = binding.toolbarMain;
        setSupportActionBar(toolbar);
    }
}
