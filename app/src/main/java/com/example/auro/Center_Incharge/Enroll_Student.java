package com.example.auro.Center_Incharge;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.auro.Adapter.Batch;
import com.example.auro.DB.Database;
import com.example.auro.R;
import com.example.auro.RecyclerAdapter.PendingBatch_RecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Enroll_Student extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    EditText studentID,studentName,fatherName,motherName,addr;
    Button date,forward,sphoto,tphoto;
    RadioGroup gender,fatherStatus,motherStatus;
    Spinner spinnerbatch;
    TextView standard;
    String stdID,stdName,fN,mN,fS,mS,stdaddress,birthDate,stdgender,batch,std;
    Uri filePath;

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public String username,reporting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll__student);

        studentID = findViewById(R.id.studentID);
        studentName = findViewById(R.id.stdname);
        fatherName = findViewById(R.id.fathername);
        motherName = findViewById(R.id.mothername);
        addr = findViewById(R.id.address);
        gender = findViewById(R.id.gender);
        fatherStatus = findViewById(R.id.fatherStatus);
        motherStatus = findViewById(R.id.motherStatus);
        spinnerbatch = findViewById(R.id.batch);
        spinnerbatch.setOnItemSelectedListener(this);
        sphoto = findViewById(R.id.sphoto);
        tphoto = findViewById(R.id.tphoto);
        forward = findViewById(R.id.forward);
        standard = findViewById(R.id.std);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);
        reporting = prefs.getString("Reporting",null);

        Database.getBatches(username,this,getApplicationContext());

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton stdgen = group.findViewById(checkedId);
                stdgender = stdgen.getText().toString();

            }
        });

        fatherStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton fstat = group.findViewById(checkedId);
                fS = fstat.getText().toString();
            }
        });

        motherStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton gstat = group.findViewById(checkedId);
                mS = gstat.getText().toString();
            }
        });

        sphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        tphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String name = studentID.getText().toString();
                File image = new File(dir,name);
                Uri pic = Uri.fromFile(image);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,pic);
                startActivityForResult(intent,2);
            }
        });

        date = findViewById(R.id.dob);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker = new com.example.auro.Pickers.DatePicker();
                datepicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
    }

    public void upload(){
        stdID = studentID.getText().toString();
        Database.uploadDP(filePath,stdID,this,getApplicationContext());
    }

    public void setUrl(String url)
    {
        stdID = studentID.getText().toString();
        stdName = studentName.getText().toString();
        stdaddress = addr.getText().toString();
        birthDate = date.getText().toString();
        fN = fatherName.getText().toString();
        mN = motherName.getText().toString();

        Database.enrollStudent(stdID,stdName,fN,fS,mN,mS,stdgender,batch,std,reporting,birthDate,stdaddress,url,getApplicationContext());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month++;
        String dates = dayOfMonth+"-"+month+"-"+year;

        date.setText(dates);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1 && data != null) {
            filePath = data.getData();
        }
        else if (requestCode == 2 && data != null) {
            filePath = data.getData();
        }
    }

    public void setBatch(List<String> list, Context c){
        ArrayAdapter batchList = new ArrayAdapter(c,android.R.layout.simple_spinner_item,list);
        batchList.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerbatch.setAdapter(batchList);

    }

    public void setStandard(String std)
    {
        standard.setText(std);
        this.std = std;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        batch = parent.getItemAtPosition(position).toString();

        Database.getStandard(batch, this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
