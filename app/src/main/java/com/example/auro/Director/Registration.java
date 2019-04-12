package com.example.auro.Director;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.auro.Adapter.UserDetails;
import com.example.auro.DB.Database;

import com.example.auro.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText userID, password;
    Button register;
    Spinner designation, project;

    String desig, repor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        userID = (EditText)findViewById(R.id.userID);
        project = (Spinner)findViewById(R.id.project);
        project.setOnItemSelectedListener(this);
        password = (EditText)findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);

        designation = (Spinner)findViewById(R.id.designation);

        designation.setOnItemSelectedListener(this);

        List<String> spinnerList = new ArrayList<String>();
        spinnerList.add("Select Designation");
        spinnerList.add("Project Manager");
        spinnerList.add("Center Incharge");

        final ArrayAdapter desi = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerList);
        desi.setDropDownViewResource(android.R.layout.simple_spinner_item);
        designation.setAdapter(desi);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userID.getText().toString();
                String pass = password.getText().toString();
                String reporting = "";

                if(userName.isEmpty()){
                    userID.setError("Enter user ID");
                    userID.requestFocus();
                    return;
                }else if(pass.isEmpty()){
                    password.setError("Enter password");
                    password.requestFocus();
                    return;
                }else if(desig == null){
                    Toast.makeText(getApplicationContext(),"Select desigination",Toast.LENGTH_LONG).show();
                    return;
                }

                if(desig.equals("Project Manager")){
                    reporting = "director";
                }else{
                    reporting = repor;
                }
                if(desig.equals("Center Incharge") && reporting.equals("Select project manager")){
                    Toast.makeText(getApplicationContext(),"Select project manager of the center incharge",Toast.LENGTH_LONG).show();
                    return;
                }

                Database.register(userName, pass, desig, reporting, getApplicationContext());

            }
        });

    }

    public void setProjectManagerSpinner(List<String> list, Context c){
        ArrayAdapter projectAdp = new ArrayAdapter(c, android.R.layout.simple_spinner_item, list);
        projectAdp.setDropDownViewResource(android.R.layout.simple_spinner_item);
        project.setAdapter(projectAdp);

        project.setVisibility(View.VISIBLE);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            String data = (String)parent.getItemAtPosition(position);

            if(data.equals("Project Manager") || data.equals("Center Incharge") || data.equals("Choose")) {
                desig = (String) parent.getItemAtPosition(position);
                if(desig.equals("Center Incharge")){
                    Database.getProjectManagerList(this, getApplicationContext());
                }
                else
                {
                    project.setVisibility(View.GONE);
                }
            }else{
                repor = data;
            }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
