package com.example.auro.Director;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.auro.Adapter.Batch;
import com.example.auro.DB.Database;
import com.example.auro.Project_Manager.Request_Batch_Details;
import com.example.auro.R;

public class Director_Request_Details extends AppCompatActivity {
    private TextView batchName,days,endDate,startDate,endTime,startTime,incharge,standard,stdLimit;
    private Button approve,reject;
    private String batch, remarks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director__request__details);

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

        batch = getIntent().getStringExtra("name");

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
                rejects(v);
            }
        });
    }


    public void approvebatch(){
        Database.approveBatch(batch);
    }

    public void rejectbatch()
    {
        Database.rejectBatch(batch,remarks,incharge.getText().toString(),standard.getText().toString());
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
    public void rejects(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reason for rejecting");
        final EditText remark = new EditText(Director_Request_Details.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        remark.setLayoutParams(lp);
        builder.setView(remark);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                remarks = remark.getText().toString();
                rejectbatch();
            }
        });
        builder.show();
    }
}
