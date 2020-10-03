package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Models.Cart;
import com.example.adapters.SummaryCartAdapter;
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
    DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_two);

        promocode = findViewById(R.id.promocode);
        totalamount= findViewById(R.id.total);
        total_botm = findViewById(R.id.txttotal);
        txttotal = findViewById(R.id.price);
        cartCycler=findViewById(R.id.cartRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(payment_two.this);
        cartCycler.setLayoutManager(linearLayoutManager);
        cartCycler.setHasFixedSize(true);

        getFirebaseData();
        clearall();
        summaryCartAdapter=new SummaryCartAdapter(getApplicationContext(),icart);
        cartCycler.setAdapter(summaryCartAdapter);
        summaryCartAdapter.notifyDataSetChanged();

        db = FirebaseDatabase.getInstance().getReference().child("Recipes");

        icart = new ArrayList<Cart>();


    }


    public void getFirebaseData() {
        db = FirebaseDatabase.getInstance().getReference().child("Recipes").child("Description");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearall();
                float total = 0;
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    System.out.println(snapshot);
                    Toast.makeText(payment_two.this,"Selected Items are ready !!",Toast.LENGTH_LONG).show();
                    cart.setReceipeName(snapshot.child("recipeName").getValue().toString());
                    cart.setPrice(Float.parseFloat(snapshot.child("price").getValue().toString()));

                    total += cart.getPrice();
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