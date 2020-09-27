package com.example.bon_apetit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class ViewRecipeNew extends AppCompatActivity {
    EditText enteredRecipeName;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe_new);

        enteredRecipeName = findViewById(R.id.submitName);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(TextUtils.isEmpty(enteredRecipeName.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Please enter the name of recipe",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent myIntent = new Intent(ViewRecipeNew.this, DisplayRecipeScroll.class);
                    myIntent.putExtra("EnteredRecipeName",enteredRecipeName.getText().toString());
                    startActivity(myIntent);
                }

            }
        });

    }
}