package com.example.androiddev;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Coord.class}, version = 1)
public abstract class CoordDatabase extends RoomDatabase {

    public abstract CoordDao coordDao();

}
