package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Models.Recipes;
import com.example.adapters.RecipeImageAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomepageActivityNew extends AppCompatActivity {

    RecyclerView recyclerView;
    RecipeImageAdapter recipeImageAdapter;
    DatabaseReference db;
    List<Recipes> recipe;
    ImageView image;
    TextView recipeName;
    Button hamBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_new);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        image = findViewById(R.id.imageUpload);
        recipeName = findViewById(R.id.recipeName);
        hamBtn=findViewById(R.id.ham_setting);
        recipe = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference("Recipes/Description");

        recipeImageAdapter = new RecipeImageAdapter(HomepageActivityNew.this,recipe);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Recipes recipes =  dataSnapshot1.getValue(Recipes.class);
                    recipe.add(recipes);
                }
                recipeImageAdapter = new RecipeImageAdapter(HomepageActivityNew.this,recipe);
                recyclerView.setAdapter(recipeImageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        hamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSet=new Intent(getApplicationContext(),Settings.class);
                startActivity(intentSet);
            }
        });


    }
}