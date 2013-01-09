package com.fourkins.rove.sqlite.posts;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

public class Post {

    private long id;
    private String userName;
    private double latitude;
    private double longitude;
    private String message;
    private Timestamp timestamp;

    public Post() {

    }

    public Post(JSONObject jsonObject) {
        try {
            long postId = jsonObject.getLong("postId");
            long userId = jsonObject.getLong("userId");
            double lat = jsonObject.getDouble("latitude");
            double lng = jsonObject.getDouble("longitude");
            String message = jsonObject.getString("message");
            String time = jsonObject.getString("timestamp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Post(long id, String name, double latitude, double longitude, String message, Timestamp timestamp) {
        super();
        this.id = id;
        this.userName = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Post(String name, double latitude, double longitude, String message, Timestamp timestamp) {
        super();
        this.userName = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = message;
        this.timestamp = timestamp;
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public JSONObject getJson() {
        JSONObject jObj = new JSONObject();

        try {
            jObj.put("userId", 1); // TODO need to put in the real userId
            jObj.put("latitude", latitude);
            jObj.put("longitude", longitude);
            jObj.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jObj;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("UserName=").append(userName);
        sb.append(", Latitude=").append(latitude);
        sb.append(", Longitude=").append(longitude);
        sb.append(", Message=").append(message);
        sb.append(", Timestamp=").append(timestamp);

        return sb.toString();
    }

}
