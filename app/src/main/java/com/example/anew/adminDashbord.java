package com.example.anew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class adminDashbord extends AppCompatActivity implements addFacultyDialog.facultyDialogListner {

    Button addFaculty, logout;
    String facultyName, facultyEmail, facultyPassword;

    @Override
    public void applyTexts(String fName, String fEmail) {
        facultyName = fName;
        facultyEmail = fEmail;
    }
    public void openDialog(){
        addFacultyDialog addFacultyDialog = new addFacultyDialog();
        addFacultyDialog.show(getSupportFragmentManager(), "Faculty Dialog");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashbord);

        logout = findViewById(R.id.logout);

        addFaculty = findViewById(R.id.addFaculty);
        addFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),adminLogin.class));
                finish();
            }
        });

        facultyPassword = facultyName + "123";

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(facultyEmail, facultyPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Faculty facultyInfo = new Faculty(facultyName, facultyEmail, facultyPassword);
                                FirebaseDatabase.getInstance().getReference("Faculty")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(facultyInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(adminDashbord.this, "Faculty account created", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(adminDashbord.this, "Failed!Try again.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
}