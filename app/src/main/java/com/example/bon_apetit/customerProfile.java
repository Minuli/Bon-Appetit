package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Models.Customer;
import com.example.Models.CustomerPost;
import com.example.adapters.FeedAdapter;
import com.example.adapters.profileFeedAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;


public class customerProfile extends AppCompatActivity {


    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference1;
    DatabaseReference databaseReference2;

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
        databaseReference1 =firebaseDatabase.getReference("Customer");
        databaseReference2= FirebaseDatabase.getInstance().getReference().child("CustomerPost").child(firebaseAuth.getCurrentUser().getUid());

        cuspost =new ArrayList<>();


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

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    CustomerPost cPost= dataSnapshot1.getValue(CustomerPost.class);
                    Toast.makeText(customerProfile.this,cPost.getImgUrl(),Toast.LENGTH_LONG).show();
                    cuspost.add(cPost);
                }
                profilefeedadapter =new profileFeedAdapter(customerProfile.this,cuspost);
                recyclerView.setAdapter(profilefeedadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}