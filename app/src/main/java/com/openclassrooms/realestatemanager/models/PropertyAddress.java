package com.openclassrooms.realestatemanager.models;

import java.io.Serializable;

public class PropertyAddress implements Serializable {
    private static final long serialVersionUID = 1L;

    private String address;
    private String city;
    private String state;
    private String zip;

    public PropertyAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
