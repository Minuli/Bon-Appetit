package com.example.bon_apetit;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.Models.Recipes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class DisplayRecipeScroll extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    String recipeName, img;
    DatabaseReference db, db1, db2;
    StorageReference storage, storage1, storage2;
    EditText servings, recipe, method, price;
    ImageView imageBox;
    Button getIngredient, delete, uploadImage, updateRecipe;
    Uri imageUri;
    Recipes recipe1;
    ProgressDialog progressDialog;
    StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe_scroll);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        recipe = findViewById(R.id.recipe);
        servings = findViewById(R.id.txtServings);
        method = findViewById(R.id.methodText3);
        price = findViewById(R.id.txtPrice);
        imageBox = findViewById(R.id.imageBox);
        getIngredient = findViewById(R.id.getIngredient);
        delete = findViewById(R.id.deleteRecipe);
        uploadImage = findViewById(R.id.uploadImage);
        updateRecipe = findViewById(R.id.updateRecipe);
        progressDialog = new ProgressDialog(DisplayRecipeScroll.this);

        Intent myIntent = getIntent();
        recipeName = myIntent.getStringExtra("EnteredRecipeName");
        System.out.println("getname :"+recipeName);

        db = FirebaseDatabase.getInstance().getReference().child("Recipes").child("Description").child(recipeName);
        db1 = FirebaseDatabase.getInstance().getReference("Recipes").child("Ingredients");
        storage2 = FirebaseStorage.getInstance().getReference().child("Recipes").child("Description");
        db2 = FirebaseDatabase.getInstance().getReference().child("Recipes").child("Description");
        storage = FirebaseStorage.getInstance().getReference();
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    recipe.setText(dataSnapshot.child("recipeName").getValue().toString());
                    servings.setText(dataSnapshot.child("servings").getValue().toString());
                    method.setText(dataSnapshot.child("method").getValue().toString());
                    price.setText(dataSnapshot.child("price").getValue().toString());
                    Picasso.get().load(Uri.parse(dataSnapshot.child("imageUrl").getValue().toString())).fit()
                            .centerCrop().into(imageBox);
                    img = dataSnapshot.child("imageUrl").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"NothingToDisplay",Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getApplicationContext(),  AddIngredients.class);
                finish();
                startActivity(intent1);
            }
        });

        getIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(DisplayRecipeScroll.this, DisplayIngredientsBackend.class);
                myIntent.putExtra("EnteredRecipeName",recipeName);
                startActivity(myIntent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(recipeName)){
                            db2 = FirebaseDatabase.getInstance().getReference("Recipes").child("Description").child(recipeName);
                            db2.removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                db1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(recipeName)){
                            db1 = FirebaseDatabase.getInstance().getReference("Recipes").child("Ingredients").child(recipeName);
                            db1.removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                storage1 = FirebaseStorage.getInstance().getReferenceFromUrl(img);
                storage1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        Toast.makeText(DisplayRecipeScroll.this,"Delete Successful",Toast.LENGTH_LONG).show();
                        Intent intent1 = new Intent(getApplicationContext(),  ViewRecipeNew.class);
                        finish();
                        startActivity(intent1);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DisplayRecipeScroll.this,"Delete Unsuccessful",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent();
                myIntent.setType("image/*");
                myIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(myIntent,"Pick Image" ), PICK_IMAGE_REQUEST);
            }
        });

        updateRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uploadTask != null && uploadTask.isInProgress()){
                    Toast.makeText(DisplayRecipeScroll.this,"upload unsuccessful",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(recipe.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Please enter the name of recipe",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(servings.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Please enter the serving amount of recipe",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(method.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Method should be provided",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(price.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Amount should be provided",Toast.LENGTH_SHORT).show();
                }
                else
                    updateDetails();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData()!= null){
            imageUri = data.getData();
            try{
                Picasso.get().load(imageUri).fit().centerCrop().into(imageBox);
            }catch(Exception ex){
                ex.printStackTrace();
            }

        }
    }

    private String obtainFileExtension(Uri uri){
        //get file extension from image
        ContentResolver contResolver = getContentResolver();
        MimeTypeMap mimeType = MimeTypeMap.getSingleton();
        return mimeType.getExtensionFromMimeType(contResolver.getType(uri));
    }

    private void updateDetails(){
            progressDialog.setTitle("Uploading Details");
            progressDialog.show();

            recipe1 = new Recipes();
            recipe1.setRecipeName(recipe.getText().toString().trim());
            recipe1.setMethod(method.getText().toString().trim());
            recipe1.setServings(servings.getText().toString().trim());
            recipe1.setPrice(price.getText().toString().trim());
            db2.child("recipeName").setValue(recipe1.getRecipeName());
            db2.child("method").setValue(recipe1.getMethod());
            db2.child("servings").setValue(recipe1.getServings());
            db2.child("price").setValue(recipe1.getPrice());

    }


}