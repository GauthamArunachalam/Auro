package com.example.auro.Director;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.auro.R;

public class Assign_Batch extends AppCompatActivity {
    private Spinner selectProjectManager,selectStandard;
    private EditText  numberOfBatch;
    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign__batch);

        selectProjectManager = (Spinner) findViewById(R.id.projectManager);
        selectStandard = (Spinner) findViewById(R.id.standard);
        numberOfBatch = (EditText) findViewById(R.id.nosBatch);
        submit = (Button) findViewById(R.id.submit);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
