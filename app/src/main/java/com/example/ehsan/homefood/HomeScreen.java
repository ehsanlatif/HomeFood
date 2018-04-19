package com.example.ehsan.homefood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
        RVAdapter rvAdapter=new RVAdapter(dishList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(rvAdapter);
    }
}
