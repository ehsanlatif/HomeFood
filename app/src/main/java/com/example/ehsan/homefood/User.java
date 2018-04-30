package com.example.ehsan.homefood;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ehsan on 28-04-2018.
 */
@IgnoreExtraProperties
public class User {
    private String email;
    private String userName;
    private String phoneNumber;
    private String location;
    private Boolean isChef;

    public Boolean getChef() {
        return isChef;
    }

    public void setChef(Boolean chef) {
        isChef = chef;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private static User user;
    public static User getUser(){
        return user;
    }
    public static void setUser(User user1){
        user=user1;
    }
    public User(){

    }
    public User(String email, String userName, String phoneNumber, String location,Boolean isChef) {
        this.email = email;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.isChef=isChef;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("userName", userName);
        result.put("phoneNumber", phoneNumber);
        result.put("location", location);
        result.put("isChef", isChef);
        return result;
    }

}
