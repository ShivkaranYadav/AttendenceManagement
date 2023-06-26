package com.example.anew;

import static com.example.anew.R.id.FullName;
import static com.example.anew.R.id.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Dashboad extends AppCompatActivity {
    // Database variables
    private FirebaseUser user;
    private DatabaseReference reference;
    private String Userid;
    Button Attendance;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    // Location variables
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView latitude, longitude, address;
    Button getLocation, logout;
    private final static int REQUEST_CODE = 100;

    // Database name retrieval variables
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboad);

        // shared preferences variables
        sharedPreferences = getSharedPreferences("sharedPreference", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //location variables
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        address = findViewById(R.id.location);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // other variables
        Attendance=(Button) findViewById(R.id.attendance);
        getLocation = (Button) findViewById(R.id.getLocation);
        logout = (Button) findViewById(R.id.logout);

        // getLocation button code to get the user's current location
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });

        Attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MarkAttendance.class));
            }
        });

        // logout button for logout user from app
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.commit();
                startActivity(new Intent(getApplicationContext(),login.class));
                finish();
            }
        });

         user = FirebaseAuth.getInstance().getCurrentUser();
         reference= FirebaseDatabase.getInstance().getReference( "Student/MCA");
         Userid=user.getUid();
         final TextView get =(TextView) findViewById(R.id.name);
         reference.child(Userid).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 student userName = snapshot.getValue(student.class);

                 if(userName != null){
                     String fullName = userName.fullName;
                     get.append(fullName);
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {
                 Toast.makeText(Dashboad.this, "Something wrong", Toast.LENGTH_SHORT).show();
             }
         });
    }

    // get location method
    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(Dashboad.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            latitude.setText("Latitude: " + addresses.get(0).getLatitude());
                            longitude.setText("Longitude: " + addresses.get(0).getLongitude());
                            address.setText("Address: " + addresses.get(0).getAddressLine(0));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            askPermission();
        }
    }

    // ask permission method for checking the permission
    private void askPermission() {
        ActivityCompat.requestPermissions(Dashboad.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {

        if (requestCode == REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getLastLocation();
            }
            else
            {
                Toast.makeText(Dashboad.this, "Please provide the required permission", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}