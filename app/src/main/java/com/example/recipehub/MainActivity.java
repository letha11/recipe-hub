package com.example.recipehub;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipehub.adapters.RecipeListAdapter;
import com.example.recipehub.helper.AppPreferences;
import com.example.recipehub.models.Recipe;
import com.example.recipehub.repository.RecipeRepository;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    RecipeRepository recipeRepository;

    TextView greetText;
    LinearLayout newRecipeBtn;
    TextInputEditText searchInput;

    RecyclerView recipeList;
    RecipeListAdapter recipeListAdapter;
    ArrayList<Recipe> recipes = new ArrayList<Recipe>();

    private void populateRecipeList(String search) {
        Recipe[] recipesFromDb = recipeRepository.searchRecipeByTitle(search);
        recipes.clear();
        recipes.addAll(Arrays.asList(recipesFromDb));

        recipeListAdapter = new RecipeListAdapter(this, recipes);

        recipeList.setAdapter(recipeListAdapter);
    }

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
        recipeList = findViewById(R.id.recipeList);
        populateRecipeList("");

        greetText = findViewById(R.id.greetText);
        greetText.setText("hello, " + AppPreferences.getUserName(this));

        newRecipeBtn = findViewById(R.id.newRecipe);
        newRecipeBtn.setOnClickListener(this);

        searchInput = findViewById(R.id.searchInput);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                populateRecipeList(s.toString());
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        populateRecipeList(String.valueOf(searchInput.getText()));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.newRecipe) {
            Intent intent = new Intent(MainActivity.this, ActivityAddRecipe.class);
            startActivity(intent);
        }
    }
}