package com.example.auro.Center_Incharge;

import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.auro.DB.Database;
import com.example.auro.R;

public class Enroll_Student extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    EditText studentID,studentName,fatherName,motherName,addr;
    Button date,forward;
    RadioGroup gender,fatherStatus,motherStatus;
    String stdID,stdName,fN,mN,fS,mS,stdaddress,birthDate,stdgender,batch="123";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll__student);

        studentID = findViewById(R.id.studentID);
        studentName = findViewById(R.id.stdname);
        fatherName = findViewById(R.id.fathername);
        motherName = findViewById(R.id.mothername);
        addr = findViewById(R.id.address);
        gender = findViewById(R.id.gender);
        fatherStatus = findViewById(R.id.fatherStatus);
        motherStatus = findViewById(R.id.motherStatus);

        forward = findViewById(R.id.forward);

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton stdgen = group.findViewById(checkedId);
                stdgender = stdgen.getText().toString();

            }
        });

        fatherStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton fstat = group.findViewById(checkedId);
                fS = fstat.getText().toString();
            }
        });

        motherStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton gstat = group.findViewById(checkedId);
                mS = gstat.getText().toString();
            }
        });


        date = findViewById(R.id.dob);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker = new com.example.auro.Pickers.DatePicker();
                datepicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stdID = studentID.getText().toString();
                stdName = studentName.getText().toString();
                stdaddress = addr.getText().toString();
                birthDate = "1234";
                fN = fatherName.getText().toString();
                mN = motherName.getText().toString();

                Database.enrollStudent(stdID,stdName,fN,fS,mN,mS,stdgender,batch,birthDate,stdaddress);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month++;
        String dates = dayOfMonth+"-"+month+"-"+year;

        date.setText(dates);
    }
}
