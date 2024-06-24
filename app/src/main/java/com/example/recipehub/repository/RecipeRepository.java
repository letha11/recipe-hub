package com.example.recipehub.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    public Recipe[] searchRecipeByTitle(String title) {
        Cursor cursor = db.rawQuery(
                "SELECT recipes.*, users.* FROM recipes " +
                        "INNER JOIN users ON recipes.user_id = users.id " +
                        "WHERE recipes.title LIKE ?",
                new String[]{"%" + title + "%"}
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
                cursor.getInt(8),
                cursor.getString(9),
                cursor.getString(10),
                cursor.getString(11)
        );

        Recipe recipe = new Recipe(
                cursor.getInt(0),
                cursor.getInt(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getBlob(7),
                user
        );

        return recipe;
    }

}
