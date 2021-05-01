package dk.au.mad21spring.AppProject.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ShowLocationService extends Service {
    public ShowLocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}