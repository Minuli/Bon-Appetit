package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.Models.Stock;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class update_stock extends AppCompatActivity {
    //firebase
    DatabaseReference dbref;

    //model
    Stock stock;
    int flag = 0 ;
    Spinner unit;
    //Radio buutons and group
    RadioGroup radioGroup;
    RadioButton radioButton;

    //textboxes
    EditText sname,sqty,srol,sunit,sup;

    //Buttons
    Button bdel,bupdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_stock);

        //get item name from intent
        Intent intent = getIntent();
        final String itmName = intent.getStringExtra("stockName");

        //Mapping Items
        unit =findViewById(R.id.stockUnit);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(update_stock.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.stocks_units)     );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unit.setAdapter(adapter);

        sname = findViewById(R.id.txt_inventory);
        sqty = findViewById(R.id.txt_Qty);
        srol = findViewById(R.id.txt_ROL);
        sup = findViewById(R.id.txt_UP);
        bupdate = findViewById(R.id.btnstockupdate);
        bdel = findViewById(R.id.btnstockdel);

        //retreving and laodinng data from db
        dbref = FirebaseDatabase.getInstance().getReference().child("stock");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    System.out.println(snapshot.getValue());
                    if(itmName.equals(snapshot.getKey())){
                        sname.setText(snapshot.child("name").getValue().toString());
                        sqty.setText(snapshot.child("qty").getValue().toString());
                        srol.setText(snapshot.child("rol").getValue().toString());
                        sup.setText(snapshot.child("unitprice").getValue().toString());
                        Toast.makeText(getApplicationContext(),"Data loaded",Toast.LENGTH_SHORT).show();
                        flag = 1;
                    }

                }
                if(flag == 0){
                    Toast.makeText(getApplicationContext(),"Item does not exist please re-Eenter Item name",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),stock_table.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Item does not exist please re-Eenter Item name",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),stock_table.class);
                startActivity(intent);
            }
        });

        bupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stock = new Stock();
                dbref = FirebaseDatabase.getInstance().getReference().child("stock");
                int qty = Integer.parseInt(sqty.getText().toString());
                int rol= Integer.parseInt(srol.getText().toString()) ;
               try {
                   if(TextUtils.isEmpty(sqty.getText().toString())){
                       sqty.setError("Quantity feild cannot be Empty");
                   }
                   else if(TextUtils.isEmpty(srol.getText().toString())){
                       srol.setError("ROL feild cannot be Empty");
                   }
                   else if(TextUtils.isEmpty(sup.getText().toString())) {
                       sup.setError("Unit price feild cannot be Empty");
                   }
                   else if( rol >= qty ){
                       srol.setError("ROL Should be lower than quantity");
                   }
                   else{
                       stock.setName(itmName);
                       stock.setQty(Integer.valueOf(sqty.getText().toString().trim()));
                       stock.setRol(Integer.valueOf(srol.getText().toString().trim()));
                       stock.setUnitprice(Float.valueOf(sup.getText().toString().trim()));
                       stock.setUnit(unit.getSelectedItem().toString().trim());
                       dbref.child(itmName).setValue(stock);
                       dbref.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               Toast.makeText(getApplicationContext(),"Stock updated Scucessfully",Toast.LENGTH_SHORT).show();
                               Intent intent = new Intent(getApplicationContext(),stock_table.class);
                               startActivity(intent);
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {
                               Toast.makeText(getApplicationContext(),"Update failed please try again",Toast.LENGTH_SHORT).show();
                           }
                       });
                   }
               }catch (Exception er){
                   System.out.println(er.getMessage());
               }
            }
        });

        bdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbref = FirebaseDatabase.getInstance().getReference().child("stock");
                dbref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(itmName)){
                            dbref = FirebaseDatabase.getInstance().getReference().child("stock").child(itmName);
                            dbref.removeValue();
                            Toast.makeText(getApplicationContext(),"item Removed Succesfully",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),stock_table.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}