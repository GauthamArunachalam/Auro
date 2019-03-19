package com.example.auro.Project_Manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.*;

import com.example.auro.Adapter.Batch;
import com.example.auro.DB.Database;
import com.example.auro.R;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Project_Manager_Batch_Report extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button report ;
    Spinner centerIncharge,batches;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    String username,reporting,designation;
    String incharge, batch;
    WritableWorkbook workbook;
    WritableSheet sheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project__manager__batch__report);

        report = findViewById(R.id.report);
        centerIncharge = findViewById(R.id.centerIncharge);
        centerIncharge.setOnItemSelectedListener(this);
        batches = findViewById(R.id.batch);
        batches.setOnItemSelectedListener(this);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);
        reporting = prefs.getString("Reporting",null);
        designation = prefs.getString("Designation",null);

        Database.getCenterInchargeList(username,this,getApplicationContext());

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(incharge.equals("All"))
                {
                    Database.getBatchDetails(username,Project_Manager_Batch_Report.this,getApplicationContext());
                }
                else
                {
                    
                }

            }
        });
    }

    public void setBatchDetailList(List<Batch> list)
    {
        int listLength = list.size();

        File sd = Environment.getExternalStorageDirectory();

        File directory = new File(sd.getAbsolutePath());

        if (!directory.isDirectory()) {
            directory.mkdirs();
        }

        try {
            File file = new File(directory, batch + ".xls");
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));

            workbook = Workbook.createWorkbook(file, wbSettings);

            sheet = workbook.createSheet("Report", 0);

            sheet.addCell(new Label(0, 1, "Batch Name"));
            sheet.addCell(new Label(0, 2, "Start Date"));
            sheet.addCell(new Label(0, 3, "End Date"));
            sheet.addCell(new Label(0, 4, "Start Time"));
            sheet.addCell(new Label(0, 5, "End Time"));
            sheet.addCell(new Label(0, 6, "Days"));
            sheet.addCell(new Label(0, 7, "Standard"));
            sheet.addCell(new Label(0, 8, "Student Limit"));
            sheet.addCell(new Label(0, 9, "Incharge"));

            for(int inc = 0; inc<listLength ; inc++)
            {
                Batch b = list.get(inc);

                sheet.addCell(new Label(inc+1, 1, b.getBatch_name()));
                sheet.addCell(new Label(inc+1, 2, b.getStart_date()));
                sheet.addCell(new Label(inc+1, 3, b.getEnd_date()));
                sheet.addCell(new Label(inc+1, 4, b.getStart_time()));
                sheet.addCell(new Label(inc+1, 5, b.getEnd_time()));
                sheet.addCell(new Label(inc+1, 6, b.getDays()));
                sheet.addCell(new Label(inc+1, 7, b.getStandard()));
                sheet.addCell(new Label(inc+1, 8, b.getStd_limit()));
                sheet.addCell(new Label(inc+1, 9, b.getIncharge()));
            }

            workbook.write();
            workbook.close();

            Toast.makeText(getApplicationContext(),"Report Generated",Toast.LENGTH_LONG).show();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setCenterInchargeList(List<String> list , Context c)
    {
        ArrayAdapter centerincharge = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,list);
        centerincharge.setDropDownViewResource(android.R.layout.simple_spinner_item);
        centerIncharge.setAdapter(centerincharge);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int ID = parent.getId();

        if(ID == R.id.centerIncharge)
        {
            incharge = parent.getItemAtPosition(position).toString();

            if(incharge.equals("All"))
            {
                batches.setVisibility(View.GONE);
            }
            else
            {
                batches.setVisibility(View.VISIBLE);
                Database.getBatchList(incharge,Project_Manager_Batch_Report.this,getApplicationContext());
            }
        }
        if(ID == R.id.batch)
        {
            batch = parent.getItemAtPosition(position).toString();
        }
    }

    public void setBatchList(List<String> list)
    {
        ArrayAdapter batchList = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,list);
        batchList.setDropDownViewResource(android.R.layout.simple_spinner_item);
        batches.setAdapter(batchList);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
