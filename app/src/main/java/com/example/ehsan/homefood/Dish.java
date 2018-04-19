package com.example.ehsan.homefood;

import android.content.Intent;

/**
 * Created by ehsan on 18-04-2018.
 */

public class Dish {
    private String title;
    private String area;
    private int price;
    private Integer img;
    private double rating;

    public  Dish()
    {

    }
    public Dish(String title, String area, int price, Integer img, double rating) {
        this.title = title;
        this.area = area;
        this.price = price;
        this.img = img;
        this.rating = rating;
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

    public Integer getImg() {
        return img;
    }

    public void setImg(Integer img) {
        this.img = img;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
