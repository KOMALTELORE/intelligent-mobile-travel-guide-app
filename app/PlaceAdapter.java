package com.example.travelguide2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {
    private Context context;
    private List<Place> placeList;

    public PlaceAdapter(Context context, List<Place> placeList) {
        this.context = context;
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.nameTextView.setText(place.getName());
        holder.cityTextView.setText(place.getCity());
        holder.stateTextView.setText(place.getState());
        //holder.typeTextView.setText(place.getType());

        Glide.with(context)
                .load(place.getImage())
                .into(holder.imageView);

        // 🟢 Launch BudgetActivity on item click with place name
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BudgetActivity.class);
            intent.putExtra("place_name", place.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, cityTextView, stateTextView, typeTextView;
        ImageView imageView;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            stateTextView = itemView.findViewById(R.id.stateTextView);
            //typeTextView = itemView.findViewById(R.id.place_type);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
