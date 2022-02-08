package com.example.androiddev;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface CoordDao {
    @Insert
    void insertAll(Coord... coord);
}
