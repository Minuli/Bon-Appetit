package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
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

public class Feed extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference1;
    DatabaseReference databaseReference2;

    //recycle view
    RecyclerView recyclerView;
    FeedAdapter feedadapter;
    ArrayList<CustomerPost> customerpost;
   // ArrayList<Customer> cusdetails;

    // view in xml
    ImageView proimg;
    TextView cusName;
    Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

            //init  firebase

            firebaseAuth = FirebaseAuth.getInstance();
            user = FirebaseAuth.getInstance().getCurrentUser();
            String userId = user.getUid();
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference1 = firebaseDatabase.getReference("Customer").child(firebaseAuth.getCurrentUser().getUid());
            databaseReference2 = FirebaseDatabase.getInstance().getReference().child("CustomerPost").child(firebaseAuth.getCurrentUser().getUid());

            customerpost = new ArrayList<>();
           // cusdetails =new ArrayList<>();

            //int views
            cusName = findViewById(R.id.cusName);
            proimg = findViewById(R.id.image);
            //recycle view
            recyclerView = findViewById(R.id.feedRcle);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


            databaseReference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        CustomerPost cPost = dataSnapshot1.getValue(CustomerPost.class);
                        Toast.makeText(Feed.this, cPost.getImgUrl(), Toast.LENGTH_LONG).show();
                        Toast.makeText(Feed.this, cPost.getProfileimg(), Toast.LENGTH_LONG).show();

                        customerpost.add(cPost);
                    }
                    feedadapter = new FeedAdapter(Feed.this,customerpost);
                    recyclerView.setAdapter(feedadapter);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });








        }

    }
