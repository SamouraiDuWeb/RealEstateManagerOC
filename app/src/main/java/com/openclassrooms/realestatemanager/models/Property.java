package com.openclassrooms.realestatemanager.models;

import java.util.Date;
import java.util.List;

public class Property {

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


}
