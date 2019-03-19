package com.example.auro.Director;

import java.io.File;
import java.util.*;
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

import com.example.auro.Adapter.Batch;
import com.example.auro.DB.Database;
import com.example.auro.Project_Manager.Project_Manager_Batch_Report;
import com.example.auro.R;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Director_Batch_Report extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button report;
    Spinner CenterIncharge,ProjectManager,Batches;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    String username,reporting,designation;
    String incharge, manager, batch;
    WritableWorkbook workbook;
    WritableSheet sheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director__batch__report);

        CenterIncharge = findViewById(R.id.centerIncharge);
        ProjectManager = findViewById(R.id.projectManager);
        Batches = findViewById(R.id.batches);
        report = findViewById(R.id.report);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);
        reporting = prefs.getString("Reporting",null);
        designation = prefs.getString("Designation",null);

        CenterIncharge.setOnItemSelectedListener(this);
        ProjectManager.setOnItemSelectedListener(this);
        Batches.setOnItemSelectedListener(this);

        Database.getProjectManagerList(username,this,getApplicationContext());

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(manager.equals("All"))
                {
                    Database.getBatchDetails(Director_Batch_Report.this,getApplicationContext());
                }
                else
                {
                    if(incharge.equals("All"))
                    {
                        Database.getBatchDetails(manager,Director_Batch_Report.this,getApplicationContext());
                    }
                    else
                    {
                        if(batch.equals("All"))
                        {
                            Database.getBatchDetails(incharge,Director_Batch_Report.this);
                        }
                        else
                        {
                            Database.getBatchDetail(batch,Director_Batch_Report.this);
                        }
                    }
                }

            }
        });

    }

    public void setBatchDetailList(List<Batch> list, int flag)
    {
        int listLength = list.size();
        String name;

        File sd = Environment.getExternalStorageDirectory();

        File directory = new File(sd.getAbsolutePath());

        if (!directory.isDirectory()) {
            directory.mkdirs();
        }

        if(flag==1)
        {
            name = incharge;
        }
        else if(flag==2)
        {
            name = manager;
        }
        else
        {
            name = batch;
        }

        try {
            File file = new File(directory, name + ".xls");
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
            finish();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setProjectManagerList(List<String> list)
    {
        ArrayAdapter centerInchargeList = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,list);
        centerInchargeList.setDropDownViewResource(android.R.layout.simple_spinner_item);
        ProjectManager.setAdapter(centerInchargeList);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int ID = parent.getId();

        if(ID == R.id.projectManager)
        {
            manager = parent.getItemAtPosition(position).toString();

            if(manager.equals("All"))
            {
                CenterIncharge.setVisibility(View.GONE);
                Batches.setVisibility(View.GONE);
            }
            else
            {
                CenterIncharge.setVisibility(View.VISIBLE);
                Batches.setVisibility(View.VISIBLE);

                Database.getCenterInchargeList(manager,this,getApplicationContext());
            }
        }
        else if(ID == R.id.centerIncharge)
        {
            incharge = parent.getItemAtPosition(position).toString();

            if(incharge.equals("All"))
            {
                Batches.setVisibility(View.GONE);
            }
            else
            {
                Batches.setVisibility(View.VISIBLE);

                Database.getBatchList(incharge,this,getApplicationContext());
            }
        }
        else if(ID == R.id.batches)
        {
            batch = parent.getItemAtPosition(position).toString();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setCenterInchargeList(List<String> list)
    {
        ArrayAdapter centerInchargeList = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,list);
        centerInchargeList.setDropDownViewResource(android.R.layout.simple_spinner_item);
        CenterIncharge.setAdapter(centerInchargeList);
    }

    public void setBatchList(List<String> list)
    {
        ArrayAdapter batchList = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,list);
        batchList.setDropDownViewResource(android.R.layout.simple_spinner_item);
        Batches.setAdapter(batchList);
    }
}
