package com.example.travelguide2;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<String> historyList;
    private DatabaseReference mDatabase;
    private String userId;

    public HistoryAdapter(List<String> historyList) {
        this.historyList = historyList;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        String location = historyList.get(position);
        holder.locationName.setText(location);

        // Added click listener for the entire item view
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ReviewActivity.class);
            intent.putExtra("location", location);
            v.getContext().startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> {
            // Remove from Firebase
            mDatabase.child("history").child(userId).child(location).removeValue();
            // Remove from local list
            historyList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, historyList.size());
        });

        // Inside onBindViewHolder method of HistoryAdapter.java
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PostReviewActivity.class);
            intent.putExtra("location", location);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView locationName;
        ImageButton deleteButton;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            locationName = itemView.findViewById(R.id.locationName);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}