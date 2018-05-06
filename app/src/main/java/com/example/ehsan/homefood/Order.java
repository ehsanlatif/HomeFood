package com.example.ehsan.homefood;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Delayed;

/**
 * Created by ehsan on 06-05-2018.
 */

public class Order implements Serializable {

    private String parcel;
    private int Total;
    private double Delivery_lat;
    private double Delivery_long;
    private int did;
    private int qty;
    private String chef;
    private String cntct;

    public String getParcel() {
        return parcel;
    }

    public void setParcel(String parcel) {
        this.parcel = parcel;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public double getDelivery_lat() {
        return Delivery_lat;
    }

    public void setDelivery_lat(double delivery_lat) {
        Delivery_lat = delivery_lat;
    }

    public double getDelivery_long() {
        return Delivery_long;
    }

    public void setDelivery_long(double delivery_long) {
        Delivery_long = delivery_long;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getCntct() {
        return cntct;
    }

    public void setCntct(String cntct) {
        this.cntct = cntct;
    }

    public Order(String parcel, int total, double delivery_lat, double delivery_long, int did, int qty, String cntct,String Chef) {
        this.parcel = parcel;
        Total = total;
        Delivery_lat = delivery_lat;
        Delivery_long = delivery_long;
        this.did = did;
        this.qty = qty;
        this.cntct = cntct;
        chef=Chef;
    }

    public Order(String parcel, int total, double delivery_lat, double delivery_long) {
        this.parcel = parcel;
        Total = total;
        Delivery_lat = delivery_lat;
        Delivery_long = delivery_long;
    }
    public Map<String,Object> to_map()
    {
        Map<String,Object> order=new HashMap<>();
        order.put("did",Cart.getCartList().get(0).getDish().getDid());
        order.put("parcel",parcel);
        order.put("delivery_lat",Delivery_lat);
        order.put("delivery_long", Delivery_long);
        order.put("qty",Cart.getCartList().get(0).getQuantity());
        order.put("total",Total);
        order.put("contact",User.getUser().getPhoneNumber());
        order.put("chef",Cart.getCartList().get(0).getDish().getChef());
        return  order;
    }
}
