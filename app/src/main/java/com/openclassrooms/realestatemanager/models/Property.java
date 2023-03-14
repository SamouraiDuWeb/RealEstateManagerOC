package com.openclassrooms.realestatemanager.models;

import android.content.ContentValues;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.RoomWarnings;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Property implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String category;

    private float price;

    private float surface;

    private int nbRooms;

    private int nbBathrooms;

    private int nbBedrooms;

    private String description;

    private String address;

    private String status;

    private String dateOfEntry;

    private String dateSold;

    private String agentName;

    private String illustration;

    private String illuDescription;

    private boolean school;

    private boolean business;

    private boolean park;

    private boolean publicTransport;

    public Property() {
    }

    @Ignore
    public Property(String category, float price, float surface, String address, int nbRooms, int nbBathrooms, int nbBedrooms, String description, String status, String agentName, boolean school, boolean business, boolean park, boolean publicTransport, String dateOfEntry, String dateSold) {
        this.category = category;
        this.address = address;
        this.price = price;
        this.surface = surface;
        this.nbRooms = nbRooms;
        this.nbBathrooms = nbBathrooms;
        this.nbBedrooms = nbBedrooms;
        this.description = description;
        this.status = status;
        this.agentName = agentName;
        this.school = school;
        this.business = business;
        this.park = park;
        this.publicTransport = publicTransport;
        this.dateOfEntry = dateOfEntry;
        this.dateSold = dateSold;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getSurface() {
        return surface;
    }

    public void setSurface(float surface) {
        this.surface = surface;
    }

    public int getNbRooms() {
        return nbRooms;
    }

    public void setNbRooms(int nbRooms) {
        this.nbRooms = nbRooms;
    }

    public int getNbBathrooms() {
        return nbBathrooms;
    }

    public void setNbBathrooms(int nbBathrooms) {
        this.nbBathrooms = nbBathrooms;
    }

    public int getNbBedrooms() {
        return nbBedrooms;
    }

    public void setNbBedrooms(int nbBedrooms) {
        this.nbBedrooms = nbBedrooms;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    public Date getDateEntry() {
//        return dateEntry;
//    }
//
//    public void setDateEntry(Date dateEntry) {
//        this.dateEntry = dateEntry;
//    }
//
//    public Date getDateSold() {
//        return dateSold;
//    }
//
//    public void setDateSold(Date dateSold) {
//        this.dateSold = dateSold;
//    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public boolean isSchool() {
        return school;
    }

    public void setSchool(boolean school) {
        this.school = school;
    }

    public boolean isBusiness() {
        return business;
    }

    public void setBusiness(boolean business) {
        this.business = business;
    }

    public boolean isPark() {
        return park;
    }

    public void setPark(boolean park) {
        this.park = park;
    }

    public boolean isPublicTransport() {
        return publicTransport;
    }

    public void setPublicTransport(boolean publicTransport) {
        this.publicTransport = publicTransport;
    }

    public String getDateOfEntry() {
        return dateOfEntry;
    }

    public void setDateOfEntry(String dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }

    public String getDateSold() {
        return dateSold;
    }

    public void setDateSold(String dateSold) {
        this.dateSold = dateSold;
    }


    //Utils
    public static Property fromContentValues(ContentValues values) {
        final Property property = new Property();
        if (values.containsKey("category")) property.setCategory(values.getAsString("category"));
        if (values.containsKey("address")) property.setAddress(values.getAsString("address"));
        if (values.containsKey("price")) property.setPrice(values.getAsFloat("price"));
        if (values.containsKey("surface")) property.setSurface(values.getAsFloat("surface"));
        if (values.containsKey("numberOfRooms")) property.setNbRooms(values.getAsInteger("numberOfRooms"));
        if (values.containsKey("numberOfBathrooms")) property.setNbBathrooms(values.getAsInteger("numberOfBathrooms"));
        if (values.containsKey("numberOfBedrooms")) property.setNbBedrooms(values.getAsInteger("numberOfBedrooms"));
        if (values.containsKey("description")) property.setDescription(values.getAsString("description"));
        if (values.containsKey("status")) property.setStatus(values.getAsString("status"));
        if (values.containsKey("agentName")) property.setStatus(values.getAsString("agentName"));
        if (values.containsKey("school")) property.setSchool(values.getAsBoolean("school"));
        if (values.containsKey("business")) property.setBusiness(values.getAsBoolean("business"));
        if (values.containsKey("park")) property.setPark(values.getAsBoolean("park"));
        if (values.containsKey("publicTransport")) property.setPublicTransport(values.getAsBoolean("publicTransport"));
        if (values.containsKey("dateOfEntry")) property.setDateOfEntry(values.getAsString("dateOfEntry"));
        if (values.containsKey("dateSold")) property.setDateSold(values.getAsString("dateSold"));
        if (values.containsKey("id")) property.setId(values.getAsLong("id"));

        return property;
    }
}
