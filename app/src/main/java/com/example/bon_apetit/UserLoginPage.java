package com.example.bon_apetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class UserLoginPage extends AppCompatActivity {
    EditText txtemail,txtPwd;
    Button login,singup;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_page);

        txtemail = findViewById(R.id.txtusername);
        txtPwd = findViewById(R.id.txtpwd);

        login = findViewById(R.id.btnlogin);
        singup = findViewById(R.id.btnreg);

        firebaseAuth=FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty((txtemail).getText().toString())){
                    txtemail.setError("Email is required");
                }
                if(TextUtils.isEmpty(txtPwd.getText().toString())){
                    txtPwd.setError("Please enter your password");
                }
                if(txtPwd.getText().toString().length()<6){
                    txtPwd.setError("password shuold contain more than 6 characters");
                }
                else{
                    String email = txtemail.getText().toString().trim();
                    String pwd = txtPwd.getText().toString().trim();
                    firebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),"Login Successfull",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),payment_one.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Incorrect username or password",Toast.LENGTH_SHORT).show();
                                txtemail.setText("");
                                txtPwd.setText("");
                            }
                        }
                    });
                }

            }
        });
        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),customerRegistration.class);
                startActivity(intent);
            }
        });
    }
}