package com.example.recipehub;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DATABASE_NAME = "recipe_hub.db";

    public DatabaseHelper(@Nullable Context context ) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE_QUERY = "CREATE TABLE users ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE, "+
                "email TEXT UNIQUE, "+
                "password TEXT )";

        String CREATE_RECIPE_TABLE_QUERY = "CREATE TABLE recipes ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, "+
                "title TEXT, "+
                "description TEXT, "+
                "ingredients TEXT, "+
                "instructions TEXT, "+
                "prep_time INTEGER,"+
                "image BLOB, "+
                "FOREIGN KEY(user_id) REFERENCES users(id) )";

        String SEED_USER_TABLE_QUERY = "INSERT INTO users (username, email, password) VALUES ( " +
                "'dummy', " +
                "'dummy@gmail.com', " +
                "'dummy' )";

        db.execSQL(CREATE_USER_TABLE_QUERY);
        db.execSQL(CREATE_RECIPE_TABLE_QUERY);
        db.execSQL(SEED_USER_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        onCreate(db);
    }

    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }
}
