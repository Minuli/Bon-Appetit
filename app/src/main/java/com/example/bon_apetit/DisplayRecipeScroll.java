package com.example.bon_apetit;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.Models.Recipes;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayRecipeScroll extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    String recipeName;
    DatabaseReference db;
    StorageReference storage;
    EditText category, recipe, method;
    ImageView imageBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe_scroll);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        recipe = findViewById(R.id.recipe);
        category = findViewById(R.id.txtCategory);
        method = findViewById(R.id.methodText3);
        imageBox = findViewById(R.id.imageBox);

        Intent myIntent = getIntent();
        recipeName = myIntent.getStringExtra("EnteredRecipeName");

        db = FirebaseDatabase.getInstance().getReference().child("Recipes").child("Description").child(recipeName);
        storage = FirebaseStorage.getInstance().getReference();
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    recipe.setText(dataSnapshot.child("recipeName").getValue().toString());
                    category.setText(dataSnapshot.child("category").getValue().toString());
                    method.setText(dataSnapshot.child("method").getValue().toString());
                    Picasso.get().load(Uri.parse(dataSnapshot.child("imageUrl").getValue().toString())).fit()
                            .centerCrop().into(imageBox);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"NothingToDisplay",Toast.LENGTH_SHORT).show();
            }
        });
    }
}