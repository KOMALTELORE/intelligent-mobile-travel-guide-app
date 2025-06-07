package com.example.travelguide2;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PlaceRecommendationActivity extends AppCompatActivity {

    private ListView lvRecommendations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_recommendation);
        lvRecommendations = findViewById(R.id.lv_recommendations);
        fetchUserData();
    }

    private void fetchUserData() {
        String userId = "YxUNw0hNDYVa8iFd9hfrMY6yqro1";  // Replace with actual user ID
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> preferences = new ArrayList<>();
                List<String> history = new ArrayList<>();

                // Fetching preferences
                DataSnapshot prefSnapshot = snapshot.child("preferences").child(userId);
                for (DataSnapshot child : prefSnapshot.getChildren()) {
                    preferences.add(child.getKey());
                }

                // Fetching history
                DataSnapshot histSnapshot = snapshot.child("history").child(userId);
                for (DataSnapshot child : histSnapshot.getChildren()) {
                    history.add(child.getKey());
                }

                Log.d("UserData", "Preferences: " + preferences);
                Log.d("UserData", "History: " + history);
                runModel(preferences, history);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error fetching data: " + error.getMessage());
            }
        });
    }

    private void runModel(List<String> preferences, List<String> history) {
        try {
            InputStream is = getAssets().open("processed_data.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            List<String> recommendations = new ArrayList<>();
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                // Skip the header row
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] columns = line.split(",");
                String placeName = columns[3];  // 'Name' column
                String combinedFeatures = columns[6];  // 'combined_features' column

                Log.d("CSVProcessing", "Processing place: " + placeName);

                // Match based on the 'combined_features' column (which might contain a description like "Temple Delhi Delhi")
                for (String pref : preferences) {
                    if (combinedFeatures.toLowerCase().contains(pref.toLowerCase())) {
                        recommendations.add(placeName);
                        Log.d("CSVProcessing", "Match found for: " + placeName);
                        break;
                    }
                }
            }
            reader.close();

            if (recommendations.isEmpty()) {
                Log.d("Recommendations", "No recommendations found.");
            } else {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recommendations);
                lvRecommendations.setAdapter(adapter);
                Log.d("Recommendations", recommendations.toString());
            }
        } catch (Exception e) {
            Log.e("ModelError", "Error running model: " + e.getMessage());
        }
    }

}
