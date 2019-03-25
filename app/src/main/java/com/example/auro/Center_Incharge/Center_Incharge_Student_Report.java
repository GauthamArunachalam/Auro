package com.example.auro.Center_Incharge;

import android.content.SharedPreferences;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.*;

import com.example.auro.Adapter.Batch;
import com.example.auro.Adapter.StudentDetails;
import com.example.auro.DB.Database;
import com.example.auro.R;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Center_Incharge_Student_Report extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner batch,gender;
    String gens[] = {"Both","Male","Female"};
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    String username,batchs,gend;
    Button report;
    WritableWorkbook workbook;
    WritableSheet sheet;
    EditText filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center__incharge__student__report);
        batch = findViewById(R.id.batch);
        report = findViewById(R.id.report);
        gender = findViewById(R.id.gender);
        batch.setOnItemSelectedListener(this);
        gender.setOnItemSelectedListener(this);
        filename = findViewById(R.id.file);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);

        Database.getBatchList(username,this,getApplicationContext());

        ArrayAdapter gen = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,gens);
        gen.setDropDownViewResource(android.R.layout.simple_spinner_item);
        gender.setAdapter(gen);

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filename.getText().toString().isEmpty()){
                    filename.setError("Enter File name");
                    filename.requestFocus();
                    return;
                }
                Database.getStudentDetails(batchs,username,gend,Center_Incharge_Student_Report.this,getApplicationContext());
            }
        });

    }

    public  void setBatch(List<String> list)
    {
        ArrayAdapter batchList = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,list);
        batchList.setDropDownViewResource(android.R.layout.simple_spinner_item);
        batch.setAdapter(batchList);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int ID = parent.getId();

        if(ID == R.id.batch)
        {
            batchs = parent.getItemAtPosition(position).toString();
        }
        else
        {
            gend  = parent.getItemAtPosition(position).toString();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
            File file = new File(directory, batchs + ".xls");
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
}
