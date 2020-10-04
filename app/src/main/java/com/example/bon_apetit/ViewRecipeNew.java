package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class ViewRecipeNew extends AppCompatActivity {
    EditText enteredRecipeName;
    Button submit;
    DatabaseReference db;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe_new);

        enteredRecipeName = findViewById(R.id.submitName);
        submit = findViewById(R.id.submit);


        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                flag=0;
                final String rname = enteredRecipeName.getText().toString().trim();
                db = FirebaseDatabase.getInstance().getReference().child("Recipes").child("Description");

                if(TextUtils.isEmpty(enteredRecipeName.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Please enter the name of recipe",Toast.LENGTH_SHORT).show();
                }
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            if(rname.equals(snapshot.getKey())){
                                flag = 1;
                            }
                        }
                        if(flag == 0){
                            Toast.makeText(getApplicationContext(),"No such recipe available",Toast.LENGTH_SHORT).show();
                            enteredRecipeName.setText(" ");
                        }
                        else if (flag == 1){
                            Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(ViewRecipeNew.this, DisplayRecipeScroll.class);
                            myIntent.putExtra("EnteredRecipeName",rname);
                            System.out.println("Send name : "+rname);
                            startActivity(myIntent);
                            enteredRecipeName.setText(" ");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}