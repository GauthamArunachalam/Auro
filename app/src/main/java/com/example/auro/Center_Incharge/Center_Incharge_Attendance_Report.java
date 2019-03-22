package com.example.auro.Center_Incharge;

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

import com.example.auro.Adapter.Attendance_Report;
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

public class Center_Incharge_Attendance_Report extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner batch,dates;
    private Button report;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    String username,batchs,date;
    WritableWorkbook workbook;
    WritableSheet sheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center__incharge__attendance__report);

        batch = findViewById(R.id.batch);
        dates = findViewById(R.id.date);
        report = findViewById(R.id.report);
        batch.setOnItemSelectedListener(this);
        dates.setOnItemSelectedListener(this);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);

        Database.getBatchList(username,this,getApplicationContext());

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database.getAttendance(username,batchs,date,Center_Incharge_Attendance_Report.this,getApplicationContext());
            }
        });
    }

    public  void setBatch(List<String> list)
    {
        ArrayAdapter batchList = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,list);
        batchList.setDropDownViewResource(android.R.layout.simple_spinner_item);
        batch.setAdapter(batchList);
    }

    public  void setDateList(List<String> list)
    {
        ArrayAdapter dateList = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,list);
        dateList.setDropDownViewResource(android.R.layout.simple_spinner_item);
        dates.setAdapter(dateList);
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
            File file = new File(directory, batchs + ".xls");
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int ID = parent.getId();

        if(ID == R.id.batch)
        {
            batchs = parent.getItemAtPosition(position).toString();
            if(batchs.equals("All"))
            {
                dates.setVisibility(View.GONE);
            }
            else
            {
                Database.getDateList(batchs,this,getApplicationContext());
                dates.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            date  = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
