package com.openclassrooms.realestatemanager.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

    private static final int SEARCH_ACTIVITY_REQUEST_CODE = 101;
    private MainActivityBinding binding;
    private FirebaseUser currentUser;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private WindowSizeClass widthWindowSizeClass;
    private ListFragment listFragment;
    private DetailFragment detailFragment;
    private long id;
    private Fragment currentFragment;

    public enum WindowSizeClass { COMPACT, MEDIUM, EXPANDED }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

//        if (currentUser == null) {
//            startActivity(new Intent(this, LoginActivity.class));
//            finish();
//            return;
//        }

        configureAndShowListFragment();
        configureAndShowDetailFragment();

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
        this.toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the toolbar menu
        getMenuInflater().inflate(R.menu.topbar_menu, menu);
        return true;
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
    public void configureAndShowDetailFragment() {
        detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.frag2);
        // Add detailFragment only if in Tablet mode
        if (detailFragment == null && findViewById(R.id.frag2) != null) {
            detailFragment = new DetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frag2, detailFragment)
                    .commit();
        }
    }

    //Transmission of the information of the clicked property for the display of details on DetailsFragment
    public void onPropertyClick(Property property) {

        if (detailFragment != null && Utils.isTablet(this)) {
            //Tablet
            detailFragment.updateData(property);
            detailFragment.updateDisplay(property);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle actions on menu items
        switch (item.getItemId()) {
            //Start AddActivity when add button is clicked
            case R.id.app_bar_add_property:
                Intent intentAdd = new Intent(MainActivity.this, AddPropertyActivity.class);
                startActivity(intentAdd);
                return true;

            //Start AddActivity when modify button is clicked
            case R.id.app_bar_edit:
                if (id != 0) {
                    Intent intentModify = new Intent(MainActivity.this, AddPropertyActivity.class);
                    intentModify.putExtra("id", id);
                    startActivity(intentModify);
                } else {
                    Toast.makeText(this, "Selectionner un bien ?? vendre", Toast.LENGTH_LONG).show();
                }
                return true;

            //Start SearchActivity when add button is clicked
            case R.id.app_bar_search:
                Intent searchActivityIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(searchActivityIntent, SEARCH_ACTIVITY_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case SEARCH_ACTIVITY_REQUEST_CODE:
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frag1);
                    fragment.onActivityResult(requestCode, resultCode, data);

                    break;
            }
        }
    }
}
