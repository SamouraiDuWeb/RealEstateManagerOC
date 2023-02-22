package com.openclassrooms.realestatemanager.ui;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapter.PropertyListAdapter;
import com.openclassrooms.realestatemanager.databinding.FragmentListBinding;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.viewModel.RealEstateManagerViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private FragmentListBinding binding;
    private RealEstateManagerViewModel realEstateManagerViewModel;
    private PropertyListAdapter adapter;
    private RecyclerView rvProperties;
    private FirebaseUser currentUser;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private List<Property> propertyList = new ArrayList<Property>();


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = FragmentListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        initView();
        configureViewModel();
        intiRecyclerView();
        setListeners();
        configureToolBar();
        configureDrawerLayout();
    }

    private void configureToolBar() {
        this.toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
    }

    private void configureDrawerLayout() {
        this.drawerLayout = findViewById(R.id.activity_list_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
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
                startActivity(new Intent(ListActivity.this, AddPropertyActivity.class));
            }
        });
    }

    private void initView() {
        rvProperties = binding.rvProperties;
    }

    private void intiRecyclerView() {
        adapter = new PropertyListAdapter(propertyList, ListActivity.this);
        rvProperties.setAdapter(adapter);
        rvProperties.setLayoutManager(new LinearLayoutManager(this));
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        realEstateManagerViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateManagerViewModel.class);
        realEstateManagerViewModel.getAll().observe(this, propertyList -> {
            adapter.setData(propertyList);
        });
    }


}
