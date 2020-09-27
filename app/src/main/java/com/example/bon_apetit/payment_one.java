package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.Models.Customer;
import com.example.Models.Payment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class payment_one extends AppCompatActivity {
    EditText address;
    RadioGroup paymentmethod;
    RadioButton cashorcard;

    EditText email,date,time;
    long maxid=0;
    Button proceed,cancel;
    FirebaseAuth fauth;
    DatabaseReference db;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_one);

        address=findViewById(R.id.txtaddress);
        email=findViewById(R.id.txtemail);
        cashorcard=findViewById(R.id.radioCash);
        date=findViewById(R.id.txtdate);
        time=findViewById(R.id.txttime);
        proceed=findViewById(R.id.imagerec);
        cancel=findViewById(R.id.btncancel);

        paymentmethod = findViewById(R.id.rgroup);
     //   String uid = fauth.getCurrentUser().getUid();

        final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        db= FirebaseDatabase.getInstance().getReference().child("Customer").child(user.getUid());

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    Customer data = dataSnapshot.getValue(Customer.class);
                    address.setText(dataSnapshot.child("address").getValue().toString());
                    email.setText(dataSnapshot.child("email").getValue().toString());
                }
                else {
                    Toast.makeText(getApplicationContext(),"Nothing to display",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        db= FirebaseDatabase.getInstance().getReference().child("Payment");
        fauth = FirebaseAuth.getInstance();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxid=(dataSnapshot.getChildrenCount());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Payment payment = new Payment();
                db= FirebaseDatabase.getInstance().getReference().child("Payment").child(String.valueOf(System.currentTimeMillis()));
                try{
                    if(TextUtils.isEmpty(address.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Please Enter Address",Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(email.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Please Enter Email",Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(date.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Please Enter Date",Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(time.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Please Enter Time",Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(cashorcard.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Please Enter Method",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        payment.setAddress(address.getText().toString().trim());
                        payment.setEmail(email.getText().toString().trim());
                        payment.setDate(date.getText().toString().trim());
                        payment.setTime(time.getText().toString().trim());
                        int radiobuttonid=paymentmethod.getCheckedRadioButtonId();
                        cashorcard=findViewById(radiobuttonid);
                        payment.setMethod(cashorcard.getText().toString().trim());

                        db.setValue(payment);
                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Toast.makeText(getApplicationContext(),"Payment recorded",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }


                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        });
    }

}