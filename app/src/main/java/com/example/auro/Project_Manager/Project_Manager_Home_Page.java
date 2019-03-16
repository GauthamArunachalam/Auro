package com.example.auro.Project_Manager;

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

public class Project_Manager_Home_Page extends Fragment {

    private Button assignBatch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.project_manager_home_page, container, false);

        assignBatch = view.findViewById(R.id.assignBatch);
        assignBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(getContext(),ProjectManager_AssignBatch.class));
            }
        });
        return  view;
    }
}
