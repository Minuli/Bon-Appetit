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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class mycart extends AppCompatActivity {

    String recipeName;
    Button checkout,cancel;
    RecyclerView recyclerView;
    Cart cart;
    private DatabaseReference db,dbref;
    float total = 0;

    CartAdapter cartAdapter;
    ArrayList<Cart> icart;
    TextView mCartTotalTextView;
    FirebaseAuth fauth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycart);

        recyclerView =findViewById(R.id.summaryRecycler);
        mCartTotalTextView = findViewById(R.id.TxtCartTotal);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        Intent intentcart=getIntent();
        recipeName=intentcart.getStringExtra("recipe");
        System.out.println("getname"+recipeName);

        final String user=FirebaseAuth.getInstance().getUid();
        db= FirebaseDatabase.getInstance().getReference();
        dbref=FirebaseDatabase.getInstance().getReference().child("Basket").child(user);
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
                dbref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(user)){
                            dbref=FirebaseDatabase.getInstance().getReference().child("Basket").child(user);
                            dbref.removeValue();
                        }
                        clearall();
                        mCartTotalTextView.setText("LKR 0.00 ");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Toast.makeText(getApplicationContext(),"No Items to display",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getFirebaseData(){
        fauth = FirebaseAuth.getInstance();
        final String currentuser=FirebaseAuth.getInstance().getUid();
       // final Query query=db.child("Basket").child(currentuser);
        db = FirebaseDatabase.getInstance().getReference().child("Basket").child(currentuser);
        db.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearall();
                System.out.println("Datas apshot ISSS ....>"+dataSnapshot);

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        cart = new Cart();
                        System.out.println("SNAPSHOT ISSS ....>"+snapshot);
                        Toast.makeText(mycart.this, "Selected Item is added", Toast.LENGTH_LONG).show();
                        cart.setImageUri(snapshot.child("imageUri").getValue().toString());
                        System.out.println("recpie name: "+snapshot.child("receipeName").getValue().toString());
                        cart.setReceipeName(snapshot.child("receipeName").getValue().toString());
                        cart.setServings(snapshot.child("servings").getValue().toString() + " Servings");
                        cart.setPrice(Float.parseFloat(snapshot.child("price").getValue().toString()));

                        total = calculateTotal(cart.getPrice());
                        cart.setTotal(total);

                        icart.add(cart);

                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Toast.makeText(getApplicationContext(),"Item recorded",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(),"Item recode fail",Toast.LENGTH_SHORT).show();
                            }
                        });
                    //db.child("total").setValue(total);
                }
                //db.child("Basket").child(String.valueOf(System.currentTimeMillis())).setValue(cart);
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

    public float calculateTotal(float price){
        total +=  price;
        return total;
    };


}