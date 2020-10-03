package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.Models.Customer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class EditProfile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST =1;
    EditText uname,uemail,unumber,uaddress;
    Customer customer;

    ImageView propic;

    Button bedit,bresetpwd;
    ImageButton baddpropic;

    ProgressDialog progressDialog;

    Uri propicuir;
    DatabaseReference dbreff;
    StorageReference storage ;
    StorageTask uploadTask;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        dbreff = FirebaseDatabase.getInstance().getReference().child("customer");
        firebaseAuth = FirebaseAuth.getInstance();
        final String uid = firebaseAuth.getCurrentUser().getUid();

        uname = findViewById(R.id.txtusername);
        uemail = findViewById(R.id.txtemail);
        unumber = findViewById(R.id.txtcontactnumber);
        uaddress = findViewById(R.id.txtaddress);

        propic= findViewById(R.id.ivpropic);
        storage = FirebaseStorage.getInstance().getReference();

        bedit = findViewById(R.id.btneditacc);
        bresetpwd = findViewById(R.id.btnresetpwd);
        baddpropic = findViewById(R.id.imageadd);

        progressDialog = new ProgressDialog(EditProfile.this);
        customer = new Customer();


        if(uid.length()>0){
            dbreff = FirebaseDatabase.getInstance().getReference().child("Customer").child(uid);
            dbreff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        System.out.println(dataSnapshot);
                        uname.setText(dataSnapshot.child("name").getValue().toString());
                        uemail.setText(dataSnapshot.child("email").getValue().toString());
                        customer.setNic(dataSnapshot.child("nic").getValue().toString());
                        unumber.setText(dataSnapshot.child("number").getValue().toString());
                        uaddress.setText(dataSnapshot.child("address").getValue().toString());
                        customer.setImageuri(dataSnapshot.child("profilePic").child("imageuri").getValue().toString());
                        try {
                            System.out.println(customer.getImageuri());
                            Picasso.get()
                                .load(customer.getImageuri())
                                .fit()
                                .centerCrop()
                                .placeholder(R.mipmap.ic_launcher_round)
                                .into(propic);


                        //Glide.with(getApplicationContext()).load(img).into(propic);
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),"Unable to load profile picture",Toast.LENGTH_SHORT).show();
                            System.out.println(e.getMessage());
                        }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG);
            Intent intent = new Intent(getApplicationContext(),UserLoginPage.class);
            finish();
            startActivity(intent);
        }

        baddpropic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChoose();
            }
        });
        bedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbreff = FirebaseDatabase.getInstance().getReference().child("Customer").child(uid);
                if(TextUtils.isEmpty(uname.getText().toString())){
                    uname.setError("Name feild Cannot be left empty");
                }
                else if (TextUtils.isEmpty(unumber.getText().toString())){
                    unumber.setError("Contact number feild cannot be left empty");
                }
                else if(TextUtils.isEmpty(uaddress.getText().toString())){
                    uaddress.setError("delivary address felid cannot be left empty");
                }
                else {
                    uploadPic();
                    customer.setName(uname.getText().toString().trim());
                    customer.setEmail(uemail.getText().toString().trim());
                    customer.setNumber(unumber.getText().toString().trim());
                    customer.setAddress(uaddress.getText().toString().trim());
                    dbreff.child("name").setValue(customer.getName());
                    dbreff.child("number").setValue(customer.getNumber());
                    dbreff.child("address").setValue(customer.getAddress());
                    dbreff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(),"Unable to update account details please try again.",Toast.LENGTH_SHORT).show();
                        }
                    });
                    Toast.makeText(getApplicationContext(),"Account details updated successfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),HomepageActivityNew.class);
                    finish();
                    startActivity(intent);


                }
            }
        });

    }
    private  void openFileChoose(){
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
    private void uploadPic(){
        if(propic != null){
            progressDialog.setTitle("Uploading image");
            progressDialog.show();
            StorageReference storageReference= storage.child("Customer").child(firebaseAuth.getCurrentUser().getUid()).child(getpropic(propicuir));
            uploadTask = storageReference.putFile(propicuir).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(),"Profile picture updated Scuessfully",Toast.LENGTH_SHORT).show();
                    try{
                        if(taskSnapshot.getMetadata() != null){
                            if(taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageuri = uri.toString();
                                        customer.setImageuri(imageuri);
                                        dbreff.child("profilePic").child("imageuri").setValue(customer.getImageuri());
                                    }
                                });
                            }
                        }
                        Intent intent = new Intent(getApplicationContext(),HomepageActivityNew.class);
                        startActivity(intent);
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
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData()!=null){
            propicuir = data.getData();
            Picasso.get().load(propicuir).into(propic);
        }
    }
}