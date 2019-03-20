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

public class Center_Incharge_Batch_Report extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner batchList;
    private Button report;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    String username,reporting,designation,batch;
    WritableWorkbook workbook;
    WritableSheet sheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center__incharge__batch__report);

        batchList = findViewById(R.id.batchlist);
        batchList.setOnItemSelectedListener(this);
        report = findViewById(R.id.report);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);
        reporting = prefs.getString("Reporting",null);
        designation = prefs.getString("Designation",null);

        Database.getBatchList(username,this,getApplicationContext());

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database.getSelectedCenterBatch(batch,username, Center_Incharge_Batch_Report.this,getApplicationContext());
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        batch = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setBatch(List<String> list)
    {
        ArrayAdapter listBatch = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,list);
        listBatch.setDropDownViewResource(android.R.layout.simple_spinner_item);
        batchList.setAdapter(listBatch);
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

            sheet.addCell(new Label(0, 0, "Batch Name"));
            sheet.addCell(new Label(1, 0, "Start Date"));
            sheet.addCell(new Label(2, 0, "End Date"));
            sheet.addCell(new Label(3, 0, "Start Time"));
            sheet.addCell(new Label(4, 0, "End Time"));
            sheet.addCell(new Label(5, 0, "Days"));
            sheet.addCell(new Label(6, 0, "Standard"));
            sheet.addCell(new Label(7, 0, "Student Limit"));
            sheet.addCell(new Label(8, 0, "Incharge"));

            for(int inc = 0; inc<listLength ; inc++)
            {
                Batch b = list.get(inc);

                sheet.addCell(new Label(0, inc+1, b.getBatch_name()));
                sheet.addCell(new Label(1, inc+1, b.getStart_date()));
                sheet.addCell(new Label(2, inc+1, b.getEnd_date()));
                sheet.addCell(new Label(3, inc+1, b.getStart_time()));
                sheet.addCell(new Label(4, inc+1, b.getEnd_time()));
                sheet.addCell(new Label(5, inc+1, b.getDays()));
                sheet.addCell(new Label(6, inc+1, b.getStandard()));
                sheet.addCell(new Label(7, inc+1, b.getStd_limit()));
                sheet.addCell(new Label(8, inc+1, b.getIncharge()));
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
}
