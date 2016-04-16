package com.caravel.justicelandandroid;

import com.google.android.gms.maps.model.LatLng;


public class DataSet {

    private String title;
    private LatLng latLng;


    public DataSet(String title, LatLng latLng){

        this.title  = title;
        this.latLng = latLng;

    }


    public String getTitle(){

        return this.title;

    }


    public LatLng getPos(){

        return this.latLng;

    }

}
