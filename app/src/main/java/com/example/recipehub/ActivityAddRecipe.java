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

public class ActivityAddRecipe extends AppCompatActivity implements View.OnClickListener {
    RecipeRepository recipeRepository;

    ImageView imageView;
    Button uploadImageButton, shareButton;

    EditText title, description, ingredients, instructions, prepTime;

    byte[] imageBytes;

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    try {
                        imageView.setVisibility(View.VISIBLE);
                        uploadImageButton.setVisibility(View.GONE);

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
        setContentView(R.layout.activity_add_recipe);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        recipeRepository = new RecipeRepository(dbHelper.getWritableDatabase());

        imageView = findViewById(R.id.imageView);
        uploadImageButton = findViewById(R.id.uploadImage);
        title = findViewById(R.id.title_text);
        description = findViewById(R.id.description_text);
        ingredients = findViewById(R.id.ingredients_text);
        instructions = findViewById(R.id.instructions_text);
        prepTime = findViewById(R.id.prep_time_text);
        shareButton = findViewById(R.id.saveButton);

        imageView.setVisibility(View.GONE);

        uploadImageButton.setOnClickListener(this);
        imageView.setOnClickListener(this);
        shareButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.uploadImage || view.getId() == R.id.imageView) {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        } else if (view.getId() == R.id.saveButton) {
            int loggedInUserId = AppPreferences.getId(this);

            String titleText = title.getText().toString();
            String descriptionText = description.getText().toString();
            String ingredientsText = ingredients.getText().toString();
            String instructionsText = instructions.getText().toString();
            int prepTimeText = Integer.parseInt(prepTime.getText().toString());

            Recipe newRecipe = new Recipe(0, loggedInUserId, titleText, descriptionText, ingredientsText, instructionsText, prepTimeText, imageBytes);

            if (recipeRepository.addRecipe(newRecipe) != -1) {
                Toast.makeText(this, "Thanks for sharing the recipe!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to share recipe, please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}