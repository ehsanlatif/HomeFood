package com.example.ehsan.homefood;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.DishViewHolder>{

    List<Dish> dishes;

    RVAdapter(List<Dish> dishes){
        this.dishes = dishes;
    }
    @Override
    public DishViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dish_template, viewGroup, false);
        DishViewHolder pvh = new DishViewHolder(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(DishViewHolder disheViewHolder, int i) {
        disheViewHolder.Dish_Title.setText(dishes.get(i).getTitle());
        disheViewHolder.Area.setText(dishes.get(i).getArea());
        disheViewHolder.Price.setText(dishes.get(i).getPrice());
        disheViewHolder.ratingBar.setRating((float)dishes.get(i).getRating());
        disheViewHolder.Dish_Photo.setImageResource(dishes.get(i).getImg());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    @Override
    public int getItemCount() {
        return dishes.size();
    }

    public static class DishViewHolder extends RecyclerView.ViewHolder {
        TextView Dish_Title;
        TextView Area;
        TextView Price;
        ImageView Dish_Photo;
        RatingBar ratingBar;

        DishViewHolder(View itemView) {
            super(itemView);
            Dish_Title = (TextView)itemView.findViewById(R.id.dish_title);
            Area = (TextView)itemView.findViewById(R.id.area);
            Price = (TextView)itemView.findViewById(R.id.price);
            Dish_Photo = (ImageView)itemView.findViewById(R.id.dish_img);
            ratingBar=(RatingBar)itemView.findViewById(R.id.ratingBar);
            ratingBar.setMax(5);
        }
    }

}