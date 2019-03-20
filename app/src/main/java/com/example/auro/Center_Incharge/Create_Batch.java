package com.example.auro.Center_Incharge;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.auro.DB.Database;
import com.example.auro.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Create_Batch extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private Button startDate,endDate,startTime,endTime,submit;
    private EditText batchName,stdLimit;
    private CheckBox mon,tue,wed,thu,fri,sat,sun;
    private Spinner std;
    private String standard,days,username,reporting;
    private int flagT = -1, flagD = -1;
    private StringBuffer day = new StringBuffer();
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__batch);

        startDate = findViewById(R.id.sd);
        endDate = findViewById(R.id.ed);
        startTime = findViewById(R.id.st);
        endTime = findViewById(R.id.et);
        submit = findViewById(R.id.submit);
        std = findViewById(R.id.stds);
        batchName = findViewById(R.id.batchNumber);
        stdLimit = findViewById(R.id.studentLimit);

        mon = findViewById(R.id.mon);
        tue = findViewById(R.id.tue);
        wed = findViewById(R.id.wed);
        thu = findViewById(R.id.thu);
        fri = findViewById(R.id.fri);
        sat = findViewById(R.id.sat);
        sun = findViewById(R.id.sun);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);
        reporting = prefs.getString("Reporting",null);

        std.setOnItemSelectedListener(this);

        Database.getStandards(this,getApplicationContext());

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flagT = 0;

                DialogFragment timepicker = new com.example.auro.Pickers.TimePicker();
                timepicker.show(getSupportFragmentManager(), "time picker");

            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flagT = 1;

                DialogFragment timepicker = new com.example.auro.Pickers.TimePicker();
                timepicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagD = 0;
                DialogFragment datepicker = new com.example.auro.Pickers.BatchDatePicker();
                datepicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagD = 1;
                DialogFragment datepicker = new com.example.auro.Pickers.BatchDatePicker();
                datepicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String batch = batchName.getText().toString();
                String sD = startDate.getText().toString();
                String eD = endDate.getText().toString();
                String sT = startTime.getText().toString();
                String eT = endTime.getText().toString();
                String limit = stdLimit.getText().toString();

                if(mon.isChecked()){
                    day.append("Monday, ");
                }if(tue.isChecked()){
                    day.append("Tuesday, ");
                }if(wed.isChecked()){
                    day.append("Wednesday, ");
                }if(thu.isChecked()){
                    day.append("Thursday, ");
                }if(fri.isChecked()){
                    day.append(("Friday, "));
                }if(sat.isChecked()){
                    day.append("Saturday, ");
                }if(sun.isChecked()){
                    day.append("Sunday, ");
                }

                days = day.substring(0,(day.length()-2));

                Database.createBatch(batch,sD,eD,sT,eT,limit,username,days,standard,reporting);

            }
        });
    }

    public void setStandardsSpinner(List<String> stds, Context c){
        ArrayAdapter standard = new ArrayAdapter(c, android.R.layout.simple_spinner_item, stds);
        standard.setDropDownViewResource(android.R.layout.simple_spinner_item);
        std.setAdapter(standard);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        standard = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String time;
        if(hourOfDay>12) {
            hourOfDay = hourOfDay - 12;
            time = hourOfDay + ":" + minute + " PM";
        }
        else if(hourOfDay == 0)
        {
            hourOfDay = 12;
            time = hourOfDay +":"+ minute+" AM";
        }
        else if(hourOfDay == 12)
        {
            time = hourOfDay +":"+ minute+" PM";
        }
        else
        {
            time = hourOfDay +":"+ minute+" PM";
        }
        if(flagT == 0)
        {
            startTime.setText(time);
        }
        else if(flagT == 1)
        {
            endTime.setText(time);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month++;
        String date = dayOfMonth+"-"+month+"-"+year;
        if(flagD == 0)
        {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                Date dts = dateFormat.parse(date);

                Calendar selectedDate = new GregorianCalendar();
                selectedDate.setTime(dts);

                int x = selectedDate.getTime().getDay();

                if (x == 0) {
                    Toast.makeText(getApplicationContext(), "Sunday", Toast.LENGTH_LONG).show();
                } else if (x == 1) {
                    Toast.makeText(getApplicationContext(), "Monday", Toast.LENGTH_LONG).show();
                } else if (x == 2) {
                    Toast.makeText(getApplicationContext(), "Tuesday", Toast.LENGTH_LONG).show();
                } else if (x == 3) {
                    Toast.makeText(getApplicationContext(), "Wednesday", Toast.LENGTH_LONG).show();
                } else if (x == 4) {
                    Toast.makeText(getApplicationContext(), "Thursday", Toast.LENGTH_LONG).show();
                } else if (x == 5) {
                    Toast.makeText(getApplicationContext(), "Friday", Toast.LENGTH_LONG).show();
                } else if (x == 6) {
                    Toast.makeText(getApplicationContext(), "Saturday", Toast.LENGTH_LONG).show();
                }

                startDate.setText(date);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        else if(flagD == 1)
        {
            endDate.setText(date);
        }
    }

}
