package com.example.recipehub.models;

import android.content.ContentValues;

public class Recipe {
    private int id;
    private int userId;
    private String title;
    private String description;
    private String ingredients;
    private String instructions;
    private int prepTime;
    private byte[] image;
    private User user;

    public Recipe(int id, int userId, String title, String description, String ingredients, String instructions, int prepTime, byte[] image) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.prepTime = prepTime;
        this.image = image;
    }

    public Recipe(int id, int userId, String title, String description, String ingredients, String instructoins, String prepTime, byte[] image, User user) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.image = image;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public byte[] getImage() {
        return image;
    }

    public User getUser() {
        return user;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("ingredients", ingredients);
        values.put("instructions", instructions);
        values.put("prep_time", prepTime);
        values.put("image", image);
        values.put("user_id", userId);
        return values;
    }
}
