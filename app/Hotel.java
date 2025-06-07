package com.example.travelguide2;

public class Hotel {
    private String name;
    private String imageUrl;
    private double rating;
    private int priceLevel;

    public Hotel(String name, String imageUrl, double rating, int priceLevel) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.priceLevel = priceLevel;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getRating() {
        return rating;
    }

    public int getPriceLevel() {
        return priceLevel;
    }
}
