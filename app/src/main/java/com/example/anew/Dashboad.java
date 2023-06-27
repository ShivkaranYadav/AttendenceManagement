package com.example.anew;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Dashboad extends AppCompatActivity {

    Spinner semSpinner, subSpinner;
    ArrayAdapter<CharSequence> adapter1, adapter2;
    String selectedSem, selectedSub, fullName, enrollment;
    TextView semError;

    // Database variables
    private FirebaseUser user;
    private DatabaseReference reference;
    private String Userid;
    Button Attendance;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    // Location variables
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView address, enrollmentNo, name;
    Button getLocation, logout;

    // store current location coordinates in variables
    double lati, longi;

    private final static int REQUEST_CODE = 100;

    // Database name retrieval variables
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboad);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference( "Student");
        Userid=user.getUid();

        // for name retrieval and enrollment no
        name =(TextView) findViewById(R.id.name);
        enrollmentNo = findViewById(R.id.enrollment_no);

        // shared preferences variables
        sharedPreferences = getSharedPreferences("sharedPreference", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //location variables
        address = findViewById(R.id.location);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // other variables
        Attendance=(Button) findViewById(R.id.attendance);
        getLocation = (Button) findViewById(R.id.getLocation);
        logout = (Button) findViewById(R.id.logout);

        // first spinner
        semSpinner = (Spinner) findViewById(R.id.spinner1);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.sem, R.layout.spinner_layout);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semSpinner.setAdapter(adapter1);
        semError = findViewById(R.id.errorSem);

        // to get user name
        reference.child(Userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user userName = snapshot.getValue(com.example.anew.user.class);

                if(userName != null){
                    fullName = userName.fullName;
                    enrollment = userName.enrollmentNo;
                    name.append(fullName);
                    enrollmentNo.append(enrollment);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Dashboad.this, "Something wrong", Toast.LENGTH_SHORT).show();
            }
        });

        // getLocation button code to get the user's current location
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });

        // when sem spinner will be selected
        semSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                // get subject spinner
                subSpinner = (Spinner) findViewById(R.id.spinner2);

                // obtain selected semester
                selectedSem = semSpinner.getSelectedItem().toString();

                int parent = adapterView.getId();
                if (parent == R.id.spinner1)
                {
                    switch(selectedSem)
                    {
                        case "Semester": adapter2 = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.no_sem_sel, R.layout.spinner_layout);
                            break;
                        case "1": adapter2 = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.mca_sub_sem_1, R.layout.spinner_layout);
                            break;

                        case "2": adapter2 = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.mca_sub_sem_2, R.layout.spinner_layout);
                            break;

                        case "3": adapter2 = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.mca_sub_sem_3, R.layout.spinner_layout);
                            break;
                        default:
                            break;
                    }
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    subSpinner.setAdapter(adapter2);

                    subSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            selectedSub = subSpinner.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get current date and time
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String dateTime = simpleDateFormat.format(calendar.getTime());

                // function checks whether the user is inside college campus or not
                checkLocation();

                // showing error message if user doesn't select semester
                if(selectedSem.equals("Semester"))
                {
                    semError.setError("Required!");
                    semError.requestFocus();
                    return;
                }
                else{
                    semError.setError(null);
                }

                // class object call
                MarkAttendance markAttendance = new MarkAttendance(fullName,enrollment,selectedSem,selectedSub, dateTime);

                FirebaseDatabase.getInstance().getReference().child("Attendance").push().setValue(markAttendance);
                Toast.makeText(Dashboad.this, "Attendance marked successfully", Toast.LENGTH_SHORT).show();
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
    }

    private void checkLocation() {
        if(lati >= 20.940091617084526 && lati <= 20.94065629990854 && longi >= 72.95007291696623 && longi <= 72.95091772289511)
        {
            Toast.makeText(Dashboad.this, "You are in college campus", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(Dashboad.this, "sorry! you are not in campus! change location and try again!", Toast.LENGTH_SHORT).show();
        }
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
                            lati = addresses.get(0).getLatitude();
                            longi = addresses.get(0).getLongitude();
                            address.setText("Current location: " + addresses.get(0).getAddressLine(0));
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