package dk.au.mad21spring.AppProject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import dk.au.mad21spring.AppProject.R;
import dk.au.mad21spring.AppProject.model.Quiz;
import dk.au.mad21spring.AppProject.service.LocationService;
import dk.au.mad21spring.AppProject.viewmodel.MapViewModel;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Button QuizButton;
    private GoogleMap mMap;
    private List<Quiz> quizzes = new ArrayList();
    private Boolean mapReady = false;

    MapViewModel mapViewModel;

    Circle myPositionCircle;
    Location currentLocation;

    public static final int PERMISSIONS_REQUEST_LOCATION = 101;
    private static final String TAG = "MapsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWigdets();

        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        mapViewModel.getQuizzes().observe(this, q -> {
            quizzes = q;
            initMap();
        });

        mapViewModel.getCurrentLocation().observe(this, location -> {
            if (mapReady){
                showPlacementOnMap(location);
            }
            currentLocation = location;
        });
    }

    private void initWigdets() {
        QuizButton = findViewById(R.id.MainQuizButton);

        //setting up OnClickListeners
        QuizButton.setOnClickListener(v -> {
            if (currentLocation != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 18));
                ConquerQuiz();
            }
        });
    }

    ////Map
    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        boolean success = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.maps_style));

        if (!success) {
            Log.e(TAG, "Style parsing failed.");
        }

        mapReady = true;

        checkLocationPermission();

        AddMapMarkers();
        QuizButton.setEnabled(true);
        mMap.setOnMarkerClickListener(marker -> {
            Toast.makeText(MainActivity.this, marker.getSnippet(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
            intent.putExtra("quizId", marker.getSnippet());

            try {
                MainActivity.this.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.catch_error) + e, Toast.LENGTH_SHORT).show();
            }



            return false;
        });
    }

    private void AddMapMarkers() {
        for (int i = 0; i < quizzes.size(); i++) {
            Log.d(TAG, quizzes.get(i).getDocumentId());
            mMap.addMarker(new MarkerOptions()
                    .snippet(quizzes.get(i).getDocumentId()) //TODO: check if there are better ways to store quizId in marker
                    .position(new LatLng(quizzes.get(i).getLatitude(), quizzes.get(i).getLongitude()))
                    .icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_grade_24)));
        }
    }


    private void addQuizes() {
        Quiz q1 = new Quiz();
        q1.setLatitude(56.1692);
        q1.setLongitude(10.1998);
        q1.setCategory("24");
        q1.setDifficulity("medium");

        Quiz q2 = new Quiz();
        q2.setLatitude(56.1700);
        q2.setLongitude(10.1992);
        q2.setCategory("26");
        q2.setDifficulity("medium");

        Quiz q3 = new Quiz();
        q1.setLatitude(56.1695);
        q1.setLongitude(10.2000);
        q1.setCategory("20");
        q1.setDifficulity("hard");

        Quiz q4 = new Quiz();
        q2.setLatitude(56.1692);
        q2.setLongitude(10.1988);
        q2.setCategory("19");
        q2.setDifficulity("easy");

        mapViewModel.addQuiz(q1);
        mapViewModel.addQuiz(q2);
        mapViewModel.addQuiz(q3);
        mapViewModel.addQuiz(q4);
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

    ////Location tracking
    private void showPlacementOnMap(Location location) {
        if (myPositionCircle == null) {
            //Add circle to mark current position
            myPositionCircle = mMap.addCircle(new CircleOptions()
                    .center(new LatLng(location.getLatitude(), location.getLongitude()))
                    .radius(5)
                    .strokeColor(Color.WHITE)
                    .fillColor(Color.MAGENTA));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));  //move camera to location
        } else {
            //Move circle to match current position
            myPositionCircle.setCenter(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }

    ////Quiz
    private void ConquerQuiz() {
        //Check for nearby quiz
        Quiz quiz = FindNearbyQuiz();

        if (quiz == null) {
            Toast.makeText(this, getResources().getString(R.string.no_quizzes_near), Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            intent.putExtra("quizId", quiz.getDocumentId());

            GoToQuizActivity(intent);
        }
    }

    private Quiz FindNearbyQuiz() {
        double distance;

        for (int i = 0; i < quizzes.size(); i++) {
            //Distance from current location
            distance = calculateDistance(currentLocation.getLatitude(), currentLocation.getLongitude(), quizzes.get(i).getLatitude(), quizzes.get(i).getLongitude());

            Toast.makeText(this, getResources().getString(R.string.distance_m) + distance + "m", Toast.LENGTH_SHORT).show();

            //If ANY Quiz i close enough, return quiz
            if (distance < 10) {
                return quizzes.get(i);
            }
        }

        //If no quizzes are near return null
        return null;
    }

    //https://www.geodatasource.com/developers/java
    ////Gets two coordinates calculates the distance between i meters
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344 * 1000; //Distance in meters

        return (dist);
    }

    private void GoToQuizActivity(Intent intent) {
        try {
            MainActivity.this.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, getResources().getString(R.string.catch_error) + e, Toast.LENGTH_SHORT).show();
        }
    }

    ////Check Location permission
    //https://developer.android.com/training/permissions/requesting.html
    private void checkLocationPermission() {
        //Check if permission has been granted:
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Request permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
        } else {
            //startLocationTracking();
            startService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // got permission
                //startLocationTracking();
                startService();
            } else {
                // permission denied
                finish();
            }
        }
    }

    private void startService() {
        Gson gson = new Gson();

        String serializedQuizzes = gson.toJson(quizzes);
        Intent weatherServiceIntent = new Intent(this, LocationService.class);
        weatherServiceIntent.putExtra("quizzes", serializedQuizzes);

        startService(weatherServiceIntent);
    }
}

