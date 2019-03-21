package com.example.auro.Director;

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

import com.example.auro.Adapter.StudentDetails;
import com.example.auro.DB.Database;
import com.example.auro.R;

import java.io.File;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Director_Student_Report extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner batch,gender,center,project;
    String gens[] = {"Both","Male","Female"};
    private Button report;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    String username,manager,incharge,batches,gende;
    WritableWorkbook workbook;
    WritableSheet sheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director__student__report);

        batch = findViewById(R.id.batch);
        gender = findViewById(R.id.gender);
        center = findViewById(R.id.center);
        report = findViewById(R.id.report);
        project = findViewById(R.id.project);
        batch.setOnItemSelectedListener(this);
        gender.setOnItemSelectedListener(this);
        center.setOnItemSelectedListener(this);
        project.setOnItemSelectedListener(this);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);

        Database.getProjectManagerList(username,this,getApplicationContext());

        ArrayAdapter gen = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,gens);
        gen.setDropDownViewResource(android.R.layout.simple_spinner_item);
        gender.setAdapter(gen);

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(manager.equals("All"))
                {
                    Database.getStudentDetails(gende,Director_Student_Report.this,getApplicationContext());
                }
                else
                {
                    if(incharge.equals("All"))
                    {
                        Database.getStudentDetails(manager,gende,Director_Student_Report.this,getApplicationContext());
                    }
                    else
                    {
                        Database.getStudentDetail(batches,incharge,gende,Director_Student_Report.this,getApplicationContext());
                    }
                }

            }
        });

    }

    public void setStudentDetails(List<StudentDetails> list)
    {
        int listLength = list.size();

        File sd = Environment.getExternalStorageDirectory();

        File directory = new File(sd.getAbsolutePath());

        if (!directory.isDirectory()) {
            directory.mkdirs();
        }

        try {
            File file = new File(directory, manager + ".xls");
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));

            workbook = Workbook.createWorkbook(file, wbSettings);

            sheet = workbook.createSheet("Report", 0);

            sheet.addCell(new Label(0, 0, "Student ID"));
            sheet.addCell(new Label(1, 0, "Student Name"));
            sheet.addCell(new Label(2, 0, "Standard"));
            sheet.addCell(new Label(3, 0, "Gender"));
            sheet.addCell(new Label(4, 0, "Batch"));
            sheet.addCell(new Label(5, 0, "DOB"));
            sheet.addCell(new Label(6, 0, "Father Name"));
            sheet.addCell(new Label(7, 0, "Father Status"));
            sheet.addCell(new Label(8, 0, "Mother Name"));
            sheet.addCell(new Label(9, 0, "Mother Status"));
            sheet.addCell(new Label(10, 0, "Address"));


            for(int inc = 0; inc<listLength ; inc++)
            {
                StudentDetails b = list.get(inc);

                sheet.addCell(new Label(0, inc+1, b.getStudentID()));
                sheet.addCell(new Label(1, inc+1, b.getStudentName()));
                sheet.addCell(new Label(2, inc+1, b.getStandard()));
                sheet.addCell(new Label(3, inc+1, b.getGender()));
                sheet.addCell(new Label(4, inc+1, b.getBatch()));
                sheet.addCell(new Label(5, inc+1, b.getDob()));
                sheet.addCell(new Label(6, inc+1, b.getFatherName()));
                sheet.addCell(new Label(7, inc+1, b.getFatherStatus()));
                sheet.addCell(new Label(8, inc+1, b.getMotherName()));
                sheet.addCell(new Label(9, inc+1, b.getMotherStatus()));
                sheet.addCell(new Label(10, inc+1, b.getAddress()));
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

    public void setProjectManagerList(List<String> list)
    {
        ArrayAdapter centerInchargeList = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,list);
        centerInchargeList.setDropDownViewResource(android.R.layout.simple_spinner_item);
        project.setAdapter(centerInchargeList);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int ID = parent.getId();

        if(ID == R.id.project)
        {
            manager = parent.getItemAtPosition(position).toString();

            if(manager.equals("All"))
            {
                center.setVisibility(View.GONE);
                batch.setVisibility(View.GONE);
            }
            else
            {
                center.setVisibility(View.VISIBLE);
                batch.setVisibility(View.VISIBLE);

                Database.getCenterInchargeList(manager,this,getApplicationContext());
            }
        }
        else if(ID == R.id.center)
        {
            incharge = parent.getItemAtPosition(position).toString();

            if(incharge.equals("All"))
            {
                batch.setVisibility(View.GONE);
            }
            else
            {
                batch.setVisibility(View.VISIBLE);

                Database.getBatchList(incharge,this,getApplicationContext());
            }
        }
        else if(ID == R.id.batch)
        {
            batches = parent.getItemAtPosition(position).toString();
        }
        else if(ID == R.id.gender)
        {
            gende = parent.getItemAtPosition(position).toString();
        }
    }

    public void setCenterInchargeList(List<String> list)
    {
        ArrayAdapter centerInchargeList = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,list);
        centerInchargeList.setDropDownViewResource(android.R.layout.simple_spinner_item);
        center.setAdapter(centerInchargeList);
    }

    public void setBatchList(List<String> list)
    {
        ArrayAdapter batchList = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,list);
        batchList.setDropDownViewResource(android.R.layout.simple_spinner_item);
        batch.setAdapter(batchList);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
