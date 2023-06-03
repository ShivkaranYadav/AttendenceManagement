package com.example.anew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class login extends AppCompatActivity
{

    FirebaseAuth auth= FirebaseAuth.getInstance();
    EditText emailId, pass;
    TextView registerLink;

    Button signup;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailId = (EditText) findViewById(R.id.emailid);
        pass = (EditText) findViewById(R.id.lpass);
        signup = (Button) findViewById(R.id.signup);
        registerLink = (TextView) findViewById(R.id.registerLink);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  emailid,lpass;
                emailid = String.valueOf(emailId.getText());
                lpass= String.valueOf(pass.getText());

                if(TextUtils.isEmpty(emailid))
                {
                    Toast.makeText(login.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(lpass))
                {
                    Toast.makeText(login.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(emailid,lpass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(login.this, "Login is successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),Dashboad.class));
                                    finish();
                                }
                                else {
                                    Toast.makeText(login.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });










    }

}