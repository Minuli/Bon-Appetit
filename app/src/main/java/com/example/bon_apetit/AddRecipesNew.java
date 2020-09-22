package com.example.bon_apetit;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Models.Recipes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;



public class AddRecipesNew extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    EditText recipeName,method, category;
    Button addIngredients, home, chooseFile;
    Recipes recipe;
    ImageView imageBox;
    Uri imageUri;
    DatabaseReference db;
    StorageReference storage;
    StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipes_new);

        recipeName = findViewById(R.id.recipe);
        method = findViewById(R.id.methodEnter);
        category = findViewById(R.id.txtCategory);
        addIngredients = findViewById(R.id.addIngredients);
        home = findViewById(R.id.home3);
        chooseFile = findViewById(R.id.chooseFile);
        imageBox = findViewById(R.id.imageBox);

        storage = FirebaseStorage.getInstance().getReference();
        db = FirebaseDatabase.getInstance().getReference().child("Recipes").child("Description");

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
                else if(TextUtils.isEmpty(recipeName.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Please enter the name of recipe",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(category.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Please enter the category of recipe",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(method.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Method should be provided",Toast.LENGTH_SHORT).show();
                }
                else
                    uploadDetails();
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
                        recipe = new Recipes();
                        recipe.setMethod(method.getText().toString().trim());
                        recipe.setImageUrl(storage.getDownloadUrl().toString());
                        recipe.setCategory(category.getText().toString().trim());
                        db.child(recipeName.getText().toString()).setValue(recipe);
                        Intent myIntent = new Intent(AddRecipesNew.this, AddIngredients.class);
                        myIntent.putExtra("RecipeName",recipeName.getText().toString());
                        startActivity(myIntent);
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
