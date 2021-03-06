package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.Models.Ingredients;
import com.example.Models.Recipes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddIngredients extends AppCompatActivity {

    String recipeName;
    long ingredientId =0;
    EditText ingredient, amount;
    Spinner unit;
    Button addIngredients, done;
    DatabaseReference db;
    Ingredients ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);
        Intent myIntent = getIntent();
        recipeName = myIntent.getStringExtra("RecipeName");
        unit = (Spinner)findViewById(R.id.unit);
        ingredient = findViewById(R.id.tIngredient);
        amount = findViewById(R.id.tAmount);
        addIngredients = findViewById(R.id.addIngredients);
        done = findViewById(R.id.done);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddIngredients.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.unit));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unit.setAdapter(adapter);

        db= FirebaseDatabase.getInstance().getReference().child("Recipes").child("Ingredients").child(recipeName);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ingredientId=(dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(TextUtils.isEmpty(ingredient.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Please enter the name of ingredient",Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(amount.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Please enter the amount of ingredient needed",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ingredients = new Ingredients();
                        ingredients.setIngredient(ingredient.getText().toString().trim());
                        ingredients.setAmount(amount.getText().toString().trim());
                        ingredients.setUnit(unit.getSelectedItem().toString().trim());
                        db.child(System.currentTimeMillis()+ "Ingredient" + String.valueOf(ingredientId + 1)).setValue(ingredients);
                    }

                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
                ingredient.setText("");
                amount.setText("");
            }
        });

    }

}