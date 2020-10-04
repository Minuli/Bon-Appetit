package com.example.bon_apetit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Models.Stock;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.StandardProtocolFamily;

public class AddStock extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    Spinner sunit;
    EditText iname,iqty,irol,iup;
    Button btnAddStock,btnReset;
    DatabaseReference db;
    String unit;
    Stock stock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);



        //linking Edit texts and buttons
        sunit =findViewById(R.id.asunit);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddStock.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.stocks_units)     );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sunit.setAdapter(adapter);

        iname = findViewById(R.id.txtiname);
        iqty = findViewById(R.id.txtiqty);
        irol = findViewById(R.id.txtirol);
        iup = findViewById(R.id.txtiup);

        //linking buttons
        btnAddStock = findViewById(R.id.btnaddstock);
        btnReset = findViewById(R.id.btnreset);

        //onlick even for add stock button
        btnAddStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stock = new Stock();
                db = FirebaseDatabase.getInstance().getReference().child("stock");
                int qty = Integer.parseInt(iqty.getText().toString());
                int rol= Integer.parseInt(irol.getText().toString()) ;
                try{
                    if(TextUtils.isEmpty(iname.getText().toString())){
                        iname.setError("Please Enter Ingredient name");
                    }
                    else if(TextUtils.isEmpty(iqty.getText().toString())){
                        iqty.setError("Please Enter Quantity");
                    }
                    else if(TextUtils.isEmpty(irol.getText().toString())){
                        irol.setError("Please Enter Re Order Level for Item");
                    }
                    else if(TextUtils.isEmpty(iup.getText().toString())) {
                        iup.setError("please Enter Unit Price for Item");
                    }
                    else if(Integer.valueOf(iqty.getText().toString().trim())<=0){
                        iqty.setError("Item Quantity should be greater than zero.");
                    }
                    else if(Integer.valueOf(irol.getText().toString().trim())<=0){
                        iqty.setError("Item Quantity should be greater than zero.");
                    }
                    else if( rol >= qty ){
                        irol.setError("ROL Should be lower than quantity");
                    }
                    else{
                        stock.setName(iname.getText().toString().trim());
                        stock.setQty(Integer.valueOf(iqty.getText().toString().trim()));
                        stock.setRol(Integer.valueOf(irol.getText().toString().trim()));
                        stock.setUnitprice(Float.valueOf(iup.getText().toString().trim()));
                        stock.setUnit(sunit.getSelectedItem().toString().trim());
                        db.child(stock.getName()).setValue(stock);
                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Toast.makeText(getApplicationContext(),"Stock Added Successfully",Toast.LENGTH_SHORT).show();
                                iname.setText("");
                                iqty.setText("");
                                irol.setText("");
                                iup.setText("");
                                Intent intent = new Intent(getApplicationContext(),stock_table.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(),"Failed to Add Stock Please Try Again",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }

            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iname.setText("");
                iqty.setText("");
                irol.setText("");
                iup.setText("");
                Toast.makeText(getApplicationContext(),"Form cleared",Toast.LENGTH_SHORT).show();
            }
        });

    }

}
