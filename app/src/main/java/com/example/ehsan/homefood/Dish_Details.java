package com.example.ehsan.homefood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class Dish_Details extends AppCompatActivity {

    ImageView img,cart;
    RatingBar ratingBar;
    TextView area,title,category,price,status,delivery_time,about;
    Button mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish__details);
        img=(ImageView)findViewById(R.id.dish_img);
        cart=(ImageView)findViewById(R.id.cart);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar2);
        area=(TextView)findViewById(R.id.area);
        title=(TextView)findViewById(R.id.title);
        category=(TextView)findViewById(R.id.category);
        price=(TextView)findViewById(R.id.price);
        status=(TextView)findViewById(R.id.status);
        delivery_time=(TextView)findViewById(R.id.time);
        about=(TextView)findViewById(R.id.about);
        mapView=(Button)findViewById(R.id.mapView);
        final Dish dish=(Dish)getIntent().getSerializableExtra("dish");
        img.setImageBitmap(dish.stringToBitmap());
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ratingBar.setRating((float)dish.getRating());
        area.setText(dish.getArea());
        title.setText(dish.getTitle());
        category.setText(dish.getCategory());
        price.setText("Rs "+dish.getPrice()+"/-");
        status.setText(dish.getStatus());
        delivery_time.setText(dish.getAvailability_time()+"minutes");
        about.setText(dish.getAbout_food());
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dish_Details.this,MapsActivity.class);
                intent.putExtra("lat",dish.getPickup_lat());
                intent.putExtra("long",dish.getPickup_long());
                startActivity(intent);
            }
        });
    }
}
