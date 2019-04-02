package com.example.auro.Project_Manager;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.auro.Approved_Batch;
import com.example.auro.Approved_Students;
import com.example.auro.Chat;
import com.example.auro.DB.Database;
import com.example.auro.Pending_Batch_Request;
import com.example.auro.Pending_Student_Batch_List;
import com.example.auro.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class Project_Manager_Home_Page extends Fragment {

    private CardView assignBatch, batchRequest,studentRequest,report,chat,centerIncharge;
    private TextView day,date,name,batchcount,inchargecount,stdcount;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public String username, today;
    private Calendar currentDate;
    private Button approvedStudents,approvedBatches;
    private final String CHANNEL_ID = "personal_notifications";
    private final int NOTIFICATION_ID = 001, NOTIFICATION_ID2 = 002;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.project_manager_home_page, container, false);

        assignBatch = view.findViewById(R.id.assignBatch);
        batchRequest = view.findViewById(R.id.batchrequest);
        studentRequest = view.findViewById(R.id.studentRequest);
        report = view.findViewById(R.id.report);
        chat = view.findViewById(R.id.chat);
        centerIncharge = view.findViewById(R.id.incharge);
        day = view.findViewById(R.id.day);
        date = view.findViewById(R.id.date);
        name = view.findViewById(R.id.userName);
        batchcount = view.findViewById(R.id.bat);
        inchargecount = view.findViewById(R.id.charge);
        stdcount = view.findViewById(R.id.stud);
        approvedStudents = view.findViewById(R.id.approvedStudent);
        approvedBatches = view.findViewById(R.id.approvedBatches);

        SharedPreferences prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);

        name.setText(username);

        Database.getPendingStudentNotification(username,this);
        Database.getPendingBatchNotification(username,this);

        try{
            Date td = android.icu.util.Calendar.getInstance().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            today = dateFormat.format(td);
            date.setText(today);
            Date dts = dateFormat.parse(today);

            currentDate = new GregorianCalendar();
            currentDate.setTime(dts);

            int x = currentDate.getTime().getDay();

            if (x == 0) {
                day.setText("Sunday");
            } else if (x == 1) {
                day.setText("Monday");
            } else if (x == 2) {
                day.setText("Tuesday");
            } else if (x == 3) {
                day.setText("Wednesday");
            } else if (x == 4) {
                day.setText("Thursday");
            } else if (x == 5) {
                day.setText("Friday");
            } else if (x == 6) {
                day.setText("Saturday");
            }
        }catch (Exception e){
            System.out.println(e);
        }

        Database.getNoOfBatches(username,currentDate,this,getContext());
        Database.getNoOfStudents(username,this,getContext());
        Database.getCenterInchargeCount(username,this,getContext());

        assignBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(getContext(),ProjectManager_AssignBatch.class));
            }
        });
        batchRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Pending_Batch_Request.class));
            }
        });
        studentRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(getContext(), Pending_Student_Batch_List.class));
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Project_Manager_Report.class));
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Chat.class));
            }
        });
        centerIncharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Center_Incharge_List.class));
            }
        });
        approvedStudents.setOnClickListener(new View.OnClickListener() {
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

        return  view;
    }
    public void setPendingStudentNotification(boolean note){
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
            builder.setContentTitle("Student Enrollment Request");
            builder.setContentText("Students are waiting for approval to join batch");
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getContext());
            notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());
        }
    }

    public void setPendingBatchNotification(boolean note){
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
            builder.setContentTitle("Batch request pending");
            builder.setContentText("Batches are created and waiting for approval");
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getContext());
            notificationManagerCompat.notify(NOTIFICATION_ID2,builder.build());
        }
    }

    public void setNoOfBatches(int count)
    {
        batchcount.setText(""+count);
    }

    public void setCenterInchargeCount(int count)
    {
        inchargecount.setText(""+count);
    }

    public void setNoOfStudents(int count)
    {
        stdcount.setText(""+count);
    }
}
