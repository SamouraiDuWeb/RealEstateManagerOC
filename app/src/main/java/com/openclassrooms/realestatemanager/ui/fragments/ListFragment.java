package com.openclassrooms.realestatemanager.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.adapter.PropertyListAdapter;
import com.openclassrooms.realestatemanager.adapter.SwipeToDeleteCallback;
import com.openclassrooms.realestatemanager.databinding.FragmentListBinding;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.ui.MainActivity;
import com.openclassrooms.realestatemanager.viewModel.RealEstateManagerViewModel;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment implements PropertyListAdapter.OnPropertyListener{

    private static final int SEARCH_ACTIVITY_REQUEST_CODE = 101;
    private static final String BUNDLE_RESULT_LIST = "BUNDLE_RESULT_LIST";
    private FragmentListBinding binding;
    private RealEstateManagerViewModel realEstateManagerViewModel;
    private PropertyListAdapter adapter;
    private RecyclerView rvProperties;
    private List<Property> propertyList = new ArrayList<>();

    private SwipeToDeleteCallback swipeToDeleteCallback;

    public ListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        rvProperties = binding.rvProperty;
        initRecyclerView();
        configureViewModel();

        enableSwipeToDeleteAndUndo();


        return view;
    }

    private void initRecyclerView() {
        adapter = new PropertyListAdapter(this.propertyList, getContext(), this);
        rvProperties.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvProperties.setHasFixedSize(false);
        rvProperties.setAdapter(adapter);
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        realEstateManagerViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateManagerViewModel.class);
        realEstateManagerViewModel.getAll().observe(getActivity(), propertyList -> {
            adapter.setData(propertyList);
            updateDisplay();
        });
    }

    private void enableSwipeToDeleteAndUndo() {
        swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final Property item = adapter.getData().get(position);

                adapter.removeItem(position);


                Snackbar snackbar = Snackbar
                        .make(binding.getRoot(), "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", view -> {
                    adapter.restoreItem(item, position);
                    rvProperties.scrollToPosition(position);
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(rvProperties);
    }

    private void updateDisplay() {
        if (propertyList.size() == 0) {
//            lblNoProperty.setVisibility(View.VISIBLE);
//            rvProperties.setVisibility(View.GONE);
        } else {
//            lblNoProperty.setVisibility(View.GONE);
            rvProperties.setVisibility(View.VISIBLE);
        }
        // same infos all
    }

    @Override
    public void onPropertyClick(int position) {
        ((MainActivity) getActivity()).onPropertyClick(propertyList.get(position));
    }

    //Use to display the search properties recover from searchActivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (SEARCH_ACTIVITY_REQUEST_CODE == requestCode && Activity.RESULT_OK == resultCode) {
            List<Property> searchPropertyList = (List<Property>) data.getSerializableExtra(BUNDLE_RESULT_LIST);
            adapter.setData(searchPropertyList);
        }
    }
}
