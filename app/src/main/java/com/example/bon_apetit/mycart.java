package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Models.Cart;
import com.example.adapters.CartAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class mycart extends AppCompatActivity {

    Button checkout,cancel;
    RecyclerView recyclerView;
    Cart cart;
    private DatabaseReference db;
    CartAdapter cartAdapter;
    ArrayList<Cart> icart;
    TextView mCartTotalTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycart);

        recyclerView =findViewById(R.id.summaryRecycler);
        mCartTotalTextView = findViewById(R.id.TxtCartTotal);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        db= FirebaseDatabase.getInstance().getReference();

        icart = new ArrayList<Cart>();
        getFirebaseData();
        clearall();
        cartAdapter=new CartAdapter(getApplicationContext(),icart);
        recyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        checkout=findViewById(R.id.btncheckout);

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(getApplicationContext(),payment_one.class);
                //Toast.makeText(getApplicationContext(),"Please Enter Address",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        cancel=findViewById(R.id.btncancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icart.remove(cartAdapter);
                Toast.makeText(getApplicationContext(),"No Items to display",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getFirebaseData(){
        Query query=db.child("Recipes").child("Description");
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearall();
                float total = 0;
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    total=0;
                    cart =new Cart();
                    System.out.println(snapshot);
                    Toast.makeText(mycart.this,"Selected Item is added",Toast.LENGTH_LONG).show();
                    cart.setImageUri(snapshot.child("imageUrl").getValue().toString());
                    cart.setReceipeName(snapshot.child("recipeName").getValue().toString());
                    cart.setServings(snapshot.child("servings").getValue().toString()+" Servings");
                    cart.setPrice(Float.parseFloat(snapshot.child("price").getValue().toString()));

                    total += cart.getPrice();
                    icart.add(cart);


                }


                cartAdapter=new CartAdapter(getApplicationContext(),icart);
                recyclerView.setAdapter(cartAdapter);
                cartAdapter.notifyDataSetChanged();
                mCartTotalTextView.setText("LKR " + total);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }

    public void clearall(){
        if(icart!= null){
            icart.clear();
            if(cartAdapter!=null){
                cartAdapter.notifyDataSetChanged();
            }
        }
        icart=new ArrayList<>();

    }


}