package com.example.androiddev;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
//Class responsible to become a table, with each attribute becoming a column
public class Coord {

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


    public void setLat(double lat) {
        this.lat = lat;
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

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
