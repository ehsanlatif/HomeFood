package com.example.ehsan.homefood;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

public class Edit_Dish extends AppCompatActivity  {
    ImageView img,take_pic,add_dish;
    EditText area_txt,title_txt,price_txt,delivery_time_txt,about;
    Spinner category_txt;
    private String userChoosenTask;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    DatabaseReference firebaseDatabase=FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__dish);
        img=(ImageView)findViewById(R.id.dish_img);
        take_pic=(ImageView)findViewById(R.id.take_pic);
        add_dish=(ImageView)findViewById(R.id.add_dish);
        area_txt=(EditText)findViewById(R.id.parcel);
        title_txt=(EditText)findViewById(R.id.title);
        category_txt=(Spinner)findViewById(R.id.spinner);
        price_txt=(EditText)findViewById(R.id.price);
        delivery_time_txt=(EditText)findViewById(R.id.time);
        about=(EditText)findViewById(R.id.about);
        final Dish dish=(Dish)getIntent().getSerializableExtra("dish");
        area_txt.setText(User.getUser().getLocation());
        if(dish!=null) {
            img.setImageBitmap(dish.stringToBitmap());
            title_txt.setText(dish.getTitle());
            category_txt.setSelection(dish.getCategory().equals("Vegeterian")?1:2);
            price_txt.setText(dish.getPrice()+"");
            delivery_time_txt.setText(dish.getAvailability_time() + "");
            about.setText(dish.getAbout_food());
        }
        add_dish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title =title_txt.getText().toString();
                final String about_food = about.getText().toString();
                final String area = area_txt.getText().toString();
                final int availability_time =Integer.parseInt( delivery_time_txt.getText().toString());
                final String category = getResources().getStringArray(R.array.category)[category_txt.getSelectedItemPosition()];
                final String chef = User.getUser().getUserName();
                final int charges = 15;
                final double pickup_lat = MainActivity.latitude;
                final double pickup_long =MainActivity.longitude;
                final int price = Integer.parseInt(price_txt.getText().toString());

                Query lastQuery = firebaseDatabase.child("dishes").orderByChild("dishes").limitToLast(1);
                lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int index;
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                            if (dish == null)
                                index = Integer.parseInt(snapshot.getKey().toString())+1;
                            else index = dish.getDid();
                            Dish dish1 = new Dish(index, title, area, price, dish!=null?dish.getImg():null, 2.5, about_food, "Ready to Serve", category, chef, charges, pickup_lat, pickup_long, availability_time, new Timestamp(new Date().getTime()));
                            if (bm != null)
                                dish1.bitmapToString(bm);

                            firebaseDatabase.child("dishes").child(index+"").setValue(dish1.toMap());
                            Toast.makeText(getApplicationContext(), "Dish Added Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(Edit_Dish.this,Chef_Activity.class));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Handle possible errors.
                    }
                });

            }
        });
        take_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

       // boolean result= Utility.checkPermission(Edit_Dish.this);
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Dish.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utils.checkPermission(Edit_Dish.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),1);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utils.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    Toast.makeText(getApplicationContext(),"Permission Denied!",Toast.LENGTH_SHORT);
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1)
                onSelectFromGalleryResult(data);
            else if (requestCode == 0)
                onCaptureImageResult(data);
        }
    }
    Bitmap bm=null;
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        img.setImageBitmap(bm);
    }
    private void onCaptureImageResult(Intent data) {
        bm = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        img.setImageBitmap(bm);
    }
}
