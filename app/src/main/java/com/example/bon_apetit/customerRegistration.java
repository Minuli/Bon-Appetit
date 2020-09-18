package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.Models.Customer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class customerRegistration extends AppCompatActivity {
    EditText cname,caddress,cemail,cnumber,cnic,cpassword;
    Button regbtn;
    long maxid = 0;
    Customer customer;
    DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration);

        cname = findViewById(R.id.txtname);
        caddress = findViewById(R.id.txtaddress);
        cemail = findViewById(R.id.txtemail);
        cnumber = findViewById(R.id.txttele);
        cnic = findViewById(R.id.txtnic);
        cpassword = findViewById(R.id.txtpassword);

        regbtn= findViewById(R.id.btnregister);

        customer= new Customer();



        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db= FirebaseDatabase.getInstance().getReference().child("Customer");
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            maxid=(dataSnapshot.getChildrenCount());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                try{
                    customer.setNic(cnic.getText().toString());
                    customer.setName(cname.getText().toString());
                    customer.setEmail(cemail.getText().toString());
                    customer.setNumber(cnumber.getText().toString());
                    customer.setAddress(caddress.getText().toString());
                    customer.setPassword(cpassword.getText().toString());

                    db.child(String.valueOf(maxid+1)).setValue(customer);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        });

    }
}