package com.example.travelguide2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {

    private List<Hotel> hotelList;
    private Context context;

    public HotelAdapter(Context context, List<Hotel> hotelList) {
        this.context = context;
        this.hotelList = hotelList;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hotel, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);
        holder.tvHotelName.setText(hotel.getName());
        holder.tvHotelRating.setText("Rating: " + hotel.getRating());
        holder.tvHotelPrice.setText("Price Level: " + hotel.getPriceLevel());
        Glide.with(context).load(hotel.getImageUrl()).into(holder.ivHotelImage);
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public void updateData(List<Hotel> hotels) {
        this.hotelList = hotels;
        notifyDataSetChanged();
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHotelImage;
        TextView tvHotelName, tvHotelRating, tvHotelPrice;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHotelImage = itemView.findViewById(R.id.ivHotelImage);
            tvHotelName = itemView.findViewById(R.id.tvHotelName);
            tvHotelRating = itemView.findViewById(R.id.tvHotelRating);
            tvHotelPrice = itemView.findViewById(R.id.tvHotelPrice);
        }
    }
}
