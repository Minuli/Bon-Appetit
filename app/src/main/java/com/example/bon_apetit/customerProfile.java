package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Models.Customer;
import com.example.Models.CustomerPost;
import com.example.adapters.FeedAdapter;
import com.example.adapters.profileFeedAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;


public class customerProfile extends AppCompatActivity implements profileFeedAdapter.OnItemClickListener {


    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseStorage fstorage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference1;
    DatabaseReference databaseReference2;
    ValueEventListener valuelistner;

    //recycle view
    RecyclerView recyclerView;
    profileFeedAdapter profilefeedadapter;
    ArrayList<CustomerPost> cuspost;

    // view in xml
    ImageView proimg;
    TextView cusName;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cutomer_profile);

        //init  firebase

        firebaseAuth = FirebaseAuth.getInstance();
        user =FirebaseAuth.getInstance().getCurrentUser();
        String userId=user.getUid();
        firebaseDatabase =FirebaseDatabase.getInstance();
        fstorage=FirebaseStorage.getInstance();

        databaseReference1 =firebaseDatabase.getReference("Customer");
        databaseReference2= FirebaseDatabase.getInstance().getReference().child("CustomerPost").child(firebaseAuth.getCurrentUser().getUid());

        cuspost =new ArrayList<>();

        cusName =findViewById(R.id.perName);
        proimg =findViewById(R.id.imageprofile1);


        //recycle view
        recyclerView =findViewById(R.id.profileFeed);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Query query1 =databaseReference1.child(user.getUid());
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String name =""+ dataSnapshot.child("name").getValue();
                String img =""+ dataSnapshot.child("profilePic").child("imageuri").getValue();

                //set data
                cusName.setText(name);
                try{
                    Picasso.get().load(img).into(proimg);
                }
                catch (Exception e){

                    Picasso.get().load(img).into(proimg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        valuelistner=databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    CustomerPost cPost= dataSnapshot1.getValue(CustomerPost.class);
                    Toast.makeText(customerProfile.this,cPost.getImgUrl(),Toast.LENGTH_LONG).show();
                    cPost.setKey(dataSnapshot1.getKey());
                    cuspost.add(cPost);
                }
                profilefeedadapter =new profileFeedAdapter(customerProfile.this,cuspost);
                recyclerView.setAdapter(profilefeedadapter);
                profilefeedadapter.setOnItemClickListner(customerProfile.this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onWhatEverClick(int position) {

    }
    //delete post
    @Override
    public void onDeleteClick(int position) {
        CustomerPost selectedItem = cuspost.get(position);
        final String selectedkey= selectedItem.getKey();

        StorageReference stoimgref =fstorage.getReferenceFromUrl(selectedItem.getImgUrl());
        stoimgref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference2.child(selectedkey).removeValue();
                Toast.makeText(customerProfile.this, "Post is deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference2.removeEventListener(valuelistner);
    }
}





