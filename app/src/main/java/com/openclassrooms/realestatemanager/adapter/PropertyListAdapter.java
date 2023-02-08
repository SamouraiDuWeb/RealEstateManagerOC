package com.openclassrooms.realestatemanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Property;

import java.util.List;

public class PropertyListAdapter extends RecyclerView.Adapter<PropertyListAdapter.ViewHolder>{

    private List<Property> propertyList;

    public PropertyListAdapter(List<Property> propertyList) {
        this.propertyList = propertyList;
    }

    @NonNull
    @Override
    public PropertyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyListAdapter.ViewHolder holder, int position) {
        holder.updateProperty(propertyList.get(position));
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


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView propertyPhoto;
        TextView propertyAddress, propertyPrice, propertyName;

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
