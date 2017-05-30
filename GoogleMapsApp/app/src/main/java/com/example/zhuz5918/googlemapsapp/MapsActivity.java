package com.example.zhuz5918.googlemapsapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
//import com.google.android.gms.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
//Apparently the following line is dangerous????
import android.Manifest;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    private static final long MIN_TIME_BETWEEN_UPDATES= 1000 * 15 * 1;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        /**
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));**/
        LatLng miCasa = new LatLng(32.94, -117.21);
        mMap.addMarker(new MarkerOptions().position(miCasa).title("Born here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(miCasa));

        /**LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(location != null){
                    //makeUseofNewLocation(location);

                };
            }
            public void onStatusChanged(String provider, int status, Bundle extras){}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        LatLng currentLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("You are here"));**/





        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            return;
        };
        mMap.setMyLocationEnabled(true);
        Log.d("My Map", "Current Location");
        getLocation();

        }

    public void switchView(View v){
        if(mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL){
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else{
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

    }

    public void getLocation(){
        try{
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            //Get GPS Status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if(isGPSEnabled){
                Log.d("MyMaps", "getLocation: GPS is enabled");
            }

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(isNetworkEnabled){
                Log.d("MyMaps", "getLocation: NETWORK is enabled");
            }

            if(!isGPSEnabled && !isNetworkEnabled){
                Log.d("MyMaps", "getLocation: No provider is enabled");
            }
            else {
                this.canGetLocation = true;
                if(isNetworkEnabled){
                    Log.d("MyMaps", "getLocation: Network enabled - requesting location updates");
                    LocationListener locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            if(location != null){
                                //makeUseofNewLocation(location);
                            };
                        }
                        public void onStatusChanged(String provider, int status, Bundle extras){}
                        public void onProviderEnabled(String provider) {}
                        public void onProviderDisabled(String provider) {}
                    };
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("MyMaps", "Permissions granted");
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BETWEEN_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            locationListener);

                        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        LatLng currentLocation = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions().position(currentLocation).title("You are here"));


                    Log.d("MyMaps", "getLocation: Apparently getting location via network updates works. JK");
                    Toast.makeText(this, "Using Network", Toast.LENGTH_SHORT);}

                }
                if(isGPSEnabled){
                    Log.d("MyMaps", "getLocation: Network enabled - reuqesting location updates");
                    /**locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BETWEEN_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            locationListenerGPS);
                    Log.d("MyMaps", "getLocation: Apparently getting location via GPS updates works. JK");
                    Toast.makeText(this, "Using Network", Toast.LENGTH_SHORT);**/
                }
            }
        }catch(Exception e){
            Log.d("MyMaps", "Exception in getLocation");
            e.printStackTrace();
        }
    }

    }

    /**
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        };
        mMap.setMyLocationEnabled(true);**/


        /**LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);**/
        /**LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(location != null){
                    makeUseofNewLocation(location);
                };
            }
            public void onStatusChanged(String provider, int status, Bundle extras){}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,(long)0,(float)0,locationListener);**/
        /**Location loc = googleMap.getMyLocation();
        LatLng currentCoords = new LatLng(loc.getLatitude(), loc.getLongitude());
        Log.d("My Map", "current location retrieved");
        mMap.addMarker(new MarkerOptions().position(currentCoords).title("You are here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentCoords));**/


