package com.example.recipehub;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recipehub.models.Recipe;
import com.example.recipehub.repository.RecipeRepository;

public class RecipeDetail extends AppCompatActivity {
    RecipeRepository recipeRepository;
    int recipeId;

    TextView title, description, ingredients, instructions, prepTime, author;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageView = findViewById(R.id.imageView);
        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        description = findViewById(R.id.description);
        ingredients = findViewById(R.id.ingredients);
        instructions = findViewById(R.id.instructions);
        prepTime = findViewById(R.id.prepTimeText);

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
        Recipe recipe = recipeRepository.getRecipeById(recipeId);

        if (recipe == null) {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        imageView.setImageBitmap(recipe.getImage());
        title.setText(recipe.getTitle());
        author.setText("by " + recipe.getUser().getUsername());
        description.setText(recipe.getDescription());
        prepTime.setText(recipe.getPrepTime() + " minutes");
        ingredients.setText(recipe.getIngredients());
        instructions.setText(recipe.getInstructions());
    }
}