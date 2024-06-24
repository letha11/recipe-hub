package com.example.recipehub;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recipehub.helper.AppPreferences;
import com.example.recipehub.models.User;
import com.example.recipehub.repository.UserRepository;

public class HalamanLogin extends AppCompatActivity {
    private UserRepository userRepository;

    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_halaman_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (AppPreferences.isUserLoggedIn(this)) {
            Toast.makeText(this, "Welcome back, " + AppPreferences.getUserName(this), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        userRepository = new UserRepository(dbHelper.getWritableDatabase());
        Button loginButton = findViewById(R.id.loginButton);
        TextView registerTextButton = findViewById(R.id.registerTextButton);
        username = findViewById(R.id.usernameText);
        password = findViewById(R.id.passwordText);

        loginButton.setOnClickListener(v -> {
            String usernameText = username.getText().toString();
            String passwordText = password.getText().toString();

            try {
                User user = userRepository.getUserByUsername(usernameText);

                if (user.getPassword().equals(passwordText)) {
                    AppPreferences.setId(this, user.getId());
                    AppPreferences.setUserName(this, user.getUsername());

                    Toast.makeText(this, "Login Successful, Welcome " + usernameText, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    this.finish();
                } else {
                    Toast.makeText(this, "Login Failed, Please check your username and password", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Login Failed, Please check your username and password", Toast.LENGTH_SHORT).show();
            }
        });

        registerTextButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        });
    }
}