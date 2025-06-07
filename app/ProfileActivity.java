package com.example.travelguide2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get the TextView where the username should be displayed
        TextView tvName = findViewById(R.id.tvName); // Ensure this ID matches your XML

        // Get username from Intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");

        // Set the text with username
        if (username != null && !username.isEmpty()) {
            tvName.setText(username);
        } else {
            tvName.setText("User");
        }


        // Hotel Suggestion Card - Redirect to HotelActivity
        CardView hotelCard = findViewById(R.id.hotel);
        hotelCard.setOnClickListener(v -> {
            Intent hotelIntent = new Intent(ProfileActivity.this, HotelActivity.class);
            startActivity(hotelIntent);
        });

        //Preferances
        findViewById(R.id.preferances).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, PreferencesActivity.class);
                startActivity(intent);
            }
        });

        //Budget
        findViewById(R.id.Budget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, BudgetActivity.class);
                startActivity(intent);
            }
        });

        //history
        findViewById(R.id.history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        //place
        findViewById(R.id.place).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, PlaceSuggestionActivity.class);
                startActivity(intent);
            }
        });

        //edit Profile
        findViewById(R.id.edit_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        //reviews
        findViewById(R.id.reviews).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ReviewActivity.class);
                startActivity(intent);
            }
        });


        // Fix: Reference the correct CardView ID instead of an ImageView
        CardView logoutCard = findViewById(R.id.logout); // Use the correct ID for the CardView

        // Fix: Place the click listener inside onCreate()
        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logoutIntent = new Intent(ProfileActivity.this, LoginActivity.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logoutIntent);
                finish();
            }
        });
    }
}
