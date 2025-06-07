package com.example.travelguide2;

public class Place {
    private String name;
    private String state;
    private String city;
    private String type;
    private String image;

    public Place() {}

    public Place(String name, String state, String city, String type, String image) {
        this.name = name;
        this.state = state;
        this.city = city;
        this.type = type;
        this.image = image;
    }

    public String getName() { return name; }
    public String getState() { return state; }
    public String getCity() { return city; }
    public String getType() { return type; }
    public String getImage() { return image; }
}