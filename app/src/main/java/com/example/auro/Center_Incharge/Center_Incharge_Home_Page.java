package com.example.auro.Center_Incharge;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.*;

import com.example.auro.Approved_Batch;
import com.example.auro.Approved_Students;
import com.example.auro.Chat;
import com.example.auro.DB.Database;
import com.example.auro.R;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class Center_Incharge_Home_Page extends Fragment implements AdapterView.OnItemSelectedListener {
    private CardView EnrollStudent,CreateBatch,Report,attendance,chat,rejecttedBatch;
    private String problemList[] = {"Select","Electricity Shutdown","Local Function","System Crash","Network Issue","Others"};
    private String today,todaysDay;
    private Calendar currentDate;
    private TextView day,tDate,name,manager,batchcount,todaybatch,stdcount;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public String username,reporting;
    private Button approvedStudent,approvedBatches;
    private final String CHANNEL_ID = "personal_notifications";
    private final int NOTIFICATION_ID = 001;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.center_incharge_home_page, container, false);

        EnrollStudent = view.findViewById(R.id.es);
        CreateBatch = view.findViewById(R.id.cb);
        Report = view.findViewById(R.id.report);
        tDate = view.findViewById(R.id.date);
        stdcount = view.findViewById(R.id.stud);
        batchcount = view.findViewById(R.id.batchcount);
        name = view.findViewById(R.id.userName);
        todaybatch = view.findViewById(R.id.todaybatch);
        manager = view.findViewById(R.id.reporting);
        day = view.findViewById(R.id.day);
        attendance = view.findViewById(R.id.attendance);
        chat = view.findViewById(R.id.chat);
        approvedStudent = view.findViewById(R.id.approvedStudent);
        approvedBatches = view.findViewById(R.id.approvedBatches);
        rejecttedBatch = view.findViewById(R.id.rejectedBatch);

        SharedPreferences prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);
        reporting = prefs.getString("Reporting",null);

        Database.getRejectNotification(username,this);

        name.setText(username);
        manager.setText(reporting);

        try{
            Date td = android.icu.util.Calendar.getInstance().getTime();
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

        Database.getNoOfBatches(username,currentDate,this,getContext());
        Database.getNoOfBatchesToday(username,todaysDay,currentDate,this,getContext());
        Database.getNoOfStudents(username,this,getContext());

        EnrollStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Enroll_Student.class));
            }
        });

        CreateBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Create_Batch.class));
            }
        });

        Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Center_Incharge_Report.class));
            }
        });

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Attendance.class));
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Chat.class));
            }
        });

        approvedStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Approved_Students.class));
            }
        });

        approvedBatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Approved_Batch.class));
            }
        });

        rejecttedBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Rejected_Batch.class));
            }
        });

        return  view;
    }
    public void setNoti(boolean note){
        if(note){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                CharSequence name = "Personal Notifications";
                String description = "Include all the personal notifications";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;

                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,name,importance);

                notificationChannel.setDescription(description);

                NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID);
            builder.setSmallIcon(R.drawable.notification);
            builder.setContentTitle("Batches Rejected");
            builder.setContentText("Batches have been rejected with remarks");
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getContext());
            notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());
        }
    }

    public void setNoOfBatches(int count)
    {
        batchcount.setText(""+count);
    }

    public void setNoOfBatchesToday(int count)
    {
        todaybatch.setText(""+count);
    }

    public void setNoOfStudents(int count)
    {
        stdcount.setText(""+count);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
