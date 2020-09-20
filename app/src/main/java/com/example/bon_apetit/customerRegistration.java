package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Models.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
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
    FirebaseAuth fauth;
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
                fauth = FirebaseAuth.getInstance();
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
                    if(TextUtils.isEmpty(cnic.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Please Enter Nic",Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(cname.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Please Enter name",Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(cemail.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Please Enter email",Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(cnumber.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Please Enter Contact number",Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(caddress.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Please Enter address",Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(cpassword.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Please Enter Password",Toast.LENGTH_SHORT).show();
                    }
                    else if(cpassword.getText().toString().trim().length() < 6){
                        Toast.makeText(getApplicationContext(),"Password should have more than 6 characters",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        customer.setNic(cnic.getText().toString().trim());
                        customer.setName(cname.getText().toString().trim());
                        customer.setEmail(cemail.getText().toString().trim());
                        customer.setNumber(cnumber.getText().toString().trim());
                        customer.setAddress(caddress.getText().toString().trim());
                        customer.setPassword(cpassword.getText().toString().trim());
                        db.child(String.valueOf(maxid+1)).setValue(customer);
                        fauth.createUserWithEmailAndPassword(customer.getEmail(),customer.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"User added Successfully",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Failed to create account",Toast.LENGTH_SHORT).show();
                                }
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