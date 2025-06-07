package com.example.travelguide2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PlaceSuggestionActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PlaceAdapter placeAdapter;
    private List<Place> placeList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private String userId;

    // Mapping user preferences to specific place types
    private static final Map<String, List<String>> preferenceMap = new HashMap<>();

    static {
        preferenceMap.put("Religious", Arrays.asList("Temple", "Religious Shrine", "Shrine", "Religious Site", "Gurudwara", "Church", "Mosque", "Religious Complex"));
        preferenceMap.put("Nature", Arrays.asList("Hill", "Gravity Hill", "Valley", "Mountain Peak", "Waterfall", "River Island", "Lake", "Cave", "Natural Feature"));
        preferenceMap.put("Parks & Gardens", Arrays.asList("National Park", "Zoo", "Botanical Garden", "Wildlife Sanctuary", "Bird Sanctuary", "Park"));
        preferenceMap.put("Historical & Cultural", Arrays.asList("Fort", "Palace", "Monument", "Historical", "Cultural", "War Memorial", "Memorial", "Tomb", "Mausoleum", "Tombs"));
        preferenceMap.put("Entertainment", Arrays.asList("Theme Park", "Amusement Park", "Film Studio", "Race Track", "Mall", "Commercial Complex", "Entertainment"));
        preferenceMap.put("Urban & Landmarks", Arrays.asList("Landmark", "Observatory", "Government Building", "Urban Development Project", "Township", "Market", "Promenade"));
        preferenceMap.put("Sports & Adventure", Arrays.asList("Cricket Ground", "Adventure Sport", "Trekking", "Ski Resort"));
        preferenceMap.put("Bridges & Dams", Arrays.asList("Bridge", "Suspension Bridge", "Dam"));
        preferenceMap.put("Scenic & Viewpoints", Arrays.asList("Scenic Area", "Scenic Point", "Viewpoint", "Sunrise Point"));
        preferenceMap.put("Agriculture & Plantation", Arrays.asList("Tea Plantation", "Vineyard", "Orchard"));
        preferenceMap.put("Spiritual & Meditation", Arrays.asList("Spiritual Center"));
        preferenceMap.put("Islands", Arrays.asList("Island"));
        preferenceMap.put("Other", Arrays.asList("Border Crossing", "Rock Carvings", "Stepwell", "Prehistoric Site", "Site", "Sculpture Garden", "Confluence", "Village"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_suggestion);

        recyclerView = findViewById(R.id.recyclerViewPlaces);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        placeAdapter = new PlaceAdapter(this, placeList);
        recyclerView.setAdapter(placeAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid(); // Get logged-in user's ID dynamically
            databaseReference = FirebaseDatabase.getInstance().getReference("preferences").child(userId);
            loadUserPreferences();
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if user is not logged in
        }
    }

    private void loadUserPreferences() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(PlaceSuggestionActivity.this, "No preferences found!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Set<String> selectedPlaceTypes = new HashSet<>();
                for (DataSnapshot prefSnapshot : snapshot.getChildren()) {
                    String preference = prefSnapshot.getKey();
                    if (preferenceMap.containsKey(preference)) {
                        selectedPlaceTypes.addAll(preferenceMap.get(preference));
                    }
                }

                if (selectedPlaceTypes.isEmpty()) {
                    Toast.makeText(PlaceSuggestionActivity.this, "No matching places found!", Toast.LENGTH_SHORT).show();
                    return;
                }

                loadPlacesFromJson(selectedPlaceTypes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PlaceSuggestionActivity.this, "Failed to fetch preferences", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPlacesFromJson(Set<String> selectedTypes) {
        try {
            InputStream is = getAssets().open("newapp.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            List<Place> allPlaces = new Gson().fromJson(json, new TypeToken<List<Place>>() {}.getType());
            Map<String, Integer> typeCount = new HashMap<>();

            for (Place place : allPlaces) {
                if (selectedTypes.contains(place.getType())) {
                    typeCount.put(place.getType(), typeCount.getOrDefault(place.getType(), 0) + 1);
                    if (typeCount.get(place.getType()) <= 3) {
                        placeList.add(place);
                    }
                }
            }

            placeAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            Log.e("PlaceSuggestion", "Error loading JSON", e);
            Toast.makeText(this, "Failed to load places", Toast.LENGTH_SHORT).show();
        }
    }
}