package com.example.auro.Project_Manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.auro.Adapter.Batch;
import com.example.auro.DB.Database;
import com.example.auro.R;

public class Request_Batch_Details extends AppCompatActivity {
    private TextView batchName,days,endDate,startDate,endTime,startTime,incharge,standard,stdLimit;
    private Button approve,reject,escalate;
    private String batch;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public String reporting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request__batch__details);

        batchName = findViewById(R.id.batchName);
        days = findViewById(R.id.days);
        endDate = findViewById(R.id.endDay);
        startDate = findViewById(R.id.startDay);
        endTime = findViewById(R.id.endTime);
        startTime = findViewById(R.id.startTime);
        incharge = findViewById(R.id.incharge);
        standard = findViewById(R.id.std);
        stdLimit = findViewById(R.id.studentLimit);
        approve = findViewById(R.id.approve);
        reject = findViewById(R.id.reject);
        escalate = findViewById(R.id.escalate);

        batch = getIntent().getStringExtra("name");

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        reporting = prefs.getString("Reporting",null);

        Database.getBatchDetails(batch,this,getApplicationContext());

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approvebatch();
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectbatch();
            }
        });
        escalate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escalatebatch();
            }
        });
    }

    public void escalatebatch(){
        Database.escalateBatch(batch,reporting,this,getApplicationContext());
    }

    public void approvebatch(){
        Database.approveBatch(batch,this,getApplicationContext());
    }

    public void rejectbatch(){
        Database.rejectBatch(batch,this,getApplicationContext());
    }

    public void setBatchDetails(final Batch bt, final Context c){
        batchName.setText(bt.getBatch_name());
        days.setText(bt.getDays());
        endDate.setText(bt.getEnd_date());
        startDate.setText(bt.getStart_date());
        endTime.setText(bt.getStart_time());
        startTime.setText(bt.getStart_time());
        incharge.setText(bt.getIncharge());
        standard.setText(bt.getStandard());
        stdLimit.setText(bt.getStd_limit());
    }
}
