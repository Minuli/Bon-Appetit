package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;

public class nav extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()){

                            case R.id.nav_home:
                                Intent home = new Intent(nav.this,HomepageActivityNew.class);

                                startActivity(home);
                                break;
                            case R.id.nav_profile:
                                Intent profile = new Intent(nav.this,customerProfile.class);

                                startActivity(profile);
                                break;
                            case R.id.nav_add:
                                Intent add = new Intent(nav.this,addPost.class);

                                startActivity(add);

                                break;
                            case R.id.nav_feed:
                                Intent feed = new Intent(nav.this,Feed.class);

                                startActivity(feed);
                                break;
                            case R.id.nav_notification:
                                Intent heart = new Intent(nav.this,Settings.class);
                                startActivity(heart);
                                break;

                        }



                        return true;

                    }
                });
    }
}