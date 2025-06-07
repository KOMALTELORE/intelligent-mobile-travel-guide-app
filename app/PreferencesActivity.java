package com.example.travelguide2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Gravity;

public class PreferencesActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private String userId;

    private LinearLayout selectedPreferencesLayout;
    private Map<String, View> preferenceViews = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        // Get the current user ID from Firebase Authentication
        FirebaseAuth auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("preferences");
        selectedPreferencesLayout = findViewById(R.id.selectedPreferencesLayout);


        setupButton(R.id.button_religious, "Religious");
        setupButton(R.id.button_nature, "Nature");
        setupButton(R.id.button_parks_gardens, "Parks & Gardens");
        setupButton(R.id.button_historical_cultural, "Historical & Cultural");
        setupButton(R.id.button_entertainment, "Entertainment");
        setupButton(R.id.button_urban_landmarks, "Urban & Landmarks");
        setupButton(R.id.button_sports_adventure, "Sports & Adventure");
        setupButton(R.id.button_bridges_dams, "Bridges & Dams");
        setupButton(R.id.button_scenic_viewpoints, "Scenic & Viewpoints");
        setupButton(R.id.button_agriculture_plantation, "Agriculture & Plantation");
        setupButton(R.id.button_spiritual_meditation, "Spiritual & Meditation");
        setupButton(R.id.button_islands, "Islands");
        setupButton(R.id.button_other, "Other");

    }

    private void setupButton(int buttonId, String preference) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> addPreference(preference));
    }

    private void addPreference(String preference) {
        if (preferenceViews.containsKey(preference)) {
            Toast.makeText(this, "Already added", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> preferencesMap = new HashMap<>();
        preferencesMap.put(preference, true);

        databaseReference.child(userId).updateChildren(preferencesMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Preference added: " + preference, Toast.LENGTH_SHORT).show();
                    showPreferenceTag(preference);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to add preference", Toast.LENGTH_SHORT).show());
    }

    private void showPreferenceTag(String preference) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setBackgroundColor(Color.LTGRAY);
        layout.setPadding(16, 8, 16, 8);

        TextView textView = new TextView(this);
        textView.setText(preference);
        textView.setTextColor(Color.BLACK);

        ImageView deleteIcon = new ImageView(this);
        deleteIcon.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        deleteIcon.setPadding(16, 0, 0, 0);
        deleteIcon.setOnClickListener(v -> removePreference(preference));

        layout.addView(textView);
        layout.addView(deleteIcon);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 24, 0);
        layout.setLayoutParams(params);
        layout.setGravity(Gravity.CENTER_VERTICAL);

        selectedPreferencesLayout.addView(layout);
        preferenceViews.put(preference, layout);
    }

    private void removePreference(String preference) {
        databaseReference.child(userId).child(preference).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Preference removed: " + preference, Toast.LENGTH_SHORT).show();
                    View view = preferenceViews.get(preference);
                    if (view != null) {
                        selectedPreferencesLayout.removeView(view);
                        preferenceViews.remove(preference);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to remove preference", Toast.LENGTH_SHORT).show());
    }
}
