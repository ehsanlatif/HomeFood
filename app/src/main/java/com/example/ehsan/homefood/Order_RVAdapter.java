package com.example.ehsan.homefood;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Order_RVAdapter extends RecyclerView.Adapter<Order_RVAdapter.DishViewHolder>{

    List<Order> orders;
    Context c;
    Order_RVAdapter(Context c, List<Order> orders){
        this.c=c;
        this.orders = orders;
    }
    @Override
    public DishViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_dish_template, viewGroup, false);
        DishViewHolder pvh = new DishViewHolder(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(DishViewHolder disheViewHolder, int i) {
        disheViewHolder.Dish_Title.setText(Chef_Activity.dishes.get(orders.get(i).getDid()).getTitle());
        disheViewHolder.Parcel.setText(orders.get(i).getParcel());
        disheViewHolder.Price.setText("Rs "+orders.get(i).getTotal()+"/-");
        disheViewHolder.Qty.setText(orders.get(i).getQty()+"Packs");
        disheViewHolder.Dish_Photo.setImageBitmap(Chef_Activity.dishes.get(orders.get(i).getDid()).stringToBitmap());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class DishViewHolder extends RecyclerView.ViewHolder {
        TextView Dish_Title;
        TextView Parcel;
        TextView Price,Qty;
        ImageView Dish_Photo;

        DishViewHolder(View itemView) {
            super(itemView);
            Dish_Title = (TextView)itemView.findViewById(R.id.dish_title);
            Parcel = (TextView)itemView.findViewById(R.id.parcel);
            Price = (TextView)itemView.findViewById(R.id.price);
            Dish_Photo = (ImageView)itemView.findViewById(R.id.dish_img);
            Qty=(TextView) itemView.findViewById(R.id.qty);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos=getAdapterPosition();
                    Order order=orders.get(pos);
                    Toast.makeText(c,"Double Tab to Deliver Order",Toast.LENGTH_SHORT).show();
//                    Intent intent;
//                    if(User.getUser().getChef()){
//                        intent = new Intent(c, Edit_Dish.class);
//                    }else {
//                         intent = new Intent(c, Dish_Details.class);
//                    }
//                    intent.putExtra("order", order);
//                    c.startActivity(intent);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog alertDialog=new AlertDialog.Builder(c)
                            .setTitle("Order Status")
                            .setMessage("Change Status if order is ready!")
                            .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    Query applesQuery = ref.child("orders").orderByChild("chef").equalTo("Aqeel Ahmed");

                                    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                                appleSnapshot.getRef().removeValue();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.e("tag:", "onCancelled", databaseError.toException());
                                        }
                                    });
                                    ((Activity) c).finish();
                                    c.startActivity(new Intent(c,Chef_Orders.class));

                                }
                            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).create();
                    alertDialog.show();
                    return true;
                }
            });
        }
    }

}