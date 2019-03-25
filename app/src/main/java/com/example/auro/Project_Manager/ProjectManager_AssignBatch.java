package com.example.auro.Project_Manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auro.Adapter.UserDetails;
import com.example.auro.DB.Database;
import com.example.auro.R;

import java.util.*;

public class ProjectManager_AssignBatch extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText noOfBatches;
    private Spinner centerIncharge,standard;
    private Button submit;
    private String noOfBatch,username, center, std;
    private TextView num;
    private int number;
    private List<Integer> list = new ArrayList<>();

    public static final String MY_PREFS_NAME = "MyPrefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_manager__assign_batch);


        noOfBatches = findViewById(R.id.nosBatch);
        centerIncharge = findViewById(R.id.centerIncharge);
        standard = findViewById(R.id.standard);
        submit = findViewById(R.id.submit);
        num = findViewById(R.id.num);

        centerIncharge.setOnItemSelectedListener(this);
        standard.setOnItemSelectedListener(this);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);

        Database.getCenterInchargeList(username, this, getApplicationContext());
        Database.getStandards(username,this, getApplicationContext());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noOfBatch = noOfBatches.getText().toString();

                if(noOfBatch.isEmpty())
                {
                    noOfBatches.setError("Enter no. of Batch");
                    noOfBatches.requestFocus();
                    return;
                }
                int x = Integer.parseInt(noOfBatch);

                if(x<=number)
                {
                    Database.addAssignBatchCenterIncharge(""+x, center, std,username, getApplicationContext());
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Limit Exceeded",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void setCenterInchargeList(List<String> list, Context c){
        ArrayAdapter center = new ArrayAdapter(c, android.R.layout.simple_spinner_item, list);
        center.setDropDownViewResource(android.R.layout.simple_spinner_item);
        centerIncharge.setAdapter(center);
    }

    public void setStandardsSpinner(List<String> std, List<Integer> list, Context c){
        this.list = list;
        ArrayAdapter standards = new ArrayAdapter(c, android.R.layout.simple_spinner_item, std);
        standards.setDropDownViewResource(android.R.layout.simple_spinner_item);
        standard.setAdapter(standards);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        int spinID = parent.getId();

        if(spinID == R.id.centerIncharge)
        {
            center = parent.getItemAtPosition(position).toString();
        }
        else if(spinID == R.id.standard)
        {
            number = list.get(position);
            num.setText(""+number);
            std = parent.getItemAtPosition(position).toString();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
