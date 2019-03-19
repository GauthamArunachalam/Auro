package com.example.auro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.*;

import com.example.auro.Adapter.Batch;
import com.example.auro.DB.Database;
import com.example.auro.Director.Director_Request_Details;
import com.example.auro.Project_Manager.Request_Batch_Details;
import com.example.auro.Project_Manager.Request_Student_list;
import com.example.auro.RecyclerAdapter.PendingBatch_RecyclerAdapter;
import com.example.auro.RecyclerAdapter.PendingStudent_Batch_RecyclerAdapter;

public class Pending_Student_Batch_List extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<String> list;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public String username,designation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending__student__batch__list);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);
        designation = prefs.getString("Designation",null);

        recyclerView = findViewById(R.id.batches);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Database.getBatchList(username,this,getApplicationContext());

        list = new ArrayList<>();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String batch = list.get(position);

                Intent intent = new Intent(getApplicationContext(), Request_Student_list.class);
                intent.putExtra("batch",batch);
                startActivity(intent);
            }
        }));

    }

    public void setBatch(List<String> list){

        this.list = list;
        adapter = new PendingStudent_Batch_RecyclerAdapter(list,getApplicationContext());
        recyclerView.setAdapter(adapter);

    }
}
