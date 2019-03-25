package com.example.auro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.auro.Adapter.Message;
import com.example.auro.DB.Database;
import com.example.auro.RecyclerAdapter.Chat_List_RecyclerAdapter;
import com.example.auro.RecyclerAdapter.Chat_RecyclerAdapter;

import java.util.*;

public class Private_message extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Message>list = new ArrayList<>();
    private EditText message ;
    private ImageView send;
    private String msg,from,to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_message);

        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");

        recyclerView = findViewById(R.id.recyclerView);
        message = findViewById(R.id.chat);
        send = findViewById(R.id.send);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Database.getChat(from,to,this,getApplicationContext());

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = message.getText().toString();
                if(msg.isEmpty())
                {
                    return;
                }
                Database.chat(msg,from,to);
                message.setText("");
            }
        });

    }

    public void setChat(List<Message> list)
    {
        this.list = list;
        adapter = new Chat_RecyclerAdapter(list,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
}
