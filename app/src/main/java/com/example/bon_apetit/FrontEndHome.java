package com.example.bon_apetit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class FrontEndHome extends AppCompatActivity {

    ImageView ham;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_new);

        ham = findViewById(R.id.hamburger);
        ham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(FrontEndHome.this,nav.class);
                startActivity(intent);
            }
        });
    }
}