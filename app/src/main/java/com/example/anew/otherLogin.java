package com.example.anew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class otherLogin extends AppCompatActivity {

    EditText emailId, pass;
    TextView adminLogin;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_login);

        checkLogin();
            sharedPreferences = getSharedPreferences("sharedPreference", MODE_PRIVATE);
            editor = sharedPreferences.edit();

            emailId = (EditText) findViewById(R.id.email);
            pass = (EditText) findViewById(R.id.password);
            signup = (Button) findViewById(R.id.signup);
            adminLogin = findViewById(R.id.adminLogin);

            adminLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), adminLogin.class));
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
                        Toast.makeText(otherLogin.this, "Enter email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(TextUtils.isEmpty(lpass))
                    {
                        Toast.makeText(otherLogin.this, "Enter password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(emailid,lpass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(otherLogin.this, "Login is successful", Toast.LENGTH_SHORT).show();
                                        editor.putString("sharedEmail", emailid);
                                        editor.putString("sharedPass", lpass);
                                        editor.commit();
                                        startActivity(new Intent(getApplicationContext(),facultyDashbord.class));
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(otherLogin.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });
        }

        private void checkLogin() {
            sharedPreferences = getSharedPreferences("sharedPreference", MODE_PRIVATE);
            String email = sharedPreferences.getString("sharedEmail","");
            if(sharedPreferences.contains("sharedEmail") && sharedPreferences.contains("sharedPass")){
                startActivity(new Intent(getApplicationContext(),facultyDashbord.class));
                finish();
            }
        }
    }