package com.example.auro.Director;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.auro.Pending_Batch_Request;
import com.example.auro.Pending_Student_Batch_List;
import com.example.auro.Project_Manager.Request_Student_list;
import com.example.auro.R;

public class Director_Home_Page extends Fragment {
    private Button registration,addStandard,assignBatch,viewProjectManager,requestBatch,requestStudent,report;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.director_home_page, container, false);

        registration = view.findViewById(R.id.registration);
        addStandard = view.findViewById(R.id.addStandard);
        assignBatch = view.findViewById(R.id.assignBatch);
        viewProjectManager = view.findViewById(R.id.viewProjectManager);
        requestBatch = view.findViewById(R.id.requestBatch);
        requestStudent = view.findViewById(R.id.requestStudent);
        report = view.findViewById(R.id.report);

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Registration.class));
            }
        });

        addStandard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Add_Standard.class));
            }
        });

        assignBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Assign_Batch.class));
            }
        });
        viewProjectManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Project_Manager_List.class));
            }
        });

        requestBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Pending_Batch_Request.class));
            }
        });
        requestStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Pending_Student_Batch_List.class));
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Director_Report.class));
            }
        });
        
        return view;
    }
}
