package com.caravel.justicelandandroid;

import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
                                                        GoogleMap.OnMarkerClickListener,
                                                        GoogleMap.OnMapClickListener{

    /**--------  Variable Declare  --------**/
    private static final String TAG = MapsActivity.class.getSimpleName();
    private static final LatLng GDG_POS = new LatLng(22.604755,120.300524);

    private GoogleMap mMap;


    private LinkedList<DataSet> mItemList;
    private List<LatLng> listPoint;



    /**--------  Life Cycle  --------**/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //====  google 送的 ====//
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //------  1. 令好 data 陣列 ------//

        mItemList = new LinkedList<DataSet>();
        listPoint = new ArrayList<LatLng>();

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



        mItemList.add(marker1);
        mItemList.add(marker2);
        mItemList.add(marker3);
        mItemList.add(marker4);
        mItemList.add(marker5);
        mItemList.add(marker6);


        Log.d(TAG, "mItemList" + mItemList);

    }


    /**-------- 2. Custom Class --------**/


    /**-------- 3. Override Method --------**/

    @Override
    public void onMapReady(GoogleMap googleMap) {


        //------  1. Map 初始化 ------//

        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GDG_POS, 14));

        mMap.addMarker(new MarkerOptions()
                .position(GDG_POS)
                .title("GDG高雄"));


        //------  2. Listener  ------//

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);

        //------  2. 插marker  ------//

        for(int i=0; i<mItemList.size(); i++) {
            mMap.addMarker(new MarkerOptions()
                            .position(mItemList.get(i).getPos())
                            .title(mItemList.get(i).getTitle())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_star_marker))
                            .snippet(String.valueOf(i))
            );

            listPoint.add(mItemList.get(i).getPos());

        }



        //====  google 送的 ====//
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));








    }


    @Override
    public boolean onMarkerClick(final Marker marker) {

        Log.d(TAG,"marker clicked.");



        //-----  驗證getPosition()誤差  -----//

        Log.d(TAG,"getPos1" + marker.getPosition());
        Log.d(TAG,"getPos2" + marker.getPosition());
        Log.d(TAG,"getPos3" + marker.getPosition());
        Log.d(TAG,"getPos4" + marker.getPosition());


        int i = Integer.parseInt(marker.getSnippet());
        final LatLng markerPos = listPoint.get(i);



        //-----  1. 轉移 camera  -----//

        Location startingLocation = new Location("starting point");
        startingLocation.setLatitude(mMap.getCameraPosition().target.latitude);
        startingLocation.setLongitude(mMap.getCameraPosition().target.longitude);

        Location endingLocation = new Location("ending point");
        endingLocation.setLatitude(markerPos.latitude);
        endingLocation.setLongitude(markerPos.longitude);

        float targetBearing = startingLocation.bearingTo(endingLocation);



        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(marker.getPosition())
                        .tilt(67.5f)
                        .bearing(targetBearing)
                        .zoom(17)
                        .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        //-----  2. marker 跳動特效  -----//
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(markerPos);
        startPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 1500;
        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * markerPos.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * markerPos.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });





        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {



    }
}
