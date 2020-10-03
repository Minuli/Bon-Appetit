package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Models.CustomerPost;
import com.example.adapters.profileFeedAdapter;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class updatePost extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
        FirebaseUser user;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;

        StorageReference storef;
        StorageTask storagetask;



        CustomerPost post;
        EditText recipename,method,ingredients;
        Button edit,addbutton;
        ImageButton editimg;
        Uri recipieimg;
        ImageView imgview;




        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_update_post);

            //init  firebase

            firebaseAuth = FirebaseAuth.getInstance();
            user =FirebaseAuth.getInstance().getCurrentUser();
            final String userId = firebaseAuth.getCurrentUser().getUid();
            firebaseDatabase =FirebaseDatabase.getInstance();


            databaseReference= FirebaseDatabase.getInstance().getReference().child("CustomerPost").child(firebaseAuth.getCurrentUser().getUid());


        recipename = findViewById(R.id.updaterecipe);
        method = findViewById(R.id.updatemethod);
        ingredients = findViewById(R.id.updateingredient);
        edit =findViewById(R.id.editbutton);
        addbutton= findViewById(R.id.addimg);
        imgview =findViewById(R.id.imgview);
        storef = FirebaseStorage.getInstance().getReference();

            databaseReference= FirebaseDatabase.getInstance().getReference().child("CustomerPost").child(firebaseAuth.getCurrentUser().getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    recipename.setText(dataSnapshot.child("recipeName").getValue().toString());
                    method.setText(dataSnapshot.child("method").getValue().toString());
                    ingredients.setText(dataSnapshot.child("ingredients").getValue().toString());
                    imgview.setImageURI(Uri.parse(dataSnapshot.child("imgUrl").getValue().toString()));
                    try{
                        Picasso.get()
                                .load(post.getImgUrl())
                                .fit()
                                .centerCrop()
                                .placeholder(R.mipmap.ic_launcher)
                                .into(imgview);
                    }catch(Exception e){
                        Toast.makeText(getApplicationContext(),"image is unable to load",Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    edit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("CustomerPost").child(firebaseAuth.getCurrentUser().getUid()).child(recipename.getText().toString());
            post.setRecipeName(recipename.getText().toString().trim());
            post.setIngredients(ingredients.getText().toString().trim());
            post.setMethod(method.getText().toString().trim());
            databaseReference.child("method").setValue(post.getMethod());
            databaseReference.child("ingredients").setValue(post.getIngredients());

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),"Post is unable to updated",Toast.LENGTH_SHORT).show();
                }
            });





        }
    });









        }
}