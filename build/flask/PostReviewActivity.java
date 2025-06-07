package com.example.travelguide2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostReviewActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText commentEditText;
    private Button submitButton;
    private DatabaseReference mDatabase;
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_review);

        // Initialize views
        ratingBar = findViewById(R.id.ratingBar);
        commentEditText = findViewById(R.id.commentEditText);
        submitButton = findViewById(R.id.submitButton);

        // Make sure RatingBar is interactive
        ratingBar.setIsIndicator(false); // This allows user interaction

        // Get location from intent
        location = getIntent().getStringExtra("location");
        if (location == null || location.isEmpty()) {
            Toast.makeText(this, "Location not specified", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("reviews");

        submitButton.setOnClickListener(v -> postReview());
    }

    private void postReview() {
        float rating = ratingBar.getRating();
        String comment = commentEditText.getText().toString().trim();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Validation
        if (comment.isEmpty()) {
            commentEditText.setError("Please enter your review");
            commentEditText.requestFocus();
            return;
        }

        if (rating == 0) {
            Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create review object
        Review review = new Review(userId, rating, comment, location);

        // Push to Firebase
        mDatabase.child(location).push().setValue(review)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Review submitted successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Failed to submit review", Toast.LENGTH_SHORT).show();
                }
            });
    }
}