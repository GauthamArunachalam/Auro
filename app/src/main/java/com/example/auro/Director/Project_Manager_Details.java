package com.example.auro.Director;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.auro.Adapter.AssignBatches;
import com.example.auro.Adapter.UserDetails;
import com.example.auro.DB.Database;
import com.example.auro.R;
import com.example.auro.RecyclerAdapter.ProjectManagerLisDetails_RecyclerAdapter;
import com.example.auro.RecyclerAdapter.ProjectManagerList_RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Project_Manager_Details extends AppCompatActivity {

    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private RecyclerView.Adapter adapter1;
    private RecyclerView.Adapter adapter2;
    private List<UserDetails> proman;
    private List<AssignBatches> promanDetials;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project__manager__details);

        username = getIntent().getStringExtra("proman");

        recyclerView1 = findViewById(R.id.projectManagerDetails);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Database.getProjectManagerDetails(username,this,getApplicationContext());

        recyclerView2 = findViewById(R.id.centerinchargelist);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Database.getCenterInchargeList(username, this, getApplicationContext());
    }

    public void setCenterInchargeList(List<UserDetails> list, Context c){

        proman = new ArrayList<>();
        proman = list;
        adapter2 = new ProjectManagerList_RecyclerAdapter(proman,getApplicationContext());
        recyclerView2.setAdapter(adapter2);
    }

    public void setProjectManagerDetails(List<AssignBatches> list, Context c){

        promanDetials = new ArrayList<>();
        promanDetials = list;
        adapter1 = new ProjectManagerLisDetails_RecyclerAdapter(promanDetials,getApplicationContext());
        recyclerView1.setAdapter(adapter1);
    }
}
