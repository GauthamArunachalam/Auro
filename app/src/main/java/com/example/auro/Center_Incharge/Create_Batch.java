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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.auro.DB.Database;
import com.example.auro.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Create_Batch extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private ImageView startDate,endDate,startTime,endTime;
    private Button submit;
    private TextView sD,eD,sT,eT;
    private EditText batchName,stdLimit;
    private CheckBox mon,tue,wed,thu,fri,sat,sun;
    private Spinner std;
    private String standard,days,username,reporting;
    private int flagT = -1, flagD = -1;
    private StringBuffer day = new StringBuffer();
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private List<Integer> list = new ArrayList<>();
    private TextView num;
    private int number;

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
        sD = findViewById(R.id.startD);
        eD = findViewById(R.id.endD);
        sT = findViewById(R.id.startT);
        eT = findViewById(R.id.endT);
        num = findViewById(R.id.count);
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

        Database.getStandards(username,this,getApplicationContext());

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
                String sDD = sD.getText().toString();
                String eDD = eD.getText().toString();
                String sTT = sT.getText().toString();
                String eTT = eT.getText().toString();
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

                if(batch.isEmpty()){
                    batchName.setError("Enter proper batch name");
                    batchName.requestFocus();
                    return;
                }else if(sDD.equals("")){
                    Toast.makeText(getApplicationContext(),"Select Start date",Toast.LENGTH_SHORT).show();
                    return;
                }else if(eDD.equals("")){
                    Toast.makeText(getApplicationContext(),"Select End date",Toast.LENGTH_SHORT).show();
                    return;
                }else if(sTT.equals("")){
                    Toast.makeText(getApplicationContext(),"Select Start Time",Toast.LENGTH_SHORT).show();
                    return;
                }else if(eTT.equals("")){
                    Toast.makeText(getApplicationContext(),"Select End date",Toast.LENGTH_SHORT).show();
                    return;
                }else if(limit.isEmpty()){
                    stdLimit.setError("Enter student limit");
                    stdLimit.requestFocus();
                    return;
                }else if(day.toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Select working days",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(number<=0)
                {
                    Toast.makeText(getApplicationContext(),"Limit Exceeded",Toast.LENGTH_SHORT).show();
                    return;
                }

                days = day.substring(0,(day.length()-2));

                Database.createBatch(batch,sDD,eDD,sTT,eTT,limit,username,days,standard,reporting,getApplicationContext(),Create_Batch.this);

            }
        });
    }

    public void setStandardsSpinner(List<String> stds,List<Integer> list, Context c){

        this.list = list;

        ArrayAdapter standard = new ArrayAdapter(c, android.R.layout.simple_spinner_item, stds);
        standard.setDropDownViewResource(android.R.layout.simple_spinner_item);
        std.setAdapter(standard);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        standard = parent.getItemAtPosition(position).toString();
        number = list.get(position);
        num.setText(""+number);
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
            sT.setText(time);
        }
        else if(flagT == 1)
        {
            eT.setText(time);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month++;
        String date = dayOfMonth+"-"+month+"-"+year;
        if(flagD == 0)
        {
            sD.setText(date);
        }
        else if(flagD == 1)
        {
            eD.setText(date);
        }
    }

}
