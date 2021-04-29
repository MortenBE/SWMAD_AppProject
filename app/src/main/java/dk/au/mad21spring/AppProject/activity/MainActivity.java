package dk.au.mad21spring.AppProject.activity;




import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initWigdet();
        initButtons();
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
    }
}