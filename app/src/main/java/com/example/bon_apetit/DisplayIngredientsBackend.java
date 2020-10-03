package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Models.Ingredients;
import com.example.Models.Recipes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayIngredientsBackend extends AppCompatActivity {

    RecyclerView recyclerView;
    IngredientAdapter ingredientAdapter;
    DatabaseReference db;
    List<Ingredients> ingredient;
    TextView ingredients, amount;
    Spinner unit;
    String recipe;
    Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_ingredients_backend);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredients = findViewById(R.id.ingredient);
        amount = findViewById(R.id.amount);
        unit = findViewById(R.id.unit);
        delete = findViewById(R.id.delete);
        Intent myIntent = getIntent();
        recipe = myIntent.getStringExtra("EnteredRecipeName");

        ingredient = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference().child("Recipes").child("Ingredients").child(recipe);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot);
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    System.out.println(snapshot);
                    Ingredients ingredients1 =  snapshot.getValue(Ingredients.class);
                    ingredient.add(ingredients1);
                }
                ingredientAdapter = new IngredientAdapter(DisplayIngredientsBackend.this,ingredient);
                recyclerView.setAdapter(ingredientAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DisplayIngredientsBackend.this, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}