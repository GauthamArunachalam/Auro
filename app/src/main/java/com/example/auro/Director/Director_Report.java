package com.example.auro.Director;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.auro.R;

public class Director_Report extends AppCompatActivity {

    private Button batchReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director__report);

        batchReport = findViewById(R.id.batchReport);
        batchReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Director_Batch_Report.class));
            }
        });
    }
}
