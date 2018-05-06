package com.example.ehsan.homefood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

public class Add_to_Cart extends AppCompatActivity {
    ImageView img;
    Button cart;
    Spinner spinner;
    TextView title,price,about;
    EditText quantity,special_instructions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to__cart);
        img=(ImageView)findViewById(R.id.img);
        cart=(Button)findViewById(R.id.add_item);
        spinner=(Spinner) findViewById(R.id.spinner2);
        quantity=(EditText)findViewById(R.id.quantity);
        title=(TextView)findViewById(R.id.title);
        special_instructions=(EditText) findViewById(R.id.special_instructions);
        price=(TextView)findViewById(R.id.price);
        about=(TextView)findViewById(R.id.about);
        final Dish dish=(Dish)getIntent().getSerializableExtra("dish");
        img.setImageBitmap(dish.stringToBitmap());
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart cart=new Cart(dish,Integer.parseInt(quantity.getText().toString()),special_instructions.getText().toString(),getResources().getStringArray(R.array.make_with)[spinner.getSelectedItemPosition()]);
                Cart.setCartList(cart);
                startActivity(new Intent(Add_to_Cart.this,Place_Order.class));
                finish();
            }
        });
        title.setText(dish.getTitle());
        price.setText("Rs "+dish.getPrice()+"/-");
        about.setText(dish.getAbout_food());
    }
}
