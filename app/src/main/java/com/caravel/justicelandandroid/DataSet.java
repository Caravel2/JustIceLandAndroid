package com.caravel.justicelandandroid;

import com.google.android.gms.maps.model.LatLng;


public class DataSet {

    private String title;
    private LatLng latLng;
    private int picPos;
    private float rating;


    public DataSet(String title, LatLng latLng, int picPos, float rating ){

        this.title  = title;
        this.latLng = latLng;
        this.picPos = picPos;
        this.rating = rating;

    }


    public String getTitle(){

        return this.title;

    }


    public LatLng getPos(){

        return this.latLng;

    }

    public int getPicPos(){

        return this.picPos;

    }

    public float getRating(){

        return this.rating;

    }

}
