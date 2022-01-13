package com.example.androiddev;

import android.content.ContentValues;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Timestamp;

@Entity
public class Coord {

    //public static final String TABLE_NAME = "coords";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = "id")
    public int id;

    @ColumnInfo(name = "lat")
    public double lat;

    @ColumnInfo(name = "lon")
    public double lon;

    @ColumnInfo(name = "action")
    public int action;

    @ColumnInfo(name = "timestamp")
    public long timestamp;


    public static Coord fromContentValues(ContentValues contentValues) {
        final Coord coord = new Coord();
        if (contentValues.containsKey("id")) {
            coord.id = contentValues.getAsInteger("id");
        }
        if (contentValues.containsKey("lat")) {
            coord.lat = contentValues.getAsDouble("lat");
        }
        if (contentValues.containsKey("lon")) {
            coord.lon = contentValues.getAsDouble("lon");
        }
        if (contentValues.containsKey("action")) {
            coord.action = contentValues.getAsInteger("action");
        }
        if (contentValues.containsKey("timestamp")) {
            coord.timestamp = contentValues.getAsLong("timestamp");
        }
        return coord;
    }


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
