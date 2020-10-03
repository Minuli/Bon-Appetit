package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.Models.Customer;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

public class custregaddprofilepic extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST =100;
    ImageView imageView;
    Button btnadd,btnupload;
    Uri propicuir;
    DatabaseReference db;
    StorageReference storage;
    StorageTask uploadTask;
    Customer customer;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");

        setContentView(R.layout.activity_custregaddprofilepic);

        imageView = findViewById(R.id.imgpropic);
        btnadd = findViewById(R.id.btnselectpic);
        btnupload = findViewById(R.id.btnuploadpic);
        progressDialog = new ProgressDialog(custregaddprofilepic.this);

        storage = FirebaseStorage.getInstance().getReference().child("Customer").child(uid);
        db = FirebaseDatabase.getInstance().getReference().child("Customer").child(uid).child("profilePic");




        btnadd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openFileChoosed();
            }
        });
    }
    private void openFileChoosed(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    private String getpropic(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType((propicuir)));

    }
    private void uploadpic(){
        if(propicuir != null){
            progressDialog.setTitle("Uploading image");
            progressDialog.show();
            StorageReference storageReference  = storage.child(getpropic(propicuir));
            uploadTask = storageReference.putFile(propicuir).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(),"Photo uploaded successfully",Toast.LENGTH_SHORT).show();
                    try{
                        customer = new Customer();
                        if(taskSnapshot.getMetadata() != null){
                            if(taskSnapshot.getMetadata().getReference() !=null){
                                Task<Uri> resultn= taskSnapshot.getStorage().getDownloadUrl();
                                resultn.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUri = uri.toString();
                                        customer.setImageuri(imageUri);
                                        db.setValue(customer);
                                    }
                                });

                            }
                        }
                        Intent intent = new Intent(getApplicationContext(),UserLoginPage.class);
                        startActivity(intent);
                        finish();
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            Toast.makeText(getApplicationContext(),"failed to upload image please try again",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData()!=null){
            System.out.println("data file storage : "+data);
            propicuir = data.getData();
            Picasso.get().load(propicuir).into(imageView);
        }
    }

}