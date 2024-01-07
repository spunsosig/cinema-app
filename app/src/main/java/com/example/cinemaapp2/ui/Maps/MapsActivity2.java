package com.example.cinemaapp2.ui.Maps;

import static com.example.cinemaapp2.BuildConfig.MAPS_API_KEY;
import static com.google.android.gms.location.Priority.PRIORITY_BALANCED_POWER_ACCURACY;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.cinemaapp2.MainActivity;
import com.example.cinemaapp2.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.cinemaapp2.databinding.ActivityMaps2Binding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class MapsActivity2 extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMaps2Binding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private PlacesClient placesClient;
    LatLng selectedLatLng;

    AutocompleteSupportFragment autocompleteFragment;

    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMaps2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        setContentView(R.layout.activity_maps2);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Log.i("MAP, "Location permissions have not been granted");
            ActivityResultLauncher<String[]> locationPermissionRequest =
                    registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.getOrDefault(
                                        android.Manifest.permission.ACCESS_FINE_LOCATION, false);
                        Boolean coarseLocationGranted = result.getOrDefault(
                                        android.Manifest.permission.ACCESS_COARSE_LOCATION, false);
                        if (fineLocationGranted != null && fineLocationGranted) {
                                    Log.i("MAP", "Precise location access granted.");
                                    getLastLocation();
                                } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                    Log.i("MAP", "Only approximate location access granted.");
                                    getLastLocation();
                                } else {
                                    Log.i("MAP", "No location access granted.");
                                }
                            }
                    );

            // Before you perform the actual permission request, check whether your app
            // already has the permissions, and whether your app needs to show a permission
            // rationale dialog. For more details, see Request permissions.
            locationPermissionRequest.launch(new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            });
        } else {
            Log.i("MAP", "Location permissions already granted.");
            getLastLocation();
        }

        Places.initialize(this, MAPS_API_KEY);
        placesClient = Places.createClient(this);

        init();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.baseline_arrow_back_24));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void init() {
        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i("Places", "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng());

                // Add a marker to the selected place.
                addMarker(place.getName(), place.getLatLng());
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i("Places", "An error occurred: " + status);

            }
        });
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
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.clear();

    }

    public void getLastLocation() {
//         fusedLocationClient.getCurrentLocation(PRIORITY_BALANCED_POWER_ACCURACY, null);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {

                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.i("MAP", "We got a location: (" + location.getLatitude() +
                                    ", " + location.getLongitude() + ")");
                            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(myLocation).title("You are here"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                            searchForCinemas(myLocation);
                        } else {
                            Log.i("MAP", "We failed to get a last location");
                        }
                    }
                });
    }

    private void addMarker(String title, LatLng latLng) {
        // Clear existing markers on the map.
        mMap.clear();

        // Add a new marker to the selected place.
        mMap.addMarker(new MarkerOptions().position(latLng).title(title));

        // Move the camera to the selected place.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
    }

//    private void searchForCinemas(LatLng location){
//        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG));
//
//        placesClient.findCurrentPlace(location)
//                .addOnSuccessListener(response -> {
//                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()){
//                        Place place = placeLikelihood.getPlace();
//
//                        mMap.addMarker(new MarkerOptions()
//                                .position(place.getLatLng())
//                                .title(place.getName()));
//                    }
//                })
//                .addOnFailureListener((e -> {
//                    Log.e("Places", "Error getting places", e);
//                }));
//    }

    private void searchForCinemas(LatLng location) {
        // Use Places API to search for places near the given location
        // Set up a Places API request and handle the response

        // For example, searching for places within a radius of 5000 meters
        // You may need to perform this operation in a background thread or use async methods

        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.TYPES));

        placesClient.findCurrentPlace(request)
                .addOnSuccessListener(response -> {
                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        Place place = placeLikelihood.getPlace();

                        // Check if the place has the type "movie_theater"
                        List<Place.Type> types = place.getTypes();
                        if (types != null && types.contains(Place.Field.TYPES.name() == "movie_theater")) {
                            // Add a marker for each cinema
                            mMap.addMarker(new MarkerOptions()
                                    .position(place.getLatLng())
                                    .title(place.getName()));
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Log.e("Places", "Error getting places", e);
                });
    }
}
