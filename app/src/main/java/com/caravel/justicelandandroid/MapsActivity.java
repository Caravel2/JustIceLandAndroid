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


    /**--------  Life Cycle  --------**/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //====  google 送的 ====//
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //=====================//
        mItemList = new LinkedList<DataSet>();
        initDataSet();





    }



    /**-------- 1. Custom Method --------**/

    public void initDataSet() {

        DataSet marker1 = new DataSet("a地點", new LatLng(22.637580, 120.307460));
        DataSet marker2 = new DataSet("b地點", new LatLng(22.620032, 120.312223));
        DataSet marker3 = new DataSet("c地點", new LatLng(22.60585, 120.300808));
        DataSet marker4 = new DataSet("d地點", new LatLng(22.637580, 120.307460));
        DataSet marker5 = new DataSet("e地點", new LatLng(22.625103, 120.294456));
        DataSet marker6 = new DataSet("f地點", new LatLng(22.624865, 120.316429));


        Log.d("status", "初始資料" + mItemList);


        mItemList.add(marker1);
        mItemList.add(marker2);
        mItemList.add(marker3);
        mItemList.add(marker4);
        mItemList.add(marker5);
        mItemList.add(marker6);


        Log.d("status", "初始資料" + mItemList);

    }


    /**-------- 2. Custom Class --------**/



    /**-------- 3. Override Method --------**/

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



}
