//package com.example.travelguide2;
//
//public class Review {
//    private String userId;
//    private String userName;
//    private String location;
//    private float rating;
//    private String comment;
//
//    // Empty constructor required for Firebase
//    public Review() {
//    }
//
//    // Full constructor
//    public Review(String userId, String userName, String location, float rating, String comment) {
//        this.userId = userId;
//        this.userName = userName;
//        this.location = location;
//        this.rating = rating;
//        this.comment = comment;
//    }
//
//    // Getters and Setters
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }
//
//    public float getRating() {
//        return rating;
//    }
//
//    public void setRating(float rating) {
//        this.rating = rating;
//    }
//
//    public String getComment() {
//        return comment;
//    }
//
//    public void setComment(String comment) {
//        this.comment = comment;
//    }
//}



package com.example.travelguide2;

public class Review {
    private String userId;
    private String userName;
    private String location;
    private float rating;
    private String comment;

    public Review() {
        // Default constructor required for Firebase
    }

    public Review(String userId, String userName, String location, float rating, String comment) {
        this.userId = userId;
        this.userName = userName;
        this.location = location;
        this.rating = rating;
        this.comment = comment;
    }

    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getLocation() { return location; }
    public float getRating() { return rating; }
    public String getComment() { return comment; }
}
