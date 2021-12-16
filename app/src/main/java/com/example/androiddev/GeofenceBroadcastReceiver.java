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

    private static final String TAG = "GeofenceBroadcastReceiv";


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Toast.makeText(context, "Geofence triggered...", Toast.LENGTH_LONG).show();

        //  NotificationHelper notificationHelper = new NotificationHelper(context);


        CoordDatabase db = Room.databaseBuilder(context,
                CoordDatabase.class, "Coords").build();

        CoordDao coordDao = db.coordDao();
        // List<Coord> coords = coordDao.getAll();
        Coord coord = new Coord();

        Location location;


        //    coordDao.insertAll(coord);

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: Error receiving geofence event...");
            return;
        }


        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        for (Geofence geofence : geofenceList) {


            Log.d(TAG, "onReceive: " + geofence.getRequestId());
        }
//        Location location = geofencingEvent.getTriggeringLocation();

        int transitionType = geofencingEvent.getGeofenceTransition();


        location = geofencingEvent.getTriggeringLocation();
        coord.setLat(location.getLatitude());
        coord.setLon(location.getLongitude());
        coord.setTimestamp(location.getTime());
        coord.setAction(geofencingEvent.getGeofenceTransition());
        Thread t;

        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT).show();

                t = new Thread(() -> {

                    coordDao.insertAll(coord);
                    Log.d("DB", "" + coord.getAction());
                });
                t.start();

                break;
//            case Geofence.GEOFENCE_TRANSITION_DWELL:
            //              Toast.makeText(context, "GEOFENCE_TRANSITION_DWELL", Toast.LENGTH_SHORT).show();
            // notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_DWELL", "", MapsActivity.class);
            //            break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Toast.makeText(context, "GEOFENCE_TRANSITION_EXIT", Toast.LENGTH_SHORT).show();
                t = new Thread(() -> {

                    coordDao.insertAll(coord);
                    Log.d("DB", "" + coord.getAction());

                });
                t.start();
                break;
        }

    }
}