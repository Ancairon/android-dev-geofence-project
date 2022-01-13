package com.example.androiddev;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Coord.class}, version = 1)
public abstract class CoordDatabase extends RoomDatabase {

    public abstract CoordDao coordDao();


    private static CoordDatabase instance;

    public static synchronized CoordDatabase getInstance(Context context){
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context.getApplicationContext(),CoordDatabase.class,"inst_name")
                    .build();

        }
        return instance;
    }

}
