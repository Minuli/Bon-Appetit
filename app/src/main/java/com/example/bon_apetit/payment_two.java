package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Models.Cart;
import com.example.adapters.SummaryCartAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class payment_two extends AppCompatActivity {

    TextView promocode, totalamount, total_botm, txttotal;
    RecyclerView cartCycler;
    Button pay, cancel;
    Cart cart = new Cart();
    ArrayList<Cart> icart;
    SummaryCartAdapter summaryCartAdapter;
    DatabaseReference db,dbref;
    FirebaseAuth fauth ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_two);

        promocode = findViewById(R.id.promocode);
        totalamount= findViewById(R.id.total);
        total_botm = findViewById(R.id.txttotal);
        txttotal = findViewById(R.id.price);
        cartCycler=findViewById(R.id.cartRecycler);
        pay=findViewById(R.id.pay);
        cancel=findViewById(R.id.cancel);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(payment_two.this);
        cartCycler.setLayoutManager(linearLayoutManager);
        cartCycler.setHasFixedSize(true);

        getFirebaseData();
        clearall();
        summaryCartAdapter=new SummaryCartAdapter(getApplicationContext(),icart);
        cartCycler.setAdapter(summaryCartAdapter);
        summaryCartAdapter.notifyDataSetChanged();

        db = FirebaseDatabase.getInstance().getReference().child("Cart");
        dbref=FirebaseDatabase.getInstance().getReference();

        icart = new ArrayList<Cart>();

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Payment Successfully Recorded",Toast.LENGTH_LONG).show();
                dbref.child("Active Order").child(String.valueOf(System.currentTimeMillis())).setValue(cart);
                Intent intenthome=new Intent(getApplicationContext(),HomepageActivityNew.class);
                startActivity(intenthome);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearall();
            }
        });


    }

    public void getFirebaseData() {
        fauth = FirebaseAuth.getInstance();
        String currentuser=fauth.getUid();
        db = FirebaseDatabase.getInstance().getReference().child("Basket").child(currentuser);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearall();
                float total=0;
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    System.out.println(snapshot);
                    Toast.makeText(payment_two.this,"Selected Items are ready !!",Toast.LENGTH_LONG).show();
                    cart.setReceipeName((String) snapshot.child("receipeName").getValue());

                    cart.setPrice(Float.parseFloat(snapshot.child("price").getValue().toString()));
                    //cart.setTotal(Float.parseFloat(snapshot.child("total").getValue().toString()));

                    total+=total+cart.getPrice();
                    icart.add(cart);
                }
                summaryCartAdapter=new SummaryCartAdapter(getApplicationContext(),icart);
                cartCycler.setAdapter(summaryCartAdapter);
                summaryCartAdapter.notifyDataSetChanged();
                totalamount.setText("LKR"+total);
                txttotal.setText("LKR"+total);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void clearall(){
        if(icart!=null){
            icart.clear();
            if(summaryCartAdapter!=null){
                summaryCartAdapter.notifyDataSetChanged();
            }
        }

        icart= new ArrayList<>();
    }
}