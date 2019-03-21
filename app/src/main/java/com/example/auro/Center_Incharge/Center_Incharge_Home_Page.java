package com.example.auro.Center_Incharge;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.*;

import com.example.auro.Chat;
import com.example.auro.R;

public class Center_Incharge_Home_Page extends Fragment implements AdapterView.OnItemSelectedListener {
    private Button EnrollStudent,CreateBatch,Report,attendance,chat;
    private String problemList[] = {"Select","Electricity Shutdown","Local Function","System Crash","Network Issue","Others"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.center_incharge_home_page, container, false);

        EnrollStudent = view.findViewById(R.id.es);
        CreateBatch = view.findViewById(R.id.cb);
        Report = view.findViewById(R.id.report);
        attendance = view.findViewById(R.id.attendance);
        chat = view.findViewById(R.id.chat);



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
        return  view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
