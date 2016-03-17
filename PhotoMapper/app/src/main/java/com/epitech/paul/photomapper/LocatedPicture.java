package com.epitech.paul.photomapper;

import android.media.Image;

import java.util.Date;

/**
 * Created by Paul on 23/02/2016.
 */
public class LocatedPicture
{

    private String address; /*< if provided by user will be used to translate to long/lat coords */
    private String title;
    private double longitude;
    private double latitude;
    private String picturePath;
    private Date   date;

    /*
        If @param address is not null it is used to compute longitude and latitude.
        Otherwise @param longitude and @param latitude are used.
     */
    public LocatedPicture(String picturePath, String title, String address, double lon, double lat, long date)
    {
        this.picturePath = picturePath;
        this.title = title;
        this.address = address;
        longitude = lon;
        latitude = lat;
        this.date = new Date(date);

        if (address != null) {
            TranslateAddressToCoords();
        }
    }

    public LocatedPicture(String picturePath, String title, String address, double lon, double lat) {
        this(picturePath, title, address, lon, lat, (new Date()).getTime());
    }

    private void TranslateAddressToCoords()
    {
        double lon = 0.0;
        double lat = 0.0;

        // todo : retrive coords from address via gmaps

        longitude = lon;
        latitude = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
