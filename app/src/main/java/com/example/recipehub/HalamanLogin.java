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
    /// Deklarasi variable
    // UserRepository untuk mengakses database ke table User
    UserRepository userRepository;

    // EditText untuk input username dan password
    EditText username, password;

    // Button untuk login dan TextView untuk register
    Button loginButton;
    TextView registerTextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Membuat aplikasi berjalan dalam dark mode
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);

        // Template
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_halaman_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Cek apakah user sudah login
        if (AppPreferences.isUserLoggedIn(this)) {
            Toast.makeText(this, "Welcome back, " + AppPreferences.getUserName(this), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Inisialisasi variable
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        userRepository = new UserRepository(dbHelper.getWritableDatabase());
        loginButton = findViewById(R.id.loginButton);
        registerTextButton = findViewById(R.id.registerTextButton);
        username = findViewById(R.id.usernameText);
        password = findViewById(R.id.passwordText);

        // Menambahkan fungsionalitas pada button Login
        loginButton.setOnClickListener(v -> {
            // Mengambil nilai dari EditText username dan password
            String usernameText = username.getText().toString();
            String passwordText = password.getText().toString();

            // Cek apakah ada error yang tidak terduga
            try {
                User user = userRepository.getUserByUsername(usernameText);

                // Cek apakah terdapat user dengan username dan password yang sesuai
                if (user.getPassword().equals(passwordText)) {
                    // Simpan data user yang login ke aplikasi agar aplikasi tau bahwa user telah login
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

        // Menambahkan fungsionalitas pada text Register
        registerTextButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        });
    }
}