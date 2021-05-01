package dk.au.mad21spring.AppProject.activity;


import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import dk.au.mad21spring.AppProject.R;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Button QuizButton, ScoreButton;
    private GoogleMap mMap;

    private boolean permissionGranted;
    public static final int PERMISSIONS_REQUEST_LOCATION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initWigdet();
        initButtons();

        checkLocationPermission();
        setCurrentLocation();

    }

    private void setCurrentLocation() {
    }

    //https://developer.android.com/training/permissions/requesting.html
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);
        }
    }

    //mhttps://developer.android.com/training/permissions/requesting.html
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // got permission

                }
                else {
                    // permission denied
                    Toast.makeText(this, "You need to enable permission for Location to use the app", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }

    private void initWigdet() {
        QuizButton = findViewById(R.id.MainQuizButton);
        ScoreButton = findViewById(R.id.MainScoreButton);
    }

    private void initButtons() {
        QuizButton.setOnClickListener(v -> {
            GoToQuizActivity();
        });

        ScoreButton.setOnClickListener(v -> {
            GoToScoreActivity();
        });
    }

    private void GoToScoreActivity() {
        Toast.makeText(this, "Score", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, ScoreActivity.class);

        try {
            MainActivity.this.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Error" + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void GoToQuizActivity() {
        Toast.makeText(this, "Quiz", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, QuizActivity.class);

        try {
            MainActivity.this.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Error" + e, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
}