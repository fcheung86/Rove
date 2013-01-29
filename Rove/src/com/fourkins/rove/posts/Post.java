package com.fourkins.rove.posts;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

public class Post {

    private int postId;
    private int userId;
    private String username;
    private double latitude;
    private double longitude;
    private String message;
    private String address;
    private String city;
    private Timestamp timestamp;

    public Post() {

    }

    public Post(JSONObject jsonObject) {
        try {
            postId = jsonObject.getInt("postId");
            userId = jsonObject.getInt("userId");
            username = jsonObject.getString("username");
            latitude = jsonObject.getDouble("latitude");
            longitude = jsonObject.getDouble("longitude");
            message = jsonObject.getString("message");
            timestamp = new Timestamp(jsonObject.getLong("timestamp"));
            address = jsonObject.getString("address");
            city = jsonObject.getString("city");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Post(int userId, String username, double latitude, double longitude, String message, String address, String city, Timestamp timestamp) {
        super();

        this.userId = userId;
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = message;
        this.timestamp = timestamp;
        this.address = address;
        this.city = city;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public JSONObject getJson() {
        JSONObject jObj = new JSONObject();

        try {
            jObj.put("userId", userId);
            jObj.put("latitude", latitude);
            jObj.put("longitude", longitude);
            jObj.put("message", message);
            jObj.put("address", address);
            jObj.put("city", city);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jObj;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("postId=").append(postId);
        sb.append(", userId=").append(userId);
        sb.append(", userName=").append(username);
        sb.append(", latitude=").append(latitude);
        sb.append(", longitude=").append(longitude);
        sb.append(", message=").append(message);
        sb.append(", address=").append(address);
        sb.append(", city=").append(city);
        sb.append(", timestamp=").append(timestamp);

        return sb.toString();
    }

}
