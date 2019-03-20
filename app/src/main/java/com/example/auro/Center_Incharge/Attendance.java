package com.example.auro.Center_Incharge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.*;

import com.example.auro.Adapter.Batch;
import com.example.auro.DB.Database;
import com.example.auro.R;
import com.example.auro.RecyclerAdapter.PendingStudent_Batch_RecyclerAdapter;
import com.example.auro.RecyclerTouchListener;

public class Attendance extends AppCompatActivity {
    private RecyclerView recyclerView ;
    private RecyclerView.Adapter adapter;
    private List<String> list;
    private TextView tDate;
    private TextView day;
    private String today,todaysDay;
    private Calendar currentDate;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private String username;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tDate = findViewById(R.id.tDate);
        day = findViewById(R.id.day);

        SharedPreferences preferences = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        username = preferences.getString("UserName",null);

        try{
            Date td = Calendar.getInstance().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            today = dateFormat.format(td);
            tDate.setText(today);
            Date dts = dateFormat.parse(today);

            currentDate = new GregorianCalendar();
            currentDate.setTime(dts);

            int x = currentDate.getTime().getDay();

            if (x == 0) {
                day.setText("Sunday");
                todaysDay = "Sunday";
            } else if (x == 1) {
                day.setText("Monday");
                todaysDay = "Monday";
            } else if (x == 2) {
                day.setText("Tuesday");
                todaysDay = "Tuesday";
            } else if (x == 3) {
                day.setText("Wednesday");
                todaysDay = "Wednesday";
            } else if (x == 4) {
                day.setText("Thursday");
                todaysDay = "Thursday";
            } else if (x == 5) {
                day.setText("Friday");
                todaysDay = "Friday";
            } else if (x == 6) {
                day.setText("Saturday");
                todaysDay = "Saturday";
            }
        }catch (Exception e){
            System.out.println(e);
        }

        Database.getBatchList(username,todaysDay,currentDate,this,getApplicationContext());

        list = new ArrayList<>();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String batch = list.get(position);
                Intent intent = new Intent(getApplicationContext(),Attendance_Student_List.class);
                intent.putExtra("name",batch);
                intent.putExtra("date",today);
                startActivity(intent);
            }
        }));

    }

    public void setBatchList(List<String> list, Context c)
    {
        this.list = list;
        adapter = new PendingStudent_Batch_RecyclerAdapter(list,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
}
