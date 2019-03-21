package com.example.auro.Project_Manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.auro.DB.Database;
import com.example.auro.R;
import com.example.auro.RecyclerAdapter.PendingStudent_Batch_RecyclerAdapter;

import java.util.*;

public class Center_Incharge_List extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<String>list = new ArrayList<>();
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center__incharge__list);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);

        Database.getCenterInchargeList(username,Center_Incharge_List.this,getApplicationContext());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
    }

    public void setCenterInchargeList(List<String> list, Context c)
    {
        this.list = list;
        adapter = new PendingStudent_Batch_RecyclerAdapter(list,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
}
