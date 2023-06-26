package com.example.anew;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MarkAttendance extends AppCompatActivity {

    Spinner semSpinner, subSpinner;
    ArrayAdapter<CharSequence> adapter1, adapter2;
    String selectedSem, selectedSub;
    TextView semError;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        // first spinner
        semSpinner = (Spinner) findViewById(R.id.spinner1);

        adapter1 = ArrayAdapter.createFromResource(this, R.array.sem, R.layout.spinner_layout);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        semSpinner.setAdapter(adapter1);

        semError = findViewById(R.id.errorSem);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // showing error message if user doesn't select semester
                if(selectedSem.equals("Semester"))
                {
                    semError.setError("Required!");
                    semError.requestFocus();
                }
                else{
                    semError.setError(null);
                }
            }
        });

        // when sem spinner will be selected
        semSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

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
                        case "1": adapter2 = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.sub_sem_1, R.layout.spinner_layout);
                            break;

                        case "2": adapter2 = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.sub_sem_2, R.layout.spinner_layout);
                            break;

                        case "3": adapter2 = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.sub_sem_3, R.layout.spinner_layout);
                            break;
                        default: break;
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



    }
}