package com.example.androiddev;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CoordDao {

    @Query("SELECT * FROM Coord")
    List<Coord> getAll();

    //@Query("SELECT * FROM Coord WHERE uid IN (:userIds)")
    //List<User> loadAllByIds(int[] userIds);

   // @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
     //       "last_name LIKE :last LIMIT 1")
    //User findByName(String first, String last);

    @Insert
    void insertAll(Coord... coord);

    @Delete
    void delete(Coord coord);

}
