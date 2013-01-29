package com.fourkins.rove.users;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.json.JSONException;
import org.json.JSONObject;

/* Password Authentication:
 * - When registering a user, random "salt" is generated.  
 * - Password is salted with this random string, and then hashed.  This password gets stored in server.
 * - When user logs in, the salt for the user is fetched from database.  Use it to salt the password input, and hash.  This hashed password is match server's hashed password.
 */

public class User {
    private int userId;
    private String username;
    private String realName;
    // private String firstName;
    // private String lastName;
    // private char gender;
    // private Date birthdate;
    private String email;
    private String salt;
    private String encryptedPassword;

    public User() {

    }

    public User(String username, String realName, String email, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        super();
        this.username = username;
        this.realName = realName;
        this.email = email;

        SecureRandom random = new SecureRandom();
        int randomInt = random.nextInt();

        this.salt = Integer.toString(randomInt);

        String encryptedPassword = getHash(password, salt);
        this.encryptedPassword = encryptedPassword;

    }

    public User(JSONObject jsonObject) {
        try {
            userId = jsonObject.getInt("userId");
            username = jsonObject.getString("username");
            realName = jsonObject.getString("realName");
            email = jsonObject.getString("email");
            salt = jsonObject.getString("salt");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public JSONObject getJson() {
        JSONObject jObj = new JSONObject();

        try {
            jObj.put("username", username);
            jObj.put("realName", realName);
            jObj.put("email", email);
            jObj.put("password", encryptedPassword);
            jObj.put("salt", salt);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jObj;
    }

    public String getHash(String password, String sSalt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(sSalt.getBytes());
        byte[] bEncryptedPassword = digest.digest(password.getBytes("UTF-8"));

        // Convert byte[] to String
        StringBuffer sb = new StringBuffer();
        for (byte b : bEncryptedPassword) {
            sb.append(String.format("%02x", b));
        }
        String sEncyrptedPassword = new String(sb.toString());
        return sEncyrptedPassword;
    }
}
