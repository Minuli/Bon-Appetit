package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.Models.Customer;
import com.example.Models.CustomerPost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class addPost extends AppCompatActivity {

    private static final int IMAGE_REQUEST = 1;

     EditText recipieName,method,ingredients;
     Button choosephoto,submit;
     CustomerPost cuspost;
     ImageView image;
     Uri photoUri;
    String postid ;
    DatabaseReference dbRef;
    StorageReference stoRef;
     StorageTask upload;
     FirebaseAuth fauth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        recipieName = findViewById(R.id.recipename);
        method = findViewById(R.id.method);
        ingredients = findViewById(R.id.ingredie);
        choosephoto = findViewById(R.id.choosebutton);
        submit = findViewById(R.id.editbutton56);
        image = findViewById(R.id.imagerec);


        fauth = FirebaseAuth.getInstance();
        stoRef = FirebaseStorage.getInstance().getReference().child("CustomerPost").child(fauth.getCurrentUser().getUid());
        dbRef = FirebaseDatabase.getInstance().getReference().child("CustomerPost").child(fauth.getCurrentUser().getUid());




        postid = fauth.getCurrentUser().getUid();
         dbRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if(dataSnapshot.exists()){

                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });

        choosephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChoose();
            }


        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPost();

            }
        });

        method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
        private void openImageChoose(){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"pick Image"), IMAGE_REQUEST);
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                photoUri = data.getData();
                try {
                    Picasso.get().load(photoUri).fit().centerCrop().into(image);
                } catch (Exception ex) {

                    ex.printStackTrace();

                }
            }
        }
        //get extention of image
        private String getFileExtension(Uri uri){
            ContentResolver conRe =getContentResolver();
            MimeTypeMap mime =MimeTypeMap.getSingleton();
            return mime.getExtensionFromMimeType(conRe.getType(uri));
        }
        private void uploadPost(){
            if(photoUri !=null){
                StorageReference storeref = stoRef.child(System.currentTimeMillis() + "" + getFileExtension(photoUri));

                upload = storeref.putFile(photoUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override

                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(addPost.this,"Your post is successfully uploaded",Toast.LENGTH_SHORT).show();
                                    try{
                                        cuspost = new CustomerPost();
                                        cuspost.setRecipeName(recipieName.getText().toString().trim());
                                        cuspost.setMethod( method.getText().toString().trim());
                                        cuspost.setIngredients(ingredients.getText().toString().trim());
                                        if(taskSnapshot.getMetadata() != null){
                                            if(taskSnapshot.getMetadata().getReference() != null){
                                                Task<Uri> result =taskSnapshot.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String photoUri = uri.toString();
                                                        cuspost.setImgUrl(photoUri);
                                                        dbRef.child(recipieName.getText().toString()+ "" + System.currentTimeMillis()).setValue(cuspost);
                                                    }
                                                });
                                            }
                                        }

                                    }catch(Exception e){
                                        System.out.println(e.getMessage());
                                    }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                            Toast.makeText(addPost.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
            else {
                Toast.makeText(addPost.this,"No image selected",Toast.LENGTH_SHORT).show();
            }
        }
    }
