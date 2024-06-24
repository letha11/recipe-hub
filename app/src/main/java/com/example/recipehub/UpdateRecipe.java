package com.example.recipehub;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.recipehub.helper.AppPreferences;
import com.example.recipehub.models.Recipe;
import com.example.recipehub.repository.RecipeRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdateRecipe extends AppCompatActivity implements View.OnClickListener {
    int recipeId;
    Recipe recipe;
    RecipeRepository recipeRepository;

    ImageView imageView;
    byte[] imageBytes;
    Button saveButton;

    EditText title, description, ingredients, instructions, prepTime;

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outStream);
                        imageBytes = outStream.toByteArray();

                        Glide.with(getApplicationContext()).load(bitmap).into(imageView);
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_recipe);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageView = findViewById(R.id.imageView);
        saveButton = findViewById(R.id.saveButton);
        title = findViewById(R.id.title_text);
        description = findViewById(R.id.description_text);
        ingredients = findViewById(R.id.ingredients_text);
        instructions = findViewById(R.id.instructions_text);
        prepTime = findViewById(R.id.prep_time_text);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(this, "No recipeId provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recipeId = extras.getInt("recipeId");

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        recipeRepository = new RecipeRepository(dbHelper.getWritableDatabase());

        // Get the recipe from the database
        recipe = recipeRepository.getRecipeById(recipeId);

        if (recipe == null) {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        imageView.setImageBitmap(recipe.getImage());
        title.setText(recipe.getTitle());
        description.setText(recipe.getDescription());
        ingredients.setText(recipe.getIngredients());
        instructions.setText(recipe.getInstructions());
        prepTime.setText(String.valueOf(recipe.getPrepTime()));

        imageView.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.saveButton) {
            String titleText = title.getText().toString();
            String descriptionText = description.getText().toString();
            String ingredientsText = ingredients.getText().toString();
            String instructionsText = instructions.getText().toString();
            String prepTimeText = prepTime.getText().toString();

            if (imageBytes == null) {
                Bitmap imageBitmap = recipe.getImage();
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, outStream);
                imageBytes = outStream.toByteArray();
            }

            Recipe updatedRecipe = new Recipe(recipeId, AppPreferences.getId(this), titleText, descriptionText, ingredientsText, instructionsText, Integer.parseInt(prepTimeText), imageBytes);

            if (recipeRepository.updateRecipe(recipeId, updatedRecipe) != -1) {
                Toast.makeText(this, "Recipe updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update recipe, please try again", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.imageView) {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        }
    }
}