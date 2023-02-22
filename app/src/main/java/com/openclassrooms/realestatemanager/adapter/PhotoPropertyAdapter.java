package com.openclassrooms.realestatemanager.adapter;

import static com.google.android.material.resources.MaterialResources.getDrawable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.models.PropertyPhotos;

import java.util.ArrayList;
import java.util.List;

public class PhotoPropertyAdapter extends  RecyclerView.Adapter<PhotoPropertyAdapter.ViewHolder> {

    private List<PropertyPhotos> gallery;
    private Context context;

    public PhotoPropertyAdapter(List<PropertyPhotos> gallery, Context context) {
        this.gallery = gallery;
        this.context = context;
    }

    @NonNull
    @Override
    public PhotoPropertyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_photo, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoPropertyAdapter.ViewHolder holder, int position) {
        holder.updatePropertyPhoto(gallery.get(position));
    }

    @Override
    public int getItemCount() {
        return gallery.size();
    }

    public void setData(List<PropertyPhotos> newData) {
        if (gallery != null) {
            gallery.clear();
            gallery.addAll(newData);
        } else {
            gallery = newData;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView propertyPhotoView;
        private String picture;
        private TextView description;

        public ViewHolder(View itemView) {
            super(itemView);

            description = itemView.findViewById(R.id.tv_add_photo_description);
            propertyPhotoView = itemView.findViewById(R.id.iv_add_photo);

        }


        public void updatePropertyPhoto(PropertyPhotos photos) {

            description.setText(photos.getPhotoDescription());

            if (photos.getPhotoUrl().isEmpty()) {
                propertyPhotoView.setImageResource(R.drawable.ic_baseline_add_a_photo_24);
            } else {
                picture = Utils.getPhotoGalleryFromDevice(photos);
                Glide.with(propertyPhotoView.getContext())
                        .load(picture)
                        .centerCrop()
                        .into(propertyPhotoView);
            }
        }
    }
}
