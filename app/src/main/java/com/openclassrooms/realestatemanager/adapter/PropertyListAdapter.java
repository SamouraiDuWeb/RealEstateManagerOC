package com.openclassrooms.realestatemanager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyPhotos;
import com.openclassrooms.realestatemanager.ui.DetailProperty;
import com.openclassrooms.realestatemanager.viewModel.RealEstateManagerViewModel;

import java.util.ArrayList;
import java.util.List;

public class PropertyListAdapter extends RecyclerView.Adapter<PropertyListAdapter.ViewHolder>{

    private List<Property> propertyList;
    private Context context;
    private RealEstateManagerViewModel realEstateManagerViewModel;

    public PropertyListAdapter(List<Property> propertyList, Context context) {
        this.propertyList = propertyList;
        this.context = context;
    }

    @NonNull
    @Override
    public PropertyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_item, parent, false);
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(context);
        realEstateManagerViewModel = ViewModelProviders.of((FragmentActivity) context, viewModelFactory).get(RealEstateManagerViewModel.class);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        context = holder.itemView.getContext();
        holder.updateProperty(propertyList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Property currentProperty = propertyList.get(position);
                Intent intent = new Intent(context, DetailProperty.class);
                intent.putExtra("detail property", currentProperty);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public void setData(List<Property> newData) {
        if (propertyList != null) {
            propertyList.clear();
            propertyList.addAll(newData);
            notifyDataSetChanged();
        } else {
            propertyList = newData;
        }
    }

    public List<Property> getData() {
        return propertyList;
    }

    public void removeItem(int position) {
        long id = propertyList.get(position).getId();
        propertyList.remove(position);
        realEstateManagerViewModel.deleteProperty(id);
        notifyItemRemoved(position);
    }

    public void restoreItem(Property item, int position) {
        propertyList.add(position, item);
        realEstateManagerViewModel.createProperty(item);
        notifyItemInserted(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView propertyPhoto;
        TextView propertyAddress, propertyPrice, propertyName;
        List<PropertyPhotos> gallery = new ArrayList<>();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            propertyPhoto = itemView.findViewById(R.id.item_property_photo);
            propertyAddress = itemView.findViewById(R.id.item_property_city);
            propertyName = itemView.findViewById(R.id.item_property_name);
            propertyPrice = itemView.findViewById(R.id.item_property_price);
        }

        public void updateProperty(Property property) {

            propertyName.setText(property.getCategory());
            propertyPrice.setText(String.valueOf((int) property.getPrice()));
            propertyAddress.setText((CharSequence) property.getAddress());
        }
    }
}
