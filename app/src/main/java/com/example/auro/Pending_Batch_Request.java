package com.example.auro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.*;

import com.example.auro.Adapter.Batch;
import com.example.auro.Adapter.UserDetails;
import com.example.auro.DB.Database;
import com.example.auro.Director.Director_Request_Details;
import com.example.auro.Director.Project_Manager_Details;
import com.example.auro.Project_Manager.Request_Batch_Details;
import com.example.auro.RecyclerAdapter.PendingBatch_RecyclerAdapter;

public class Pending_Batch_Request extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Batch batch;
    private List<Batch>batchList;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public String username,designation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending__batch__request);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);
        designation = prefs.getString("Designation",null);

        recyclerView = findViewById(R.id.batchList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Database.getBatches(username, this, getApplicationContext());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Batch batch = batchList.get(position);
                String bt = batch.getBatch_name();

                if(designation.equals("Project Manager"))
                {
                    Intent i = new Intent(getApplicationContext(), Request_Batch_Details.class);
                    i.putExtra("name",bt);
                    startActivity(i);
                }
                else if(designation.equals("Director"))
                {
                    Intent i = new Intent(getApplicationContext(), Director_Request_Details.class);
                    i.putExtra("name",bt);
                    startActivity(i);
                }
            }
        }));
    }

    public void setBatch(List<Batch> list, Context c){
        batchList = new ArrayList<>();
        batchList = list;
        adapter = new PendingBatch_RecyclerAdapter(batchList,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
}
