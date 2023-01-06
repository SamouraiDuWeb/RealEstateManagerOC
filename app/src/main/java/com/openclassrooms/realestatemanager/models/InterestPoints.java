package com.openclassrooms.realestatemanager.models;

import com.google.android.libraries.places.api.model.Place;

import java.io.Serializable;

//class place with fewer attributes
public class InterestPoints implements Serializable {

    private Place place;

    private String name;

    private String type;

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
