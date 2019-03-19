package com.example.auro.Center_Incharge;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.auro.R;

public class Center_Incharge_Home_Page extends Fragment {
    private Button EnrollStudent,CreateBatch,Report;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.center_incharge_home_page, container, false);

        EnrollStudent = view.findViewById(R.id.es);
        CreateBatch = view.findViewById(R.id.cb);
        Report = view.findViewById(R.id.report);

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
                startActivity(new Intent(getContext(), Center_Incharge_Batch_Report.class));
            }
        });

        return  view;
    }

}
