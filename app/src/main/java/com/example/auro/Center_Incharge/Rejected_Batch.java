package com.example.auro.Center_Incharge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.auro.DB.Database;
import com.example.auro.R;
import com.example.auro.RecyclerAdapter.PendingStudent_Batch_RecyclerAdapter;
import com.example.auro.RecyclerTouchListener;

import java.util.*;

public class Rejected_Batch extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<String> list = new ArrayList();
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected__batch);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);

        recyclerView  = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        Database.getRejectedBatch(username,this,getApplicationContext());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String data = list.get(position);
                Intent i = new Intent(getApplicationContext(),Rejected_Batch_Details.class);
                i.putExtra("data",data);
                startActivity(i);
            }
        }));
    }

    public void setBatches(List<String> list)
    {
        this.list = list;
        adapter = new PendingStudent_Batch_RecyclerAdapter(list,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
}
