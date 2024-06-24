package com.example.recipehub;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recipehub.helper.AppPreferences;
import com.example.recipehub.repository.RecipeRepository;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    RecipeRepository recipeRepository;

    TextView greetText;
    LinearLayout recipe1, newRecipeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        recipeRepository = new RecipeRepository(dbHelper.getWritableDatabase());
        recipe1 = findViewById(R.id.recipe1);
        newRecipeBtn = findViewById(R.id.newRecipe);
        greetText = findViewById(R.id.greetText);

        greetText.setText("hello, " + AppPreferences.getUserName(this));

        recipe1.setOnClickListener(this);
        newRecipeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.recipe1) {
            Intent intent = new Intent(this, RecipeDetail.class);
            intent.putExtra("recipeId", 2);
            startActivity(intent);
        } else if (v.getId() == R.id.newRecipe) {
            Intent intent = new Intent(MainActivity.this, ActivityAddRecipe.class);
            startActivity(intent);
//            Recipe dummyRecipe = new Recipe(0, 1, "Dummy Recipe", "Dummy Description", "Dummy Ingredients", "Dummy Instructions", 30, new byte[0]);
//
//            if (recipeRepository.addRecipe(dummyRecipe) != -1) {
//                // show toast success
//                Toast.makeText(this, "Recipe added successfully", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Failed to add Recipe", Toast.LENGTH_SHORT).show();
//            }
        }
    }
}