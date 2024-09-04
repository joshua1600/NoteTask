package my.edu.utar.notetask;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private TextInputEditText usernameEditText, emailEditText, passwordEditText;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        Button registerButton = findViewById(R.id.register_button);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    usernameEditText.setError("Username is required");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    emailEditText.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordEditText.setError("Password is required");
                    return;
                }

                if (password.length() < 12) {
                    passwordEditText.setError("Password must be at least 12 characters");
                    return;
                }

                // Register the user with Firebase Authentication
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register.this, task -> {
                            if (task.isSuccessful()) {
                                // Get the registered user's FirebaseUser object
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                if (firebaseUser != null) {
                                    // Save additional user information to Firebase Realtime Database
                                    String userId = firebaseUser.getUid();
                                    saveUserInformation(userId, username, email);

                                    Toast.makeText(Register.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Register.this, LoginActivity.class));
                                }
                            } else {
                                Toast.makeText(Register.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void saveUserInformation(String userId, String username, String email) {
        // Create a map to store user data
        Map<String, String> userMap = new HashMap<>();
        userMap.put("username", username);
        userMap.put("email", email);

        // Store user data in the database under the user's unique ID
        databaseReference.child(userId).setValue(userMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Register.this, "User data saved successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Register.this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

