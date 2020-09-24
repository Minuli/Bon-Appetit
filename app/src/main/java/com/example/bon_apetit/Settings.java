package com.example.bon_apetit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    TextView cart,home,purHistory,socailMedia,profile,contactUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        cart = findViewById(R.id.cart1);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Settings.this,mycart.class);
                startActivity(intent);
            }
        });

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Settings.this,FrontEndHome.class);
                startActivity(intent);
            }
        });
        purHistory = findViewById(R.id.history1);
        purHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Settings.this,history.class);
                startActivity(intent);
            }
        });
        socailMedia = findViewById(R.id.feed);
        socailMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Settings.this,Feed.class);
                startActivity(intent);
            }
        });
        profile= findViewById(R.id.profile1);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Settings.this,customerProfile.class);
                startActivity(intent);
            }
        });



        contactUs= findViewById(R.id.contactus);
        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Settings.this,ContactUsPage.class);
                startActivity(intent);
            }
        });
        }
    }
