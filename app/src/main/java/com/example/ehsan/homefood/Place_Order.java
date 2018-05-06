package com.example.ehsan.homefood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Place_Order extends AppCompatActivity {
    Button check_out;
    ImageView pickup,delivery;
    TextView title,price,qty,sub_total,discount,tax,delivery_charge,total;
    int charges=0,local_charge=0;
    Boolean deliver=false;
    int Total=0,sb_total=0;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    DatabaseReference firebaseDatabase= FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place__order);
        check_out=(Button) findViewById(R.id.check_out);
        pickup=(ImageView) findViewById(R.id.pickup);
        delivery=(ImageView) findViewById(R.id.delivery);
        title=(TextView)findViewById(R.id.title);
        qty=(TextView) findViewById(R.id.qty);
        price=(TextView)findViewById(R.id.price);
        sub_total=(TextView)findViewById(R.id.subtotal);
        discount=(TextView)findViewById(R.id.discount);
        tax=(TextView) findViewById(R.id.tax);
        delivery_charge=(TextView)findViewById(R.id.delivery_charge);
        total=(TextView)findViewById(R.id.total);
        List<Cart> cartList=Cart.getCartList();
        for(Cart cart:cartList)
        {
            title.setText(cart.getDish().getTitle());
            qty.setText(cart.getQuantity()+"");
            price.setText(cart.getDish().getPrice()+"");
            sb_total+=cart.getQuantity()*cart.getDish().getPrice();
            double dis=distance(cart.getDish().getPickup_lat(),cart.getDish().getPickup_long(),MainActivity.latitude,MainActivity.longitude,"K");
            local_charge+= (cart.getDish().getCharges()*dis);
        }
        sub_total.setText(sb_total+".00/-");
        pickup.setBackgroundResource(R.drawable.red_round_button);
        delivery.setBackgroundResource(R.drawable.button_border);
        pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickup.setBackgroundResource(R.drawable.red_round_button);
                delivery.setBackgroundResource(R.drawable.button_border);
                charges=0;
                deliver=false;
                delivery_charge.setText(charges+".00/-");
                Total=(sb_total+charges+10);
                total.setText(Total+".00/-");
            }
        });
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickup.setBackgroundResource(R.drawable.button_border);
                delivery.setBackgroundResource(R.drawable.red_round_button);
                charges=local_charge;
                deliver=true;
                delivery_charge.setText(charges+".00/-");
                Total=(sb_total+charges+10);
                total.setText(Total+".00/-");
            }
        });

        check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Order order=new Order(deliver?"Delivery":"Pickup",Total,MainActivity.latitude,MainActivity.longitude);
                firebaseDatabase.child("orders").child("1").setValue(order.to_map());
                Cart.Check_out();
                Toast.makeText(getApplicationContext(), "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(Place_Order.this,HomeScreen.class));
            }
        });

    }
    public double distance(double lat1, double lon1, double lat2, double lon2, String sr) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (sr.equals("K")) {
            dist = dist * 1.609344;
        } else if (sr.equals("N")) {
            dist = dist * 0.8684;
        }
        return (dist);
    }
    public double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    public double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
