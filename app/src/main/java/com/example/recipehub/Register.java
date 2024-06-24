package com.example.recipehub;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recipehub.helper.AppPreferences;
import com.example.recipehub.models.User;
import com.example.recipehub.repository.RecipeRepository;
import com.example.recipehub.repository.UserRepository;

public class Register extends AppCompatActivity {
    // Deklarasi Variable
    UserRepository userRepository;
    // EditText untuk input username, email, dan password
    EditText username, email, password;
    // Button untuk signup
    Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Template
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi Variable
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        userRepository = new UserRepository(dbHelper.getWritableDatabase());
        signupButton = findViewById(R.id.signupButton);
        username = findViewById(R.id.usernameText);
        email = findViewById(R.id.emailText);
        password = findViewById(R.id.passwordText);

        // Menambahkan fungsionalitas pada button signup
        signupButton.setOnClickListener(v -> {
            // Mengambil data dari inputan
            String usernameText = username.getText().toString();
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();

            // Cek apakah ada error yang tidak terduga saat signup
            try {
                User user = new User(0, usernameText, emailText, passwordText);

                // Menambahkan user ke dalam database
                if (userRepository.addUser(user) != -1) {
                    User createdUser = userRepository.getUserByUsername(usernameText);
                    // Menyimpan data user ke dalam aplikasi agar aplikasi tau bahwa user telah login
                    AppPreferences.setUserName(this, usernameText);
                    AppPreferences.setId(this, createdUser.getId());

                    Toast.makeText(this, "Register Successful, Welcome " + usernameText, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    this.finish();
                } else {
                    Toast.makeText(this, "Register failed, try again with different username", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Register failed, try again with different username", Toast.LENGTH_SHORT).show();
            }
        });
    }
}