package com.example.auro.Director;

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

import com.example.auro.DB.Database;
import com.example.auro.R;

import java.util.List;

public class Assign_Batch extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner selectProjectManager,selectStandard;
    private EditText  numberOfBatch;
    private Button submit;

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public String username, project_manager, standard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign__batch);

        selectProjectManager = (Spinner) findViewById(R.id.projectManager);
        selectStandard = (Spinner) findViewById(R.id.standard);
        numberOfBatch = (EditText) findViewById(R.id.nosBatch);
        submit = (Button) findViewById(R.id.submit);

        selectProjectManager.setOnItemSelectedListener(this);
        selectStandard.setOnItemSelectedListener(this);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);

        Database.getProjectManagerList(username, this, getApplicationContext());
        Database.getStandards(this, getApplicationContext());



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String batch = numberOfBatch.getText().toString();
                Database.addAssignBatch(batch, project_manager, standard, getApplicationContext());

            }
        });

    }

    public void setProjectManagerSpinner(List<String> list, Context c){
        ArrayAdapter projectAdp = new ArrayAdapter(c, android.R.layout.simple_spinner_item, list);
        projectAdp.setDropDownViewResource(android.R.layout.simple_spinner_item);
        selectProjectManager.setAdapter(projectAdp);
    }

    public void setStandardsSpinner(List<String> std, Context c){
        ArrayAdapter standard = new ArrayAdapter(c, android.R.layout.simple_spinner_item, std);
        standard.setDropDownViewResource(android.R.layout.simple_spinner_item);
        selectStandard.setAdapter(standard);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int spinnerID = parent.getId();

        if(spinnerID == R.id.projectManager)
        {
            project_manager = parent.getItemAtPosition(position).toString();
        }
        else if(spinnerID == R.id.standard)
        {
            standard = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
