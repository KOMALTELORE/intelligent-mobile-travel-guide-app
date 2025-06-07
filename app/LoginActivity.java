package com.example.travelguide2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;

public class LoginActivity extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    Button loginButton;
    TextView signupRedirectText;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);

        auth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(view -> {
            String email = loginEmail.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();

            if (email.isEmpty()) {
                loginEmail.setError("Email cannot be empty");
                loginEmail.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                loginPassword.setError("Password cannot be empty");
                loginPassword.requestFocus();
                return;
            }

            // Sign in with Firebase Authentication using Email and Password
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
                                userRef.get().addOnSuccessListener(dataSnapshot -> {
                                    if (dataSnapshot.exists()) {
                                        String name = dataSnapshot.child("name").getValue(String.class); // Fetch name from DB
                                        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                                        intent.putExtra("USERNAME", name); // Pass name instead of email
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        });

        signupRedirectText.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));
    }
}