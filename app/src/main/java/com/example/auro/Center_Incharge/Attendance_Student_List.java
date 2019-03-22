package com.example.auro.Center_Incharge;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.*;

import com.example.auro.Adapter.Status;
import com.example.auro.Adapter.StudentDetails;
import com.example.auro.DB.Database;
import com.example.auro.R;
import com.example.auro.RecyclerAdapter.Attendance_RecyclerAdapter;
import com.example.auro.RecyclerAdapter.PendingStudent_Batch_RecyclerAdapter;
import com.example.auro.RecyclerTouchListener;

public class Attendance_Student_List extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private StudentDetails studentDetails;
    private List<String> list = new ArrayList<>();
    private List<Status> list2 = new ArrayList<>();
    private RadioGroup group;
    private RadioButton button;
    private Button submit;
    private EditText others;
    private Spinner problem,topics;
    private String batch_name,date,topic_covered,status;
    private String problemList[] = {"Select","Electricity Shutdown","Local Function","System Crash","Network Issue","Others"};
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public String username,reporting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance__student__list);

        recyclerView = findViewById(R.id.attendanceStudentList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        submit = findViewById(R.id.submit);
        group = findViewById(R.id.group);
        others = findViewById(R.id.others);
        problem = findViewById(R.id.problem);
        topics = findViewById(R.id.topics);
        problem.setOnItemSelectedListener(this);
        topics.setOnItemSelectedListener(this);


        batch_name = getIntent().getStringExtra("name");
        date = getIntent().getStringExtra("date");

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);
        reporting = prefs.getString("Reporting",null);

        Database.getTopics(batch_name,this,getApplicationContext());

        Database.getStudentID(batch_name,this,getApplicationContext());

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                button = group.findViewById(checkedId);
                status = button.getText().toString();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        check(v);
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Status up = list2.get(position);
                int x = up.getStatus();

                if(x==0)
                {
                    Status s = new Status();
                    s.setStatus(1);
                    list2.set(position,s);
                    recyclerView.setAdapter(adapter);
                }
                else
                {
                    Status s = new Status();
                    s.setStatus(0);
                    list2.set(position,s);
                    recyclerView.setAdapter(adapter);
                }
            }
        }));
        ArrayAdapter pbl = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,problemList);
        pbl.setDropDownViewResource(android.R.layout.simple_spinner_item);
        problem.setAdapter(pbl);



    }

    public void setTopics(List<String> list)
    {
        ArrayAdapter topic = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,list);
        topic.setDropDownViewResource(android.R.layout.simple_spinner_item);
        topics.setAdapter(topic);
    }

    public void setStudentList(List<String> list, List<Status> list2, Context c)
    {
        this.list2 = list2;
        this.list = list;
        adapter = new Attendance_RecyclerAdapter(list,list2,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        int ID = parent.getId();

        String data = parent.getItemAtPosition(position).toString();

        if(ID == R.id.topics)
        {
            topic_covered = data;
        }
        if(ID == R.id.problem)
        {
            if(!data.equals("Select"))
            {
               status = data;
               if(data.equals("Others"))
               {
                   others.setVisibility(View.VISIBLE);
               }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void check(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure do you want to submit");
        builder.setMessage("Attendance can not be modified once submitted!!!");
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(status.equals("Others"))
                {
                    status = others.getText().toString();
                }

                Database.putAttendance(batch_name,date,list,list2,status,topic_covered,username,reporting,getApplicationContext());


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
