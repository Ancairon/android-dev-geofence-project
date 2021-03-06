package com.example.androiddev;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;
import androidx.sqlite.db.SimpleSQLiteQuery;

public class GeofenceContentProvider extends ContentProvider {
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    Context context;

    @Override
    public boolean onCreate() {
        uriMatcher.addURI("com.example.androiddev", "coord", 1);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = uriMatcher.match(uri);
        if (code == 1) {
            context = MapsActivity.getContext();
            CoordDatabase db = Room.databaseBuilder(context, CoordDatabase.class, "Coords").build();

            final Cursor cursor;
            SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * FROM Coord");

            cursor = db.query(query);

            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    //Dummy Implementations
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}