package com.nomadhub.nomadhub;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

public class Event {

    private String title;
    private String address;
    private Bitmap image;
    private LatLng latLng;
    private String date;

    Event(String title, String address, Bitmap bitmap, LatLng latLng, String date) {
        this.title = title;
        this.address = address;
        this.image = bitmap;
        this.latLng = latLng;
        this.date = date;
    }


    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public Bitmap getImage() {
        return image;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getDate() {
        return date;
    }
}
