package com.example.anew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class adminLogin extends AppCompatActivity {

    EditText emailId, pass;
    TextView forgetPassword;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        emailId = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
        signup = (Button) findViewById(R.id.signup);
        forgetPassword = findViewById(R.id.forgetPassword);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  emailid,lpass;
                emailid = String.valueOf(emailId.getText());
                lpass= String.valueOf(pass.getText());

                if(TextUtils.isEmpty(emailid))
                {
                    Toast.makeText(adminLogin.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(lpass))
                {
                    Toast.makeText(adminLogin.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailid,lpass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(adminLogin.this, "Login is successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),adminDashbord.class));
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(adminLogin.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}