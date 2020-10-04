package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DisplayRecipeScrollFrontEndNew extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    String recipeName;
    DatabaseReference db;
    StorageReference storage;
    TextView servings, price;
    EditText method;
    ImageView imageBox;
    TextView recipe;
    Button getIngredients;

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

        Intent myIntent = getIntent();
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
    }

}