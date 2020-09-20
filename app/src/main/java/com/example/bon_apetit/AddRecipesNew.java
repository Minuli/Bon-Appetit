package com.example.bon_apetit;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;



public class AddRecipesNew extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    EditText recipeName,method;
    Button addIngredients, home, chooseFile;
    Recipes recipe;
    ImageView imageBox;
    Uri imageUri;
    long id = 0;
    DatabaseReference db;
    StorageReference storage;
    StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipes_new);

        recipeName = findViewById(R.id.recipe);
        method = findViewById(R.id.methodEnter);
        addIngredients = findViewById(R.id.addIngredients);
        home = findViewById(R.id.home3);
        chooseFile = findViewById(R.id.chooseFile);
        imageBox = findViewById(R.id.imageBox);

        storage = FirebaseStorage.getInstance().getReference();
        db = FirebaseDatabase.getInstance().getReference().child("Recipes");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    id=(dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chooseFile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openFileChoosed();
            }
        });

        addIngredients.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(uploadTask != null && uploadTask.isInProgress()){
                    Toast.makeText(AddRecipesNew.this,"upload unsuccessful",Toast.LENGTH_SHORT).show();
                }
                else
                    uploadDetails();
                Intent myIntent = new Intent(AddRecipesNew.this, AddIngredients.class);
                startActivity(myIntent);
            }
        });

        recipeName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        method.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });
    }
    private void openFileChoosed(){
        Intent myIntent = new Intent();
        myIntent.setType("image/*");
        myIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(myIntent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData()!= null){
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(imageBox);
        }
    }

    private String obtainFileExtension(Uri uri){
        //get file extension from image
        ContentResolver contResolver = getContentResolver();
        MimeTypeMap mimeType = MimeTypeMap.getSingleton();
        return mimeType.getExtensionFromMimeType(contResolver.getType(uri));
    }

    private void uploadDetails(){
        if(imageUri != null){
            StorageReference stReference = storage.child(System.currentTimeMillis()+ "" + obtainFileExtension(imageUri));

            uploadTask = stReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddRecipesNew.this,"successfully uploaded",Toast.LENGTH_SHORT).show();
                    try {
                        recipe = new Recipes(method.getText().toString().trim(),
                                storage.getDownloadUrl().toString(),
                                recipeName.getText().toString().trim());
                        //String uploadCheck = db.push().getKey();
                        //db.child(uploadCheck).setValue(recipe);
                        db.child(String.valueOf(id+1)).setValue(recipe);
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddRecipesNew.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Toast.makeText(AddRecipesNew.this,"Details are not completely filled",Toast.LENGTH_SHORT).show();
        }
    }
}