package com.example.anew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    EditText fullName, enrollmentNo, email, password;
    Button sighupButton;
    TextView Goto;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fullName = (EditText) findViewById(R.id.FullName);
        enrollmentNo = (EditText) findViewById(R.id.En);
        email = (EditText) findViewById(R.id.Email);
        password = (EditText) findViewById(R.id.password);
        sighupButton = (Button) findViewById(R.id.Login);
        Goto=(TextView) findViewById(R.id.Goto);

        auth = FirebaseAuth.getInstance();

        Goto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent( MainActivity.this, login.class));
            }
        });

        sighupButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String fn = fullName.getText().toString();
                String en = enrollmentNo.getText().toString();
                String user = email.getText().toString();
                String pass = password.getText().toString();

                if(TextUtils.isEmpty(fn))
                {
                    Toast.makeText(MainActivity.this, "Please enter fullname", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(en)){
                    Toast.makeText(MainActivity.this, "Please enter Enrollment no", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(user))
                {
                    Toast.makeText(MainActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(pass)){
                    Toast.makeText(MainActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(user,pass)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful())
                                {
                                    user userInfo = new user(fn,en,user,pass);
                                    FirebaseDatabase.getInstance().getReference("Student")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>()
                                            {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    Toast.makeText(MainActivity.this, "signup successful", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(),login.class));
                                                    finish();
                                                }
                                            });
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
