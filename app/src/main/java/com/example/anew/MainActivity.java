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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {


    EditText fullName, enrollmentNo, email, password;
    Button sighupButton;
    TextView Goto, courseErr;
    FirebaseAuth auth;
    Spinner courseSpinner;
    ArrayAdapter<CharSequence> adapter;
    String reference = "";


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
        courseErr = findViewById(R.id.courseErr);

        auth = FirebaseAuth.getInstance();

        // Spinner
        courseSpinner = (Spinner) findViewById(R.id.courseSpinner);

        // set the spinner using array adapter
        adapter = ArrayAdapter.createFromResource(this,R.array.course, R.layout.spinner_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(adapter);

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
                // get selected course
                String selectedCourse = courseSpinner.getSelectedItem().toString();

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

                if(selectedCourse.equals("Course")) {
                    courseErr.setError("Required!");
                    courseErr.requestFocus();
                } else if(selectedCourse.equals("MCA")) {
                    reference = "Student/MCA";
                } else if (selectedCourse.equals("MBA")) {
                    reference = "Student/MBA";
                } else if (selectedCourse.equals("BCA")) {
                    reference = "Student/BCA";
                }else {
                    reference = "Student/BBA";
                }

                auth.createUserWithEmailAndPassword(user,pass)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful())
                                {
                                    student studentInfo = new student(fn,en,user,pass,selectedCourse);
                                    FirebaseDatabase.getInstance().getReference(reference)
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())                                            .setValue(studentInfo).addOnCompleteListener(new OnCompleteListener<Void>()
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
                                    Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
