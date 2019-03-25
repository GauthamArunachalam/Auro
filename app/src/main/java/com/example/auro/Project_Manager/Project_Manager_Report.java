package com.example.auro.Project_Manager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.auro.R;

public class Project_Manager_Report extends AppCompatActivity {
    private CardView batchReport,studentReport,attendanceReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project__manager__report);

        batchReport = findViewById(R.id.batchReport);
        studentReport = findViewById(R.id.studentReport);
        attendanceReport = findViewById(R.id.attendanceReport);
        batchReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Project_Manager_Batch_Report.class));
            }
        });
        studentReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Project_Manager_Student_Report.class));
            }
        });
        attendanceReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Project_Manager_Attendance_Report.class));
            }
        });
    }
}
