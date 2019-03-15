package com.example.auro.Director;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auro.DB.Database;
import com.example.auro.R;
import java.util.List;
import static com.example.auro.DB.Database.createStandard;
import static com.example.auro.DB.Database.createTopic;

public class Add_Standard extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText AddStandard,AddTopics;
    private Spinner selectStd;
    private Button addStd,addTopic;
    private TextView topic;

    private String standards,topics,topicList, stdSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__standard);

        AddStandard =(EditText)findViewById(R.id.std);
        AddTopics =(EditText)findViewById(R.id.topic);
        selectStd =(Spinner)findViewById(R.id.selectStd);
        selectStd.setOnItemSelectedListener(this);
        addStd =(Button)findViewById(R.id.addStd);
        addTopic =(Button)findViewById(R.id.addTopic);
        topic = (TextView)findViewById(R.id.topics);

        getStd();

        addStd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                standards = AddStandard.getText().toString();

                createStandard(standards,getApplicationContext());

                getStd();

            }
        });
        addTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topics = AddTopics.getText().toString();

                createTopic(stdSpinner,topics,getApplicationContext());

            }
        });

    }

    public void getStd()
    {
        Database.getStandards(this, getApplicationContext());
    }

    public void setStandardsSpinner(List<String> list, Context c){
        ArrayAdapter stand = new ArrayAdapter(c, android.R.layout.simple_spinner_item, list);
        stand.setDropDownViewResource(android.R.layout.simple_spinner_item);
        selectStd.setAdapter(stand);

        //project.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        stdSpinner = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
