package com.example.bon_apetit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class employee_home_page extends AppCompatActivity {
    Button baddrecpie,bviewrecpie,baddstock,bviewstock,bvieworders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home_page);

        baddrecpie = findViewById(R.id.btnAddRecipe);
        bviewrecpie = findViewById(R.id.btnRecpie);
        baddstock = findViewById(R.id.btnViewRecpie);
        bviewstock = findViewById(R.id.btnViewStock);
        bvieworders = findViewById(R.id.btnViewOrders);

        baddstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddStock.class);
                startActivity(intent);
            }
        });
        bviewstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),stock_table.class);
                startActivity(intent);
            }
        });
        baddrecpie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddRecipesNew.class);
                startActivity(intent);
            }
        });
        bviewrecpie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ViewRecipeNew.class);
                startActivity(intent);
            }
        });
    }
}