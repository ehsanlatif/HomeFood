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
