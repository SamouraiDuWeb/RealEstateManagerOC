package com.openclassrooms.realestatemanager.models;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class Property implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String category;

    private String type;

    private float price;

    private float surface;

    private int nbRooms;

    private int nbBathrooms;

    private int nbBedrooms;

    private String description;

    @Embedded
    private PropertyPhotos photos;

    @Embedded
    private PropertyAddress address;

    private String status;

    @Embedded
    private Date dateEntry;

    @Embedded
    private Date dateSold;

    @Embedded
    private User agent;

    private boolean school;

    private boolean business;

    private boolean park;

    private boolean publicTransport;

    public Property() {
    }

    public Property(long id, String category, String type, float price, float surface, int nbRooms, int nbBathrooms, int nbBedrooms, String description, PropertyPhotos photos, PropertyAddress address, String status, Date dateEntry, Date dateSold, User agent, boolean school, boolean business, boolean park, boolean publicTransport) {
        this.id = id;
        this.category = category;
        this.type = type;
        this.price = price;
        this.surface = surface;
        this.nbRooms = nbRooms;
        this.nbBathrooms = nbBathrooms;
        this.nbBedrooms = nbBedrooms;
        this.description = description;
        this.photos = photos;
        this.address = address;
        this.status = status;
        this.dateEntry = dateEntry;
        this.dateSold = dateSold;
        this.agent = agent;
        this.school = school;
        this.business = business;
        this.park = park;
        this.publicTransport = publicTransport;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public PropertyPhotos getPhotos() {
        return photos;
    }

    public void setPhotos(PropertyPhotos photos) {
        this.photos = photos;
    }

    public PropertyAddress getAddress() {
        return address;
    }

    public void setAddress(PropertyAddress address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDateEntry() {
        return dateEntry;
    }

    public void setDateEntry(Date dateEntry) {
        this.dateEntry = dateEntry;
    }

    public Date getDateSold() {
        return dateSold;
    }

    public void setDateSold(Date dateSold) {
        this.dateSold = dateSold;
    }

    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
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
}
