package com.example.auro;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.*;
import com.example.auro.Adapter.StudentDetails;
import com.example.auro.DB.Database;
import com.example.auro.RecyclerAdapter.ApprovedStudent_RecyclerAdapter;

public class Approved_Students extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public String username,designation;
    private Spinner select;
    private Button search;
    private EditText serachValue;
    private String key,list[] = {"ID","Student Name","DOB","Gender","Father Name","Mother Name","Standard","Batch"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved__students);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);
        designation = prefs.getString("Designation",null);

        Database.getStudentDetails(username,designation,this,getApplicationContext());

        search = findViewById(R.id.search);
        select = findViewById(R.id.key);
        serachValue = findViewById(R.id.keyVal);
        select.setOnItemSelectedListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ArrayAdapter searchList = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,list);
        searchList.setDropDownViewResource(android.R.layout.simple_spinner_item);
        select.setAdapter(searchList);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = serachValue.getText().toString();

                if(data.isEmpty())
                {
                    serachValue.setError("Enter value");
                    serachValue.requestFocus();
                    return;
                }

                Database.getStudentSearch(key,data,username,designation,Approved_Students.this,getApplicationContext());
            }
        });

    }

    public void setStudentDetails(List<StudentDetails> list)
    {
        adapter = new ApprovedStudent_RecyclerAdapter(list,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        key = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
