package com.example.recipehub.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.recipehub.models.User;

public class UserRepository {
    private final SQLiteDatabase db;

    public UserRepository(SQLiteDatabase db) {
        this.db = db;
    }

    public long addUser(User user) {
        return db.insertOrThrow("users", null, user.toContentValues());
    }

    public int updateUser(int id, User user) {
        return db.update("users", user.toContentValues(), "id = ?", new String[]{String.valueOf(id)});
    }

    public int deleteUser(int id) {
        return db.delete("users", "id = ?", new String[]{String.valueOf(id)});
    }

    public User getUserById(int id) {
        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE id = ?",
                new String[]{String.valueOf(id)}
        );

        if (cursor.moveToFirst()) {
            User user = extractUserFromCursor(cursor);

            return user;
        } else {
            return null;
        }
    }

    public User getUserByUsername(String username) {
        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE username = ?",
                new String[]{String.valueOf(username)}
        );

        if (cursor.moveToFirst()) {
            User user = extractUserFromCursor(cursor);

            return user;
        } else {
            return null;
        }
    }

    private User extractUserFromCursor(Cursor cursor) {
        User user = new User(
                cursor.getInt(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex("username")),
                cursor.getString(cursor.getColumnIndex("email")),
                cursor.getString(cursor.getColumnIndex("password"))
        );

        return user;
    }
}