package com.example.ehsan.homefood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    List<Dish> dishList=new LinkedList<>();
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        recyclerView=(RecyclerView)findViewById(R.id.rv);
        dishList.add(new Dish("Karachi Biryani","22-B Faisal Town, Lahore",120,R.drawable.biryani,3.5));
        dishList.add(new Dish("Daal","22-B Faisal Town, Lahore",80,R.drawable.daaal,2.5));
        dishList.add(new Dish("Spaggatti","22-B Faisal Town, Lahore",180,R.drawable.index,3));
        dishList.add(new Dish("Karachi Biryani","22-B Faisal Town, Lahore",150,R.drawable.biryani,4));
        dishList.add(new Dish("Spaggatti","22-B Faisal Town, Lahore",150,R.drawable.index,4.5));
        Toast.makeText(getApplicationContext(),User.getUser().toMap().toString(),Toast.LENGTH_SHORT).show();
        RVAdapter rvAdapter=new RVAdapter(dishList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(rvAdapter);
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
                User.setUser(null);
                new MainActivity().signOut();
                finish();
                startActivity(new Intent(this,Login.class));
            }
                break;
        }
        return true;
    }
}
