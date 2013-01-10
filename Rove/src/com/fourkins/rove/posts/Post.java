package com.fourkins.rove.posts;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

public class Post {

    private long postId;
    private long userId;
    private String username;
    private double latitude;
    private double longitude;
    private String message;
    private Timestamp timestamp;

    public Post() {

    }

    public Post(JSONObject jsonObject) {
        try {
            postId = jsonObject.getLong("postId");
            userId = jsonObject.getLong("userId");
            username = jsonObject.getString("username");
            latitude = jsonObject.getDouble("latitude");
            longitude = jsonObject.getDouble("longitude");
            message = jsonObject.getString("message");
            timestamp = new Timestamp(jsonObject.getLong("timestamp"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Post(long postId, String userName, double latitude, double longitude, String message, Timestamp timestamp) {
        super();
        this.postId = postId;
        this.username = userName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Post(String username, double latitude, double longitude, String message, Timestamp timestamp) {
        super();
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = message;
        this.timestamp = timestamp;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
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

        sb.append("postId=").append(postId);
        sb.append(", userId=").append(userId);
        sb.append(", userName=").append(username);
        sb.append(", latitude=").append(latitude);
        sb.append(", longitude=").append(longitude);
        sb.append(", message=").append(message);
        sb.append(", timestamp=").append(timestamp);

        return sb.toString();
    }

}
