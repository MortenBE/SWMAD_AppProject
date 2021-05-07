package dk.au.mad21spring.AppProject.activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.Transliterator;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.location.Location;


import java.util.ArrayList;
import java.util.List;

import dk.au.mad21spring.AppProject.R;
import dk.au.mad21spring.AppProject.model.Quiz;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    //Widgets
    private Button QuizButton, ScoreButton;
    private GoogleMap mMap;

    //For location tracking
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Location lastLocation = null;
    Circle myPosistionCircle;
    Location currentLocation;


    //Variables
    private boolean permissionGranted;
    public static final int PERMISSIONS_REQUEST_LOCATION = 100;
    public static final int REQUEST_CHECK_SETTINGS = 501;
    private static final String TAG = "MapsActivity";

    List<Quiz> quizzes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWigdets();
        checkLocationPermission();
        //initLocationFramework();
        initMap();

        initLocationTracking();
        startLocationTracking();
    }

    private void initLocationTracking() {
        //Make a location request
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    if(myPosistionCircle == null)
                    {
                        //Add circle to mark current position
                        myPosistionCircle = mMap.addCircle(new CircleOptions()
                                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                                .radius(5)
                                .strokeColor(Color.WHITE)
                                .fillColor(Color.MAGENTA));
                    }
                    else {
                        //Move circle to match current position
                        myPosistionCircle.setCenter(new LatLng(location.getLatitude(), location.getLongitude()));
                    }

                    //Update current location and move camera postion and zoom
                    currentLocation = location;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));  //move camera to location
                }
            }
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                Log.d(TAG, "onLocationAvailability: " + locationAvailability.isLocationAvailable());
            }
        };
    }

    @SuppressLint("MissingPermission")
    private void startLocationTracking() {
        //https://developer.android.com/training/location/change-location-settings
        //Setting up location request
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(2 * 1000); //The interval should be low for best user experience
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //Add location settings to location settings
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        //Check whether the current location settings are satisfied
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        //Determine whether the location settings are appropriate for the location request
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });


        //Make a location request
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }


    private void getLocation() {
        Toast.makeText(MainActivity.this, "Location: " + currentLocation.getLatitude() + ", " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();



        /*
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Toast.makeText(MainActivity.this, "Location latitude: " + location.getLatitude() + "Location longitude: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

         */


    }

    private void conquerQuiz(){
        //Check for nearby quiz
        Quiz quiz = checkForNearbyQuiz();

        if(quiz == null)
        {
            Toast.makeText(this, "No quizzes near", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Take relevant information from quiz object
            //Move to quiz activity
            GoToQuizActivity();

        }
    }

    private Quiz checkForNearbyQuiz(){

        double distance;

        for (int i = 0; i < quizzes.size(); i++)
        {
            //Distance from current location
            distance = (currentLocation.getLatitude() - quizzes.get(i).getLatitude()) + (currentLocation.getLongitude() - quizzes.get(i).getLongitude());
            Toast.makeText(this, "Distance: " + distance, Toast.LENGTH_SHORT).show();

            //If ANY Quiz i close enough, return quiz
            if(distance < 1)
            {
                return quizzes.get(i);
            }
        }

        //If no quizzes are near return null
        return null;
    }


    private void initWigdets() {
        QuizButton = findViewById(R.id.MainQuizButton);
        ScoreButton = findViewById(R.id.MainScoreButton);

        //setting up OnClickListeners
        QuizButton.setOnClickListener(v -> {
            conquerQuiz(); //GoToQuizActivity();
        });
        ScoreButton.setOnClickListener(v -> {
            GoToScoreActivity();
        });
    }

    private void GoToScoreActivity() {
        Toast.makeText(this, "Score", Toast.LENGTH_SHORT).show();
        //getLocation();

        /*
        Intent intent = new Intent(MainActivity.this, ScoreActivity.class);

        try {
            MainActivity.this.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Error" + e, Toast.LENGTH_SHORT).show();
        }

         */
    }

    private void GoToQuizActivity() {
        //Toast.makeText(this, "Quiz", Toast.LENGTH_SHORT).show();
        getLocation();

        Intent intent = new Intent(MainActivity.this, QuizActivity.class);

        try {
            MainActivity.this.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Error" + e, Toast.LENGTH_SHORT).show();
        }

    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //currentLocationMarker = makeMarkerIcon(R.drawable.ic_baseline_grade_24, 100, 80);
    }

    ////Check Location permission
    //https://developer.android.com/training/permissions/requesting.html
    private void checkLocationPermission() {
        //Check if permission has been granted:
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            //Request permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
        }
        else
        {
            Toast.makeText(this, "Permission has been granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // got permission
                    // Continue worklflow
                }
                else {
                    // permission denied
                    finish();
                }
                return;
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        AddMapMarkers();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Toast.makeText(MainActivity.this, "HEY" + marker.getTitle(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, ScoreActivity.class);

                try {
                    MainActivity.this.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "Error" + e, Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });

        /*
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        mMap.setMyLocationEnabled(true);

         */
    }

    private void AddMapMarkers() {
        mockGetQuizzes();
        for (int i = 0; i< quizzes.size(); i ++)
        {
            mMap.addMarker(new MarkerOptions()
                    .title(quizzes.get(i).getMockString())
                    .position(new LatLng(quizzes.get(i).getLatitude(), quizzes.get(i).getLongitude()))
                    .icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_grade_24)));
        }
    }

    //TODO:Replace with a method for getting persisted quiz/location data
    private void mockGetQuizzes()
    {
        Quiz q1 = new Quiz();
        q1.setLatitude(56.1692);
        q1.setLongitude(10.1998);
        q1.setMockString("q1 in Aarhus");

        Quiz q2 = new Quiz();
        q2.setLatitude(56.1700);
        q2.setLongitude(10.1992);
        q2.setMockString("q2 in Aarhus");

        quizzes.add(q1);
        quizzes.add(q2);
    }

    //Taken from https://www.geeksforgeeks.org/how-to-add-custom-marker-to-google-maps-in-android/
    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}

