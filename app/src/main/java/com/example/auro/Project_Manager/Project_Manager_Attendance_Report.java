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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.auro.Adapter.Attendance_Report;
import com.example.auro.Center_Incharge.Center_Incharge_Attendance_Report;
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

public class Project_Manager_Attendance_Report extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button report;
    private Spinner centerIncharge,batch,date;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    String username, incharge, batches, dates;
    WritableWorkbook workbook;
    WritableSheet sheet;
    EditText filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project__manager__attendance__report);

        report = findViewById(R.id.report);
        centerIncharge = findViewById(R.id.center);
        batch = findViewById(R.id.batch);
        date = findViewById(R.id.date);
        centerIncharge.setOnItemSelectedListener(this);
        batch.setOnItemSelectedListener(this);
        date.setOnItemSelectedListener(this);
        filename = findViewById(R.id.file);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);
        Database.getCenterInchargeList(username,this,getApplicationContext());

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filename.getText().toString().isEmpty()){
                    filename.setError("Enter File name");
                    filename.requestFocus();
                    return;
                }
                if(incharge.equals("All"))
                {
                    Database.getAttendance(username,Project_Manager_Attendance_Report.this, getApplicationContext());
                }
                else
                {
                    Database.getAttendance(incharge,batches,dates, Project_Manager_Attendance_Report.this,getApplicationContext());
                }
            }
        });
    }

    public void setAttendance(List<Attendance_Report> list)
    {
        int listLength = list.size();

        File sd = Environment.getExternalStorageDirectory();

        File directory = new File(sd.getAbsolutePath());

        if (!directory.isDirectory()) {
            directory.mkdirs();
        }

        try {
            File file = new File(directory, filename.getText().toString() + ".xls");
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));

            workbook = Workbook.createWorkbook(file, wbSettings);

            sheet = workbook.createSheet("Report", 0);
            sheet.addCell(new Label(0,0,"Batch"));
            sheet.addCell(new Label(1,0,"Student Name"));
            sheet.addCell(new Label(2,0,"Date"));
            sheet.addCell(new Label(3,0,"Status"));

            for(int inc=0 ; inc<listLength ; inc++)
            {
                Attendance_Report ar = list.get(inc);

                sheet.addCell(new Label(0,inc+1,ar.getBatch()));
                sheet.addCell(new Label(1,inc+1,ar.getId()));
                sheet.addCell(new Label(2,inc+1,ar.getDate()));
                String st = ar.getStat();
                if(st.equals("1"))
                {
                    st = "present";
                }
                else
                {
                    st = "absent";
                }
                sheet.addCell(new Label(3,inc+1,st));
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
        ArrayAdapter centerList = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,list);
        centerList.setDropDownViewResource(android.R.layout.simple_spinner_item);
        centerIncharge.setAdapter(centerList);
    }

    public void setBatchList(List<String> list)
    {
        ArrayAdapter batchList = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,list);
        batchList.setDropDownViewResource(android.R.layout.simple_spinner_item);
        batch.setAdapter(batchList);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int ID = parent.getId();

        if(ID == R.id.center)
        {
            incharge = parent.getItemAtPosition(position).toString();

            if(incharge.equals("All"))
            {
                batch.setVisibility(View.GONE);
                date.setVisibility(View.GONE);
            }
            else
            {
                batch.setVisibility(View.VISIBLE);
                date.setVisibility(View.VISIBLE);
                Database.getBatchList(incharge,this,getApplicationContext());
            }
        }
        if(ID == R.id.batch)
        {
            batches = parent.getItemAtPosition(position).toString();

            if(batches.equals("All"))
            {
                date.setVisibility(View.GONE);
            }
            else
            {
                Database.getDateList(batches,this,getApplicationContext());
                date.setVisibility(View.VISIBLE);
            }
        }
        if(ID == R.id.date)
        {
            dates = parent.getItemAtPosition(position).toString();
        }
    }

    public  void setDateList(List<String> list)
    {
        ArrayAdapter dateList = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,list);
        dateList.setDropDownViewResource(android.R.layout.simple_spinner_item);
        date.setAdapter(dateList);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
