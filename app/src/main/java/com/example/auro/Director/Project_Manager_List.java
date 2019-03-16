package com.example.auro.Director;

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

import com.example.auro.Adapter.UserDetails;
import com.example.auro.DB.Database;
import com.example.auro.R;
import com.example.auro.RecyclerAdapter.ProjectManagerList_RecyclerAdapter;
import com.example.auro.RecyclerTouchListener;

public class Project_Manager_List extends AppCompatActivity  {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<UserDetails> proman;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project__manager__list);


        recyclerView = findViewById(R.id.promanList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);

        Database.getProjectManagerList(username, this, getApplicationContext());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                UserDetails userDetails = proman.get(position);
                String proman = userDetails.getName();
                Intent i = new Intent(getApplicationContext(),Project_Manager_Details.class);
                i.putExtra("proman",proman);
                startActivity(i);

            }
        }));
    }

    public void setProjectManagerList(List<UserDetails> list, Context c){

        proman = new ArrayList<>();
        proman = list;
        adapter = new ProjectManagerList_RecyclerAdapter(proman,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }


}
