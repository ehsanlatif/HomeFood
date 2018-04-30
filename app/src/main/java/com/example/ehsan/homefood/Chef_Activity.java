package com.example.ehsan.homefood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

public class Chef_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
    List<Dish> dishes=new LinkedList<>();
    ImageView add_dish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef);
        recyclerView=(RecyclerView)findViewById(R.id.rv);
        progressDialog=new ProgressDialog(this);
        add_dish=(ImageView)findViewById(R.id.add_dish);
        add_dish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Chef_Activity.this,Edit_Dish.class));
            }
        });
//        dishList.add(new Dish("Karachi Biryani","22-B Faisal Town, Lahore",120,R.drawable.biryani,3.5));
//        dishList.add(new Dish("Daal","22-B Faisal Town, Lahore",80,R.drawable.daaal,2.5));
//        dishList.add(new Dish("Spaggatti","22-B Faisal Town, Lahore",180,R.drawable.index,3));
//        dishList.add(new Dish("Karachi Biryani","22-B Faisal Town, Lahore",150,R.drawable.biryani,4));
//        dishList.add(new Dish("Spaggatti","22-B Faisal Town, Lahore",150,R.drawable.index,4.5));
        mAuth= FirebaseAuth.getInstance();

        new DishTask().execute();


    }

    class DishTask extends AsyncTask<Void, Integer, String> {
        Dish dish;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading Dishes...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            DatabaseReference root = firebaseDatabase.getReference().child("dishes");
            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HomeFoodDB db = HomeFoodDB.getAppDatabase(Chef_Activity.this);
                    db.dishDao().deleteAll();
                        for (DataSnapshot dishSnapshot : dataSnapshot.getChildren()) {
                            int did=Integer.parseInt(dishSnapshot.getKey().toString());
                            String chef = dishSnapshot.child("chef").getValue().toString();
                            if (chef.equals(User.getUser().getUserName())) {
                                String title = dishSnapshot.child("title").getValue().toString();
                                String about_food = dishSnapshot.child("about_food").getValue().toString();
                                String area = dishSnapshot.child("area").getValue().toString();
                                int availability_time = Integer.parseInt(dishSnapshot.child("availability_time").getValue().toString());
                                String category = dishSnapshot.child("category").getValue().toString();
                                int charges = Integer.parseInt(dishSnapshot.child("delivery_charged_per_km").getValue().toString());
                                String img = dishSnapshot.child("img").getValue().toString();
                                String uploaded_datetime = dishSnapshot.child("uploaded_datetime").getValue().toString();
                                double pickup_lat = Double.parseDouble(dishSnapshot.child("pickup_lat").getValue().toString());
                                double pickup_long = Double.parseDouble(dishSnapshot.child("pickup_long").getValue().toString());
                                int price = Integer.parseInt(dishSnapshot.child("price").getValue().toString());
                                double rating = Double.parseDouble(dishSnapshot.child("rating").getValue().toString());
                                dish = new Dish(did,title, area, price, img, rating, about_food, "Ready to Serve", category, chef, charges, pickup_lat, pickup_long, availability_time, Timestamp.valueOf(uploaded_datetime));
                                db.dishDao().insertAll(dish);
                                //Toast.makeText(getApplicationContext(), dish.toMap().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return "Done";
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            dishes=HomeFoodDB.getAppDatabase(Chef_Activity.this).dishDao().getChefsDish(User.getUser().getUserName());
            RVAdapter rvAdapter=new RVAdapter(Chef_Activity.this,dishes);
            LinearLayoutManager llm = new LinearLayoutManager(Chef_Activity.this);
            recyclerView.setLayoutManager(llm);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(Chef_Activity.this, LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(rvAdapter);
            super.onPostExecute(result);
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.logout:
            {

                new MainActivity().signOut();
                finish();
                User.setUser(null);
                startActivity(new Intent(this,Login.class));
            }
            break;
        }
        return true;
    }
}
