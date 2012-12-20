package com.fourkins.rove.sqlite.posts;

public class Post {

    private long id;
    private String userName;
    private double latitude;
    private double longitude;
    private String message;

    public Post() {

    }

    public Post(long id, String name, double latitude, double longitude, String message) {
        super();
        this.id = id;
        this.userName = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = message;
    }

    public Post(String name, double latitude, double longitude, String message) {
        super();
        this.userName = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("UserName=").append(userName);
        sb.append(", Latitude=").append(latitude);
        sb.append(", Longitude=").append(longitude);
        sb.append(", Message=").append(message);

        return sb.toString();
    }

}
