package com.example.ehsan.homefood;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ehsan on 01-05-2018.
 */

public class Cart {
    private Dish dish;
    private int Quantity;
    private String Additional_features;
    private String make_with;

    private static List<Cart> cartList=new LinkedList<>();
    public Cart(Dish dish, int quantity, String additional_features, String make_with) {
        this.dish = dish;
        Quantity = quantity;
        Additional_features = additional_features;
        this.make_with = make_with;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getAdditional_features() {
        return Additional_features;
    }

    public void setAdditional_features(String additional_features) {
        Additional_features = additional_features;
    }

    public String getMake_with() {
        return make_with;
    }

    public void setMake_with(String make_with) {
        this.make_with = make_with;
    }

    public static List<Cart> getCartList() {
        return cartList;
    }

    public static void setCartList(Cart add_item) {
        Cart.cartList.add(add_item);
    }
    public static  void Check_out()
    {
        cartList.clear();
    }
}
