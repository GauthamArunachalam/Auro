package com.example.auro.Project_Manager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.auro.Adapter.StudentDetails;
import com.example.auro.DB.Database;
import com.example.auro.R;
import com.example.auro.RecyclerAdapter.PendingStudent_Batch_RecyclerAdapter;
import com.example.auro.RecyclerAdapter.PendingStudent_RecyclerAdapter;

import java.util.*;

public class Request_Student_list extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private StudentDetails studentDetails;
    private List<StudentDetails> list;
    String batch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request__student_list);

        batch = getIntent().getStringExtra("batch");

        Database.getStudentDetails(batch,this,getApplicationContext());

        recyclerView = findViewById(R.id.stdList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        list = new ArrayList<>();
    }

    public void setStudentDetails(List<StudentDetails> list)
    {
        this.list = list;
        adapter = new PendingStudent_RecyclerAdapter(list,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
}
