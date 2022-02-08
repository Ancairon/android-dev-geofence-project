package com.example.androiddev;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "GeofenceBroadcastR";

    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Toast.makeText(context, "Geofence triggered...", Toast.LENGTH_LONG).show();

        //make a db object
        CoordDatabase db = Room.databaseBuilder(context,
                CoordDatabase.class, "Coords").build();

        CoordDao coordDao = db.coordDao();
        Coord coord = new Coord();

        Location location;

        //Get the Geofencing event from the Intent
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: Error receiving geofence event...");
            return;
        }

        //This should Iterate just once, but just in case I use it for debugging reasons
        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        for (Geofence geofence : geofenceList) {
            Log.d(TAG, "onReceive: " + geofence.getRequestId());
        }

        int transitionType = geofencingEvent.getGeofenceTransition();

        location = geofencingEvent.getTriggeringLocation();
        coord.setLat(location.getLatitude());
        coord.setLon(location.getLongitude());
        coord.setTimestamp(location.getTime());
        coord.setAction(geofencingEvent.getGeofenceTransition());
        Thread t;

        //Determine the transition type, and insert the event in the database.
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT).show();

                t = new Thread(() -> {
                    coordDao.insertAll(coord);
                    Log.d("DB", "" + coord.getAction());
                    db.close();
                });
                t.start();

                break;

            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Toast.makeText(context, "GEOFENCE_TRANSITION_EXIT", Toast.LENGTH_SHORT).show();

                t = new Thread(() -> {
                    coordDao.insertAll(coord);
                    Log.d("DB", "" + coord.getAction());
                    db.close();
                });
                t.start();

                break;
        }

    }
}