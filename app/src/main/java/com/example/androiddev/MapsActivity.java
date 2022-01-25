package com.example.androiddev;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.androiddev.databinding.ActivityMapsBinding;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;
    private ActivityMapsBinding binding;
    private final float GEOFENCE_RADIUS = 100;
    private final String GEOFENCE_ID = "SOME_GEOFENCE_ID";

    private final int FINE_LOCATION_ACCESS_REQUEST_CODE = 1;
    private final int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 2;
    private final int INITIAL_REQUEST_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        //Up until 25/1/2022, requesting the permission after the map is initialized,
        //results in the emulator not tracking the location at first launch.
        requestFineLocation();
    }

    private void requestFineLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Ask for permission, this is checked every time the app launches to see if it has the permission to show the blue dot on the map.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, INITIAL_REQUEST_CODE);
            return;
        }
        //If this is not first-launch case, initialize the map right-away
        initMap();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        //Initialize the map
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);

        //Use the zoom controls to make it easier on the emulator
        mMap.getUiSettings().setZoomControlsEnabled(true);

        checkPermissions();


    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        if (Build.VERSION.SDK_INT >= 29) {
            //We need to request background permission
            if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //If the user has already granted the permission, handle his click
                handleMapLongClick(latLng);
            } else {
                //else request that permission along with an informative toast
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                Toast.makeText(MapsActivity.this, "Background location access is necessary for Geofences to trigger...", Toast.LENGTH_LONG).show();
            }
        } else {
            //if the API is lower than 29, we already got background permission with the fine location access, so handle the click
            handleMapLongClick(latLng);
        }
    }


    private void checkPermissions() {
        //if we got the permissions we need, enable the location.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private void initMap() {
        //Layout stuff initialization
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        //Initialize the geofence stuff
        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceHelper = new GeofenceHelper(this);
        //checkPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case FINE_LOCATION_ACCESS_REQUEST_CODE:
                //If I got a fine location permission, guide the user to assign also the background location permission
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //We have the permission
                    Toast.makeText(this, "Long press and select \"Allow all the time\" to start adding geofences :)", Toast.LENGTH_LONG).show();
                }
                break;
            case BACKGROUND_LOCATION_ACCESS_REQUEST_CODE:
                /*
                if I got a background location permission, then the user long clicked,
                so I check the permissions just to make sure and enable the location and I also Toast a message.
                 */
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //We have the permission
                    //checkPermissions();
                    Toast.makeText(this, "You can add geofences...", Toast.LENGTH_SHORT).show();
                }
                break;
            case INITIAL_REQUEST_CODE:
                //When the user grants fine location upon launch, enable the flag for the map to read when it is ready.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //We have the permission so initialize the map
                    initMap();
                }
                break;
        }
    }

    private void handleMapLongClick(LatLng latLng) {
        mMap.clear();
        addMarker(latLng);
        addCircle(latLng, GEOFENCE_RADIUS);
        addGeofence(latLng, GEOFENCE_RADIUS);
    }

    private void addGeofence(LatLng latLng, float radius) {

        Geofence geofence = geofenceHelper.getGeofence(GEOFENCE_ID, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();

        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess: Geofence Added..."))
                .addOnFailureListener(e -> {
                    String errorMessage = geofenceHelper.getErrorString(e);
                    Log.d(TAG, "onFailure: " + errorMessage);
                });
    }

    private void addMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        mMap.addMarker(markerOptions);
    }

    private void addCircle(LatLng latLng, float radius) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
        circleOptions.fillColor(Color.argb(64, 255, 0, 0));
        circleOptions.strokeWidth(4);
        mMap.addCircle(circleOptions);
    }
}


