package com.example.anew;

import static com.example.anew.R.id.FullName;
import static com.example.anew.R.id.atten;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboad extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference reference;

    private String Userid;
     Button Atten;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboad);

        Atten=(Button) findViewById(R.id.atten);
        Atten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
         user = FirebaseAuth.getInstance().getCurrentUser();
         reference= FirebaseDatabase.getInstance().getReference( "Student");
         Userid=user.getUid();
         final TextView get =(TextView) findViewById(R.id.Retrive_name);
         reference.child(Userid).addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 student stud=snapshot.getValue(student.class);

                 if(stud != null){
                     String fullName =stud.fullName;
                     get.setText(fullName);
                 }

             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {
                 Toast.makeText(Dashboad.this, "Something wrong", Toast.LENGTH_SHORT).show();


             }
         });
    }
}