package com.example.auro.Director;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auro.Adapter.Attendance_Report;
import com.example.auro.Adapter.StudentDetails;
import com.example.auro.DB.Database;
import com.example.auro.Project_Manager.Project_Manager_Attendance_Report;
import com.example.auro.R;

import java.io.File;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Director_Attendance_Report extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    Spinner projectManager,centerInchsrge,batch,date;
    Button report;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    String username, manager, incharge, batches, dates, startdate="start",enddate="end";
    WritableWorkbook workbook;
    WritableSheet sheet;
    EditText filename;
    TextView start,end;
    ImageView sdate,edate;
    int flagD = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director__attendance__report);

        projectManager = findViewById(R.id.projectManager);
        centerInchsrge = findViewById(R.id.centerIncharge);
        batch = findViewById(R.id.batch);
        date = findViewById(R.id.date);
        report = findViewById(R.id.report);
        projectManager.setOnItemSelectedListener(this);
        centerInchsrge.setOnItemSelectedListener(this);
        batch.setOnItemSelectedListener(this);
        date.setOnItemSelectedListener(this);
        filename = findViewById(R.id.file);
        start = findViewById(R.id.Start_Date);
        end = findViewById(R.id.end_Date);
        sdate = findViewById(R.id.start_date);
        edate = findViewById(R.id.end_date);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);

        Database.getProjectManagerList(username,this,getApplicationContext());

        sdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagD = 0;
                DialogFragment datepicker = new com.example.auro.Pickers.DatePicker();
                datepicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagD = 1;
                DialogFragment datepicker = new com.example.auro.Pickers.DatePicker();
                datepicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filename.getText().toString().isEmpty()){
                    filename.setError("Enter File name");
                    filename.requestFocus();
                    return;
                }
                if(manager.equals("All"))
                {
                    Database.getAttendance(startdate,enddate,Director_Attendance_Report.this,getApplicationContext());
                }
                else
                {
                    if(incharge.equals("All"))
                    {
                        Database.getAttendance(startdate,enddate,username, Director_Attendance_Report.this, getApplicationContext());
                    }
                    else
                    {
                        Database.getAttendance(startdate,enddate,incharge,batches,dates, Director_Attendance_Report.this,getApplicationContext());
                    }
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

    public void setProjectManagerList(List<String> list)
    {
        ArrayAdapter project = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,list);
        project.setDropDownViewResource(android.R.layout.simple_spinner_item);
        projectManager.setAdapter(project);
    }

    public void setCenterInchargeList(List<String> list)
    {
        ArrayAdapter center = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,list);
        center.setDropDownViewResource(android.R.layout.simple_spinner_item);
        centerInchsrge.setAdapter(center);
    }

    public void setBatchList(List<String> list)
    {
        ArrayAdapter batchList = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,list);
        batchList.setDropDownViewResource(android.R.layout.simple_spinner_item);
        batch.setAdapter(batchList);
    }

    public  void setDateList(List<String> list)
    {
        ArrayAdapter dateList = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,list);
        dateList.setDropDownViewResource(android.R.layout.simple_spinner_item);
        date.setAdapter(dateList);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int ID = parent.getId();

        if(ID == R.id.projectManager)
        {
            manager = parent.getItemAtPosition(position).toString();

            if(manager.equals("All"))
            {
                centerInchsrge.setVisibility(View.GONE);
                batch.setVisibility(View.GONE);
                date.setVisibility(View.GONE);
            }
            else
            {
                centerInchsrge.setVisibility(View.VISIBLE);
                batch.setVisibility(View.VISIBLE);
                date.setVisibility(View.VISIBLE);

                Database.getCenterInchargeList(manager,this,getApplicationContext());
            }
        }
        else if(ID == R.id.centerIncharge)
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
        else if(ID == R.id.batch)
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
        else if(ID == R.id.date)
        {
            dates = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month++;
        String date = dayOfMonth+"-"+month+"-"+year;

        if(flagD == 0)
        {
            start.setText(date);
            startdate = date;
        }
        else if(flagD == 1)
        {
            end.setText(date);
            enddate = date;
        }
    }
}
