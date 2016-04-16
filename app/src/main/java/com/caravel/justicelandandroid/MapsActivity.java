package com.caravel.justicelandandroid;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    /**--------  Variable Declare  --------**/
    private GoogleMap mMap;
    private LinkedList<DataSet> mItemList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //====  google 送的 ====//
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //=====================//

        initDataSet(mItemList);



    }



    /**-------- 1. Override Method --------**/

    @Override
    public void onMapReady(GoogleMap googleMap) {



        //------  1. Map 初始化 ------//

        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.620227, 120.311306), 14));




        //------  2. 插marker  ------//

        for(int i=0; i<mItemList.size(); i++) {
            mMap.addMarker(new MarkerOptions()
                            .position(mItemList.get(i).getPos())
                            .title(mItemList.get(i).getTitle())
                            .snippet(String.valueOf(i))
            );

        }



        //====  google 送的 ====//
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));








    }

    /**-------- 2. Custom Method --------**/

    public void initDataSet(LinkedList<DataSet> list){

        DataSet marker1 = new DataSet("a地點",new LatLng(22.637580,120.307460));
        DataSet marker2 = new DataSet("a地點",new LatLng(22.637580,120.307460));
        DataSet marker3 = new DataSet("a地點",new LatLng(22.637580,120.307460));
        DataSet marker4 = new DataSet("a地點",new LatLng(22.637580,120.307460));
        DataSet marker5 = new DataSet("a地點",new LatLng(22.637580,120.307460));
        DataSet marker6 = new DataSet("a地點",new LatLng(22.637580,120.307460));



        Log.d("status", "初始資料" + mItemList);


        list.add(marker1);
        list.add(marker2);
        list.add(marker3);
        list.add(marker4);
        list.add(marker5);
        list.add(marker6);


        Log.d("status", "初始資料" + mItemList);

    }





}
