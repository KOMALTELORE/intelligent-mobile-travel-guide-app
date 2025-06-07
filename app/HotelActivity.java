package com.example.travelguide2;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class HotelActivity extends AppCompatActivity {

    private RecyclerView recyclerViewHotels;
    private HotelAdapter hotelAdapter;
    private List<Hotel> hotelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        recyclerViewHotels = findViewById(R.id.recyclerViewHotels);
        recyclerViewHotels.setLayoutManager(new LinearLayoutManager(this));
        hotelList = new ArrayList<>();
        hotelAdapter = new HotelAdapter(this, hotelList);
        recyclerViewHotels.setAdapter(hotelAdapter);

        fetchNearbyHotels();
    }

    private void fetchNearbyHotels() {
        String apiKey = getString(R.string.google_maps_key);
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?keyword=restaurant&location=19.5761207,74.1668977&name=restaurant&radius=5000&key=" + apiKey;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray results = jsonObject.getJSONArray("results");
                    List<Hotel> hotels = new ArrayList<>();

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject place = results.getJSONObject(i);
                        String name = place.getString("name");
                        double rating = place.optDouble("rating", 0);
                        int priceLevel = place.optInt("price_level", 0);

                        // Get image URL from photo reference if available
                        String imageUrl = "";
                        if (place.has("photos")) {
                            JSONArray photos = place.getJSONArray("photos");
                            if (photos.length() > 0) {
                                String photoReference = photos.getJSONObject(0).getString("photo_reference");
                                imageUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoReference + "&key=" + apiKey;
                            }
                        }

                        hotels.add(new Hotel(name, imageUrl, rating, priceLevel));
                    }
                    hotelAdapter.updateData(hotels);
                } catch (Exception e) {
                    Log.e("HotelError", "Error parsing hotel data: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HotelError", "Error fetching hotels: " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }
}
