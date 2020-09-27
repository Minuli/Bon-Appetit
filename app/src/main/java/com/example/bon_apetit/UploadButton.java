package com.example.bon_apetit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bon_apetit.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public class UploadButton extends AppCompatActivity {
    Button showImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_button);
        showImages = findViewById(R.id.showImages);

        showImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(UploadButton.this, HomepageActivityNew.class);
                startActivity(myIntent);
            }
        });
    }
}