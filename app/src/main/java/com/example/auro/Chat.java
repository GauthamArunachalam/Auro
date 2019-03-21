package com.example.auro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.auro.Adapter.Message;
import com.example.auro.Adapter.UserDetails;
import com.example.auro.DB.Database;
import com.example.auro.RecyclerAdapter.Chat_List_RecyclerAdapter;

import java.util.*;

public class Chat extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<UserDetails>list = new ArrayList<>();
    private Message message;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public String username,designation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);
        designation = prefs.getString("Designation",null);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Database.getUserList(username,designation,this,getApplicationContext());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                UserDetails u = list.get(position);
                startActivity(new Intent(getApplicationContext(), Private_message.class).putExtra("from",username).putExtra("to",u.getName()));

            }
        }));

    }

    public void setUserList(List<UserDetails> list)
    {
        this.list = list;
        adapter = new Chat_List_RecyclerAdapter(list,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
}
