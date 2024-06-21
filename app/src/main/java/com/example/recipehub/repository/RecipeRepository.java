package com.example.recipehub.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.recipehub.models.Recipe;
import com.example.recipehub.models.User;

import java.lang.reflect.Array;

public class RecipeRepository {
    private final SQLiteDatabase db;

    public RecipeRepository(SQLiteDatabase db) {
        this.db = db;
    }

    public long addRecipe(Recipe recipe) {
        return db.insert("recipes", null, recipe.toContentValues());
    }

    public int updateRecipe(int id, Recipe recipe) {
        return db.update("recipes", recipe.toContentValues(), "id = ?", new String[]{String.valueOf(id)});
    }

    public int deleteRecipe(int id) {
        return db.delete("recipes", "id = ?", new String[]{String.valueOf(id)});
    }

    public Recipe getRecipeById(int id) {
        Cursor cursor = db.rawQuery(
                "SELECT recipes.*, users.* FROM recipes " +
                        "INNER JOIN users ON recipes.user_id = users.id " +
                        "WHERE recipes.id = ?",
                new String[]{String.valueOf(id)}
        );

        if (cursor.moveToFirst()) {
            Recipe recipe = extractRecipeFromCursor(cursor);

            return recipe;
        } else {
            return null;
        }
    }

    public Recipe getRecipeByTitle(String title) {
        Cursor cursor = db.rawQuery(
                "SELECT recipes.*, users.* FROM recipes " +
                        "INNER JOIN users ON recipes.user_id = users.id " +
                        "WHERE recipes.title = ?",
                new String[]{String.valueOf(title)}
        );

        if (cursor.moveToFirst()) {
            Recipe recipe = extractRecipeFromCursor(cursor);

            return recipe;
        } else {
            return null;
        }
    }

    public Recipe[] getAllRecipeWithUser() {
        Cursor cursor = db.rawQuery(
                "SELECT recipes.*, users.* FROM recipes " +
                        "INNER JOIN users ON recipes.user_id = users.id",
                null
        );

        Recipe[] recipes = new Recipe[cursor.getCount()];

        int i = 0;
        while (cursor.moveToNext()) {
            Recipe recipe = extractRecipeFromCursor(cursor);

            recipes[i] = recipe;
            i++;
        }

        return recipes;
    }

    private Recipe extractRecipeFromCursor(Cursor cursor) {
        User user = new User(
                cursor.getInt(cursor.getColumnIndex("users.id")),
                cursor.getString(cursor.getColumnIndex("username")),
                cursor.getString(cursor.getColumnIndex("email")),
                cursor.getString(cursor.getColumnIndex("password"))
        );

        Recipe recipe = new Recipe(
                cursor.getInt(cursor.getColumnIndex("id")),
                cursor.getInt(cursor.getColumnIndex("user_id")),
                cursor.getString(cursor.getColumnIndex("title")),
                cursor.getString(cursor.getColumnIndex("description")),
                cursor.getString(cursor.getColumnIndex("ingredients")),
                cursor.getString(cursor.getColumnIndex("instructoins")),
                cursor.getString(cursor.getColumnIndex("prep_time")),
                cursor.getBlob(cursor.getColumnIndex("image")),
                user
        );

        return recipe;
    }

}
