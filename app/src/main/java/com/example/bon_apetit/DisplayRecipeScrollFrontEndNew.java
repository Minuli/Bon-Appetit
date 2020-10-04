package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Models.Cart;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DisplayRecipeScrollFrontEndNew extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    String recipeName;
    DatabaseReference db;
    StorageReference storage;
    TextView servings, price;
    EditText method;
    ImageView imageBox;
    TextView recipe;
    Button getIngredients,addtocart;
    Cart cart;
    DatabaseReference dbcart;
    ArrayList<Cart> icart;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe_scroll_frontend_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        recipe = findViewById(R.id.recipe);
        servings = findViewById(R.id.txtServings);
        method = findViewById(R.id.methodText3);
        price = findViewById(R.id.txtPrice);
        imageBox = findViewById(R.id.imageBox);
        getIngredients = findViewById(R.id.getIngredient);
        addtocart=findViewById(R.id.addTocart);
        Intent myIntent = getIntent();
        progressDialog = new ProgressDialog(DisplayRecipeScrollFrontEndNew.this);
        icart=new ArrayList<>();
        recipeName = myIntent.getStringExtra("EnteredRecipe");
        System.out.println("getname :"+recipeName);

        db = FirebaseDatabase.getInstance().getReference().child("Recipes").child("Description").child(recipeName);
        storage = FirebaseStorage.getInstance().getReference();
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    recipe.setText(dataSnapshot.child("recipeName").getValue().toString());
                    servings.setText(dataSnapshot.child("servings").getValue().toString());
                    method.setText(dataSnapshot.child("method").getValue().toString());
                    price.setText(dataSnapshot.child("price").getValue().toString());
                    Picasso.get().load(Uri.parse(dataSnapshot.child("imageUrl").getValue().toString())).fit()
                            .centerCrop().into(imageBox);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"NothingToDisplay",Toast.LENGTH_SHORT).show();
            }
        });
        getIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(DisplayRecipeScrollFrontEndNew.this, DisplayIngredientsFrontEnd.class);
                myIntent.putExtra("EnteredRecipeName",recipeName);
                startActivity(myIntent);
            }
        });
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Adding to Cart");
                progressDialog.show();
                dbcart=FirebaseDatabase.getInstance().getReference();
                final Query query=dbcart.child("Recipes").child("Description");
                FirebaseAuth fauth;
                fauth = FirebaseAuth.getInstance();
                final String currentuser=fauth.getUid();
                //dbcart.child("Basket").child(currentuser);


                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            if(recipeName.equals(snapshot.getKey())) {
                                cart = new Cart();
                                System.out.println(snapshot);
                                Toast.makeText(DisplayRecipeScrollFrontEndNew.this, "Selected Item is added", Toast.LENGTH_LONG).show();
                                cart.setImageUri(snapshot.child("imageUrl").getValue().toString());
                                cart.setReceipeName(snapshot.child("recipeName").getValue().toString());
                                cart.setServings(snapshot.child("servings").getValue().toString() + " Servings");
                                cart.setPrice(Float.parseFloat(snapshot.child("price").getValue().toString()));

                                icart.add(cart);

                                db.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Toast.makeText(getApplicationContext(),"Item recorded",Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(getApplicationContext(),"Item recode fail",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                        dbcart.child("Basket").child(currentuser).child(String.valueOf(System.currentTimeMillis())).setValue(cart);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                Intent intentcart=new Intent(getApplicationContext(),mycart.class);
                System.out.println("blaa blaaaa");
                intentcart.putExtra("recipe",recipeName);
                //intentcart.putExtra("servings",  servings.getText().toString());
                //intentcart.putExtra("price", price.getText().toString());
                startActivity(intentcart);



            }
        });
    }

}