package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.Models.Stock;
import com.example.adapters.DisplayStockAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class stock_table extends AppCompatActivity {
    RecyclerView recyclerView;
    Stock stock;
    Button baddstock,bupdate;
    EditText updatename;

    //firebase
    private DatabaseReference dbref;

    //variables
    private DisplayStockAdapter radapter;
    ArrayList<Stock> istock = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_table);

        recyclerView = findViewById(R.id.stockrecycvleview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //firebase
        dbref = FirebaseDatabase.getInstance().getReference();

        //arrayList
        istock = new ArrayList<>();

        //clear data
        clearall();

        //getdata method
        GetDataFromFirebase();

        baddstock = findViewById(R.id.btnaddstock);
        bupdate = findViewById(R.id.btnupdate_stock);
        updatename = findViewById(R.id.txtupdatename);
        radapter= new DisplayStockAdapter(getApplicationContext(),istock);
        recyclerView.setAdapter(radapter);


        baddstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddStock.class);
                startActivity(intent);
            }
        });

        bupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(updatename.getText().toString())){
                    updatename.setError("This feild cannot be empty");
                }
                else{
                    Intent intent = new Intent(getApplicationContext(),update_stock.class);
                    intent.putExtra("stockName",updatename.getText().toString().trim());
                    startActivity(intent);
                }

            }
        });

    }
    private void GetDataFromFirebase(){
        Query query =dbref.child("stock");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearall();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    System.out.println(snapshot);
                    stock = new Stock();
                    stock.setName(snapshot.child("name").getValue().toString());
                    stock.setQty(Integer.parseInt(snapshot.child("qty").getValue().toString()));
                    stock.setUnit(snapshot.child("unit").getValue().toString());
                    stock.setUnitprice(Float.valueOf(snapshot.child("unitprice").getValue().toString()));

                    istock.add(stock);
                }
                radapter= new DisplayStockAdapter(getApplicationContext(),istock);
                recyclerView.setAdapter(radapter);
                radapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void clearall(){
        if(istock != null){
            istock.clear();

            if(radapter != null){
                radapter.notifyDataSetChanged();
            }
        }
        istock = new ArrayList<>();
    }
}