package com.example.ehsan.homefood;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.firebase.database.Exclude;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ehsan on 18-04-2018.
 */
@Entity(tableName = "dish")
public class Dish implements Serializable {
    @PrimaryKey
    private int did;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "area")
    private String area;

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public String getAbout_food() {
        return about_food;
    }

    public void setAbout_food(String about_food) {
        this.about_food = about_food;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getChef() {
        return chef;
    }

    public void setChef(String chef) {
        this.chef = chef;
    }

    public Bitmap stringToBitmap(){

        String pureBase64Encoded = img.substring(img.indexOf(",") + 1);
        byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        return decodedByte;
    }
    public String bitmapToString(Bitmap bmp){
       // Bitmap bmp =  BitmapFactory.decodeResource(getResources(),R.drawable.chicken);//your image
        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
        bmp.recycle();
        byte[] byteArray = bYtE.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        img=encodedImage;
        return encodedImage;
    }
    public int getCharges() {
        return charges;
    }

    public void setCharges(int charges) {
        this.charges = charges;
    }

    public double getPickup_lat() {
        return pickup_lat;
    }

    public void setPickup_lat(double pickup_lat) {
        this.pickup_lat = pickup_lat;
    }

    public double getPickup_long() {
        return pickup_long;
    }

    public void setPickup_long(double pickup_long) {
        this.pickup_long = pickup_long;
    }

    public int getAvailability_time() {
        return availability_time;
    }

    public void setAvailability_time(int availability_time) {
        this.availability_time = availability_time;
    }

    public String getUploaded_datetime() {
        return uploaded_datetime;
    }

    public void setUploaded_datetime(String uploaded_datetime) {
        this.uploaded_datetime = uploaded_datetime;
    }

    @ColumnInfo(name = "price")

    private int price;
    @ColumnInfo(name = "img")
    private String img;
    @ColumnInfo(name = "rating")
    private double rating;
    @ColumnInfo(name = "about_food")
    private String about_food;
    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "category")
    private String category;
    @ColumnInfo(name = "chef")
    private String chef;
    @ColumnInfo(name = "charges")
    private int charges;
    @ColumnInfo(name = "pickup_lat")
    private double pickup_lat;
    @ColumnInfo(name = "pickup_long")
    private double pickup_long;
    @ColumnInfo(name = "availability_time")
    private int availability_time;
    @ColumnInfo(name = "uploaded_datetime")
    private String uploaded_datetime;
    public  Dish()
    {

    }

    public Dish(int did,String title, String area, int price, String img, double rating, String about_food, String status, String category, String chef, int charges, double pickup_lat, double pickup_long, int availability_time, Timestamp uploaded_datetime) {
        this.did=did;
        this.title = title;
        this.area = area;
        this.price = price;
        this.img = img;
        this.rating = rating;
        this.about_food = about_food;
        this.status = status;
        this.category = category;
        this.chef = chef;
        this.charges = charges;
        this.pickup_lat = pickup_lat;
        this.pickup_long = pickup_long;
        this.availability_time = availability_time;
        this.uploaded_datetime = uploaded_datetime.toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("area", area);
        result.put("price", price);
        result.put("img", img);
        result.put("rating", rating);
        result.put("about_food",about_food);
        result.put("category", category);
        result.put("chef", chef);
        result.put("charges", charges);
        result.put("pickup_lat", pickup_lat);
        result.put("pickup_long",pickup_long);
        result.put("delivery_charged_per_km",charges);
        result.put("availability_time", availability_time);
        result.put("uploaded_datetime",uploaded_datetime);
        return result;
    }
}
