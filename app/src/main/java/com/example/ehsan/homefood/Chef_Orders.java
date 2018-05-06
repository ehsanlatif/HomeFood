package com.example.ehsan.homefood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class Chef_Orders extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
    List<Order> orders=new LinkedList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef__orders);
        recyclerView=(RecyclerView)findViewById(R.id.rv);
        progressDialog=new ProgressDialog(this);

        mAuth= FirebaseAuth.getInstance();
        progressDialog.setMessage("Loading Orders...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        DatabaseReference root = firebaseDatabase.getReference().child("orders");
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dishSnapshot : dataSnapshot.getChildren()) {
                    String chef = dishSnapshot.child("chef").getValue().toString();
                    if (chef.equals(User.getUser().getUserName())) {
                        String contact = dishSnapshot.child("contact").getValue().toString();
                        int did = Integer.parseInt(dishSnapshot.child("did").getValue().toString());
                        String parcel = dishSnapshot.child("parcel").getValue().toString();
                        int Qty = Integer.parseInt(dishSnapshot.child("qty").getValue().toString());
                        double delivery_lat = Double.parseDouble(dishSnapshot.child("delivery_lat").getValue().toString());
                        double delivery_long = Double.parseDouble(dishSnapshot.child("delivery_long").getValue().toString());
                        int total = Integer.parseInt(dishSnapshot.child("total").getValue().toString());
                        Order order = new Order(parcel,total,delivery_lat,delivery_long,did,Qty,contact,chef);
                        orders.add(order);
                        //Toast.makeText(getApplicationContext(), dish.toMap().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressDialog.dismiss();
                Order_RVAdapter rvAdapter=new Order_RVAdapter(Chef_Orders.this,orders);
                LinearLayoutManager llm = new LinearLayoutManager(Chef_Orders.this);
                recyclerView.setLayoutManager(llm);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(Chef_Orders.this, LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(rvAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

}
