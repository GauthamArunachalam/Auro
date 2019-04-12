package com.example.auro.Project_Manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.auro.Adapter.Status;
import com.example.auro.Adapter.StudentDetails;
import com.example.auro.DB.Database;
import com.example.auro.R;
import com.example.auro.RecyclerAdapter.PendingStudent_Batch_RecyclerAdapter;
import com.example.auro.RecyclerAdapter.PendingStudent_RecyclerAdapter;
import com.example.auro.RecyclerTouchListener;

import java.util.*;

public class Request_Student_list extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private StudentDetails studentDetails;
    private List<StudentDetails> list = new ArrayList<>();
    private List<Status> list2 = new ArrayList<>();
    String batch,remarks;
    Button select, accept, reject, escalate;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public String username,reporting,designation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request__student_list);

        batch = getIntent().getStringExtra("batch");

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);
        reporting = prefs.getString("Reporting",null);
        designation = prefs.getString("Designation",null);

        select = findViewById(R.id.select);
        escalate = findViewById(R.id.escalate);
        accept = findViewById(R.id.accept);
        reject = findViewById(R.id.reject);

        if(designation.equals("Project Manager"))
        {
            escalate.setVisibility(View.VISIBLE);
        }

        Database.getStudentDetails(batch,username,this,getApplicationContext());

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int xd = adapter.getItemCount();

                for(int x=0; x<xd; x++)
                {
                    Status up = list2.get(x);
                    int con = up.getStatus();

                    if(con==0)
                    {
                        Status s = new Status();
                        s.setStatus(1);
                        list2.set(x,s);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int xd = adapter.getItemCount();

                for(int x=0; x<xd; x++)
                {
                    Status up1 = list2.get(x);
                    int con = up1.getStatus();

                    StudentDetails up = list.get(x);

                    if(con==1)
                    {
                        Database.approveStudent(up.getStudentID(),getApplicationContext(),Request_Student_list.this);
                    }
                }
            }
        });

        escalate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int xd = adapter.getItemCount();

                for(int x=0; x<xd; x++)
                {
                    Status up1 = list2.get(x);
                    int con = up1.getStatus();

                    StudentDetails up = list.get(x);

                    if(con==1)
                    {
                        Database.escalateStudent(up.getStudentID(),reporting,getApplicationContext(),Request_Student_list.this);
                    }
                }
            }
        });

        recyclerView = findViewById(R.id.stdList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Request_Student_list.this);
                builder.setTitle("Reason for rejecting");
                final EditText remark = new EditText(Request_Student_list.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                remark.setLayoutParams(lp);
                builder.setView(remark);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        remarks = remark.getText().toString();
                        rejectstudent();
                    }
                });
                builder.show();
            }
        });

    }

    public void setStudentDetails(List<StudentDetails> list, List<Status> list2)
    {
        this.list = list;
        this.list2 = list2;
        adapter = new PendingStudent_RecyclerAdapter(list,list2,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    public void rejectstudent(){
        int xd = adapter.getItemCount();

        for(int x=0; x<xd; x++)
        {
            Status up1 = list2.get(x);
            int con = up1.getStatus();

            StudentDetails up = list.get(x);

            if(con==1)
            {
                Database.rejectStudent(up.getStudentID(),remarks,getApplicationContext(),Request_Student_list.this);
            }
        }
    }
}
