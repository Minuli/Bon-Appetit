package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.Models.Recipes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HomepageActivityNew extends AppCompatActivity {

    RecyclerView recyclerView;
    RecipeImageAdapter recipeImageAdapter;
    DatabaseReference db;
    List<Recipes> recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_new);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recipe = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference("Recipes/Description");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Recipes recipes =  dataSnapshot1.getValue(Recipes.class);
                    Toast.makeText(HomepageActivityNew.this, recipes.getImageUrl(), Toast.LENGTH_LONG).show();
                    recipe.add(recipes);
                }
                recipeImageAdapter = new RecipeImageAdapter(HomepageActivityNew.this,recipe);
                recyclerView.setAdapter(recipeImageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomepageActivityNew.this, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}