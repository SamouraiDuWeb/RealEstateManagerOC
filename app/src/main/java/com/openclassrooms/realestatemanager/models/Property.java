package com.openclassrooms.realestatemanager.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Property implements Serializable {

    private String id;

    private String type;

    private float price;

    private float surface;

    private int nbPieces;

    private String description;

    private List<PropertyPhotos> photos = null;

    private PropertyAddress address;

    private List<InterestPoints> interestPoints;

    private String status;

    private Date dateEntry;

    private Date dateSold;

    private User agent;

    public Property(String type, float price, String description, PropertyAddress address, List<PropertyPhotos> photos) {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public int getNbPieces() {
        return nbPieces;
    }

    public void setNbPieces(int nbPieces) {
        this.nbPieces = nbPieces;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PropertyPhotos> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PropertyPhotos> photos) {
        this.photos = photos;
    }

    public PropertyAddress getAddress() {
        return address;
    }

    public void setAddress(PropertyAddress address) {
        this.address = address;
    }

    public List<InterestPoints> getInterestPoints() {
        return interestPoints;
    }

    public void setInterestPoints(List<InterestPoints> interestPoints) {
        this.interestPoints = interestPoints;
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
}
