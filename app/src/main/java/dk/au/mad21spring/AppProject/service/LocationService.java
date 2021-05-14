package dk.au.mad21spring.AppProject.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


import dk.au.mad21spring.AppProject.R;
import dk.au.mad21spring.AppProject.database.Repository;
import dk.au.mad21spring.AppProject.model.Quiz;

public class LocationService extends Service {

    private static final String TAG = "LocationService";
    public static final int NOTIFICATION_ID = 301;
    public static final String SERVICE_CHANNEL = "serviceChannel";
    private List<Quiz> quizzes = new ArrayList<>();
    private Boolean notifyForNearbyQuiz = true;
    private Location lastLocation;

    //For notification: https://stuff.mit.edu/afs/sipb/project/android/docs/training/notify-user/managing.html
    NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;
    Notification notification;

    Repository repository;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    public LocationService() {
        repository = Repository.getInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        lastLocation = new Location("");

        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(SERVICE_CHANNEL, "Foreground Service", NotificationManager.IMPORTANCE_LOW);
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
        notificationBuilder = new NotificationCompat.Builder(this, SERVICE_CHANNEL);

        initialNotification();
        startForeground(NOTIFICATION_ID, notification);

        //Getting serialized quizzes from intent
        String serializedQuizzes = intent.getStringExtra("quizzes");

        //Deserializing quizzes
        Type listOfMyClassObject = new TypeToken<ArrayList<Quiz>>() {}.getType();
        quizzes = new Gson().fromJson(serializedQuizzes, listOfMyClassObject);

        initLocationTracking();
        startLocationTracking();

        return START_STICKY;
    }

    ////Location tracking
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
                    // Update location in repository
                    if(location.getLatitude() != lastLocation.getLatitude() && location.getLongitude() != lastLocation.getLongitude())
                    {
                        repository.setCurrentLocation(location);
                    }
                    lastLocation = location;


                    Boolean quizNearby = CheckForNearbyQuiz(location);

                    if(quizNearby == true)
                    {
                        if(notifyForNearbyQuiz == true) {
                            //Quiz nearby make notification
                            notification = notificationBuilder
                                    .setContentTitle(getResources().getString(R.string.notificationQuizNear_Title))
                                    .setContentText(getResources().getString(R.string.notificationQuizNear_Text))
                                    .setSmallIcon(R.mipmap.ic_launcher_quizapp)
                                    .build();

                        } else {
                            //User has already been notified do nothing

                        }
                        notifyForNearbyQuiz = false;
                    }
                    else
                    {
                        //No quizzes nearby
                        notification = notificationBuilder
                                .setContentTitle(getResources().getString(R.string.notificationNoQuiz_Title))
                                .setContentText(getResources().getString(R.string.notificationNoQuiz_Text))
                                .setSmallIcon(R.mipmap.ic_launcher_quizapp)
                                .build();

                        notifyForNearbyQuiz = true;

                    }
                    notificationManager.notify(NOTIFICATION_ID, notification);

                }
            }
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                Log.d(TAG, "onLocationAvailability: " + locationAvailability.isLocationAvailable());
            }
        };


    }
    private void initialNotification()
    {
        notification = notificationBuilder
                .setContentTitle("")
                .setContentText("")
                .setSmallIcon(R.mipmap.ic_launcher_quizapp)
                .build();
        notificationManager.notify(NOTIFICATION_ID, notification);
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

        //Make a location request
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Boolean CheckForNearbyQuiz(Location currentLocation) {
        double distance;

        for (int i = 0; i < quizzes.size(); i++) {
            //Distance from current location
            distance = calculateDistance(currentLocation.getLatitude(), currentLocation.getLongitude(), quizzes.get(i).getLatitude(), quizzes.get(i).getLongitude());

            //If ANY Quiz i close enough
            if (distance < 10) {
                return true;
            }
        }

        //If no quizzes are near
        return false;
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
}