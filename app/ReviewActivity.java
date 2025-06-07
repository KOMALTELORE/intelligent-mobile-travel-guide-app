package com.example.travelguide2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReviewAdapter reviewAdapter;
    private ProgressBar progressBar;
    private List<Review> reviewList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(reviewList);
        recyclerView.setAdapter(reviewAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference("reviews");

        loadReviews();
    }

    private void loadReviews() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);  // Show progress while loading
                if (dataSnapshot.exists()) {
                    reviewList.clear();
                    for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot reviewSnapshot : locationSnapshot.getChildren()) {
                            Review review = reviewSnapshot.getValue(Review.class);
                            if (review != null) {
                                reviewList.add(review);
                            }
                        }
                    }
                    reviewAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);  // Hide progress once done
                    recyclerView.setVisibility(View.VISIBLE);  // Make the list visible
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ReviewActivity.this, "No reviews found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ReviewActivity.this, "Failed to load reviews", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
