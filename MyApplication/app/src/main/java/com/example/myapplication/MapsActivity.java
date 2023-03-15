package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener{

    GoogleMap map;
    SupportMapFragment mapFragment;
    Spinner sp_spinner;
    LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location Location;
    public LatLng myLocation = null;
    public static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    FusedLocationProviderClient client;
    Marker marker;
    double latitude;
    double longitude;
    ArrayList<LatLng> LocationStore = new ArrayList<>();
    double mR = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        addControls();
        addEvents();
        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);

    }

    private void addEvents() {
        sp_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case 1:
                        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    case 2:
                        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case 3:
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void addControls() {
        sp_spinner = findViewById(R.id.spinner);

        ArrayList<String> styleMapList = new ArrayList<>();
        styleMapList.add("Hybrid");
        styleMapList.add("Terrain");
        styleMapList.add("Satellite");
        styleMapList.add("Normal");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, styleMapList);
        sp_spinner.setAdapter(arrayAdapter);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            myLocation = new LatLng(latitude, longitude);
                            map.addMarker(new MarkerOptions()
                                    .position(myLocation)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                    .title("You are here"));

                            map.addCircle(new CircleOptions()
                                    .center(myLocation)
                                    .radius(mR)
                                    .strokeColor(Color.GREEN));

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12));

                            int pos = 0;
                            for (LatLng i:LocationStore){
                                pos = pos + 1;
                                Double s = (Double) cal_distance((Double) myLocation.latitude ,(Double) myLocation.longitude,(Double) i.latitude,(Double) i.longitude);
                                if (s <= mR){
                                    map.addMarker(new MarkerOptions()
                                            .position(i).snippet("Open: 6:00, Close: 22:00")
                                            .title("store" + String.valueOf(pos)));

                                }
                            }

                        }
                    });
                }
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        //bat nut zoom out/ zoom in
        map.getUiSettings().setZoomControlsEnabled(true);

        // tao vi tri cua hang
        LatLng store1 = new LatLng(10.786082017417565, 106.70270859668636);
        LatLng store2 = new LatLng(10.763014773985528, 106.68250385435682);
        LatLng store3 = new LatLng(10.772259470328448, 106.72208861753627);
        LatLng store4 = new LatLng(10.761317247799518, 106.67274742874211);
        LatLng store5 = new LatLng(10.854707419452758, 106.77227827717427);
        LatLng store6 = new LatLng(10.761348224917382, 106.66344855558538);
        LatLng store7 = new LatLng(10.754253923503672, 106.66104624160441);
        LatLng store8 = new LatLng(10.760025265698031, 106.68224968005656);
        LatLng store9 = new LatLng(10.777451074491797, 106.69525164802051);
        LatLng store10 = new LatLng(10.870734474577574, 106.79965370422846);


        LocationStore.add(store1);
        LocationStore.add(store2);
        LocationStore.add(store3);
        LocationStore.add(store4);
        LocationStore.add(store5);
        LocationStore.add(store6);
        LocationStore.add(store7);
        LocationStore.add(store8);
        LocationStore.add(store9);
        LocationStore.add(store10);


    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private double cal_distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist * 1.609344 * 1000);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}