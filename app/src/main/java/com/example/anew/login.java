package com.example.anew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class login extends AppCompatActivity {

    FirebaseAuth auth;
    EditText emailId, pass;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        emailId = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.pass);
        signup = (Button) findViewById(R.id.signup);

        String email = emailId.getText().toString();
        String password = pass.getText().toString();


        signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                checkUser();
            }
        });
    }



    public void checkUser(){

        String email = emailId.getText().toString().trim();
        String passwd = pass.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(login.this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(passwd))
        {
            Toast.makeText(login.this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Student");
        Query checkUserDatabase = reference.orderByChild("email").equalTo(email);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                if(snapshot.exists())
                {
                    String passfromDB = snapshot.child(email).child("password").getValue(String.class);
                    Toast.makeText(login.this, passfromDB, Toast.LENGTH_SHORT).show();

                    if(!Objects.equals(passfromDB, passwd))
                    {
                        Toast.makeText(login.this, "login successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(login.this, Dashbord.class));
                    }
                    else
                    {
                        Toast.makeText(login.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        pass.requestFocus();
                    }
                }
                else
                {
                    Toast.makeText(login.this, "User not found", Toast.LENGTH_SHORT).show();
                    emailId.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}