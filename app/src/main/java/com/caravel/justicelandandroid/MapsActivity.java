package com.caravel.justicelandandroid;

import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
                                                        GoogleMap.OnMarkerClickListener,
                                                        GoogleMap.OnMapClickListener,
                                                        GoogleMap.OnMapLongClickListener{

    /**--------  Variable Declare  --------**/
    private static final String TAG = MapsActivity.class.getSimpleName();
    private static final LatLng GDG_POS = new LatLng(22.604755,120.300524);

    private GoogleMap mMap;
    private Marker shrimp;
    private Marker mMarker;


    private LinkedList<DataSet> mItemList;
    private List<LatLng> listPoint;


    private Polyline polyline;
    private PolylineOptions polylineOptions;

    private LatLng prevPos = GDG_POS;

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

        DataSet marker1 = new DataSet("三多商圈", new LatLng(22.613835, 120.304676),R.drawable.view_2,3.2f);
        DataSet marker2 = new DataSet("85大樓"  , new LatLng(22.611649, 120.300268),R.drawable.view_1,5f);
        DataSet marker3 = new DataSet("市立總圖", new LatLng(22.610042, 120.301829),R.drawable.view_3,6.8f);
        DataSet marker4 = new DataSet("勞工公園", new LatLng(22.607257, 120.309315),R.drawable.view_1,1.5f);
        DataSet marker5 = new DataSet("星光水岸", new LatLng(22.606789, 120.297462),R.drawable.view_4,5.1f);
        DataSet marker6 = new DataSet("生日公園", new LatLng(22.618366, 120.304268),R.drawable.view_1,2f);



        mItemList.add(marker1);
        mItemList.add(marker2);
        mItemList.add(marker3);
        mItemList.add(marker4);
        mItemList.add(marker5);
        mItemList.add(marker6);


        Log.d(TAG, "mItemList" + mItemList);

    }

//    private void clearPolyline() {
//        if (polyline != null) {
//            polyline.remove();
//        }
//
//        polylineOptions = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
//        polylineOptions.color(Color.RED);
//
//    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });

        prevPos = toPosition;
    }


    /**-------- 2. Custom Class --------**/

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private View infoView;
        private TextView infoNum;
        private TextView infoTitle;
        private ImageView infoPic;
        private RatingBar infoRating;



        public CustomInfoWindowAdapter(){

            infoView = getLayoutInflater().inflate(R.layout.map_info_window, null);


        }

        @Override
        public View getInfoWindow(Marker marker) {


            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {

            infoNum = (TextView) infoView.findViewById(R.id.map_info_num);
            infoTitle = (TextView) infoView.findViewById(R.id.map_info_title);
            infoPic = (ImageView) infoView.findViewById(R.id.map_info_pic);
            infoRating = (RatingBar) infoView.findViewById(R.id.map_info_star);



            if(marker.equals(shrimp) || marker.equals(mMarker)){

                infoTitle.setText("我又出來了YA!");
                infoPic.setVisibility(View.GONE);
                infoRating.setVisibility(View.GONE);

            }

            else{

                int whichOne = Integer.parseInt(marker.getSnippet());
                int picPos = mItemList.get(whichOne).getPicPos();
                float rating = mItemList.get(whichOne).getRating();



                infoTitle.setText(marker.getTitle());
                infoPic.setImageResource(picPos);
                infoRating.setRating(rating);



                infoPic.setVisibility(View.VISIBLE);
                infoRating.setVisibility(View.VISIBLE);


            }






            return infoView;
        }
    }


    /**-------- 3. Override Method --------**/

    @Override
    public void onMapReady(GoogleMap googleMap) {


        //------  1. Map 初始化 ------//

        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GDG_POS, 14));




        //------  2. 各種Listener  ------//

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);

        //------  2. 路徑 初始化  ------//

//        clearPolyline();


        //------  2. 插marker  ------//

        //===  蝦米  ====//
        shrimp = mMap.addMarker(new MarkerOptions()
                            .position(GDG_POS)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.shrimp3))
                            .title("GDG高雄"));





        //===  星星  ====//

        for(int i=0; i<mItemList.size(); i++) {
            mMap.addMarker(new MarkerOptions()
                            .position(mItemList.get(i).getPos())
                            .title(mItemList.get(i).getTitle())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_star_marker))
                            .snippet(String.valueOf(i))
            );

            listPoint.add(mItemList.get(i).getPos());

        }

        //------  3. 實作infoWindow  ------//

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());



    }


    @Override
    public boolean onMarkerClick(final Marker marker) {

        Log.d(TAG,"marker clicked.");

        final LatLng markerPos = marker.getPosition();


        if(marker.equals(shrimp) || marker.equals(mMarker)){}

        else{




            //-----  驗證getPosition()誤差  -----//

            Log.d(TAG, "getPos1 位置 --" + marker.getPosition());
            Log.d(TAG, "getPos2 位置 --" + marker.getPosition());
            Log.d(TAG, "getPos3 位置 --" + marker.getPosition());
            Log.d(TAG, "getPos4 位置 --" + marker.getPosition());



    //        int i = Integer.parseInt(marker.getSnippet());
    //        final LatLng markerPos = listPoint.get(i);











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


            //-----  3. 出現infoWindow  -----//

            marker.showInfoWindow();


            //-----  4. 我進去了  -----//
            animateMarker(shrimp, markerPos,true);
        }

        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {

        Log.d(TAG,"map clicked.");

        CameraPosition clickPos = new CameraPosition.Builder()
                .target(GDG_POS)
                .zoom(14)
                .tilt(0)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(clickPos));

        //-----  4. 我又出來了  -----//


        shrimp.setPosition(GDG_POS);
        shrimp.setVisible(true);
        shrimp.showInfoWindow();

    }

    @Override
    public void onMapLongClick(LatLng latLng) {


        //-----  1. 轉移 camera  -----//

        Location startingLocation = new Location("starting point");
        startingLocation.setLatitude(mMap.getCameraPosition().target.latitude);
        startingLocation.setLongitude(mMap.getCameraPosition().target.longitude);

        Location endingLocation = new Location("ending point");
        endingLocation.setLatitude(latLng.latitude);
        endingLocation.setLongitude(latLng.longitude);

        float targetBearing = startingLocation.bearingTo(endingLocation);



        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(latLng)
                        .tilt(67.5f)
                        .bearing(targetBearing)
                        .zoom(17)
                        .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        //-----  2. 生marker  -----//
        String zoom = "Zoom : " + String.valueOf(mMap.getCameraPosition().zoom);

        if(mMarker == null){

            mMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(zoom));

        }else{

            mMarker.getPosition(latLng);
        }




        mMarker.showInfoWindow();

    }
}
