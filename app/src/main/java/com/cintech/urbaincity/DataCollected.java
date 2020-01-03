package com.cintech.urbaincity;

import java.io.Serializable;

public class DataCollected implements Serializable {

    private String id;
    private String latitude;
    private String longitude;
    private String description;
    private String date;
    private String arrondissement;
    private String quartier;
    private String imageUrl;
    private String imageName;
    private String type;




    public DataCollected(){}
    public DataCollected(String latitude, String longitude, String description,
                         String date, String arrondissement, String quartier,String imageUrl, String imageName,String type){
        this.setId(id);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setDescription(description);
        this.setDate(date);
        this.setArrondissement(arrondissement);
        this.setQuartier(quartier);
        this.setImageUrl(imageUrl);
        this.setImageName(imageName);
        this.setType(type);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArrondissement() {
        return arrondissement;
    }

    public void setArrondissement(String arrondissement) {
        this.arrondissement = arrondissement;
    }

    public String getQuartier() {
        return quartier;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
