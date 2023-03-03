package com.openclassrooms.realestatemanager.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.window.layout.WindowMetrics;
import androidx.window.layout.WindowMetricsCalculator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.databinding.MainActivityBinding;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.ui.fragments.DetailFragment;
import com.openclassrooms.realestatemanager.ui.fragments.ListFragment;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding binding;
    private FirebaseUser currentUser;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private WindowSizeClass widthWindowSizeClass;
    private ListFragment listFragment;
    private DetailFragment detailFragment;
    private long id;

    public enum WindowSizeClass { COMPACT, MEDIUM, EXPANDED }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        configureAndShowListFragment();

        // Add a utility view to the container to hook into
        // View.onConfigurationChanged. This is required for all
        // activities, even those that don't handle configuration
        // changes. We also can't use Activity.onConfigurationChanged,
        // since there are situations where that won't be called when
        // the configuration changes. View.onConfigurationChanged is
        // called in those scenarios.
        ViewGroup container = binding.getRoot();
        container.addView(new View(this) {
            @Override
            protected void onConfigurationChanged(Configuration newConfig) {
                super.onConfigurationChanged(newConfig);
                computeWindowSizeClasses();
            }
        });
        computeWindowSizeClasses();
        setToolbar();
        configureDrawerLayout();
    }



    private void configureDrawerLayout() {
        this.drawerLayout = findViewById(R.id.activity_list_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setToolbar() {
        toolbar = binding.toolbarMain;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.ivEdit.setVisibility(View.GONE);
        binding.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPropertyActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void computeWindowSizeClasses() {
        WindowMetrics metrics = WindowMetricsCalculator.getOrCreate()
                .computeCurrentWindowMetrics(this);

        float widthDp = metrics.getBounds().width() /
                getResources().getDisplayMetrics().density;

        if (widthDp < 600f) {
            widthWindowSizeClass = WindowSizeClass.COMPACT;
        } else if (widthDp < 840f) {
            widthWindowSizeClass = WindowSizeClass.MEDIUM;
        } else {
            widthWindowSizeClass = WindowSizeClass.EXPANDED;
        }

        // Use widthWindowSizeClass and heightWindowSizeClass.
    }

    public void configureAndShowListFragment() {
        // Get FragmentManager (support) and Try to find existing instance of fragment in FrameLayout container
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frag1);
        if (!(fragment instanceof ListFragment)) {
            listFragment = new ListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frag1, listFragment)
                    .commit();
        }
    }

    //Transmission of the information of the clicked property for the display of details on DetailsFragment
    public void onPropertyClick(Property property) {

        if (detailFragment != null && Utils.isTablet(this)) {
            //Tablet
            detailFragment.updateData(property);

        } else {
            //Smartphone
            detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.frag2);
            detailFragment = new DetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frag1, detailFragment)
                    .addToBackStack(null)
                    .commit();
            detailFragment.onPropertyClick(property);
        }
        this.id = property.getId();
    }
}
