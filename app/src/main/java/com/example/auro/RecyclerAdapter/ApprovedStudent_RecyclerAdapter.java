package com.example.auro.RecyclerAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.auro.Adapter.Status;
import com.example.auro.Adapter.StudentDetails;
import com.example.auro.R;

import java.util.List;

public class ApprovedStudent_RecyclerAdapter extends RecyclerView.Adapter<ApprovedStudent_RecyclerAdapter.ViewHolder> {
    private List<StudentDetails> list;
    private Context context;

    public ApprovedStudent_RecyclerAdapter(List<StudentDetails> list, Context context)
    {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.student_list_details,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        StudentDetails items = list.get(i);
        viewHolder.stdID.setText(items.getStudentID());
        viewHolder.stdName.setText(items.getStudentName());
        viewHolder.fatherName.setText(items.getFatherName());
        viewHolder.motherName.setText(items.getMotherName());
        viewHolder.gender.setText(items.getGender());
        viewHolder.standard.setText(items.getStandard());
        viewHolder.dob.setText(items.getDob());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView stdID,stdName,dob,fatherName,motherName,gender,standard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stdID = itemView.findViewById(R.id.stdID);
            stdName = itemView.findViewById(R.id.stdName);
            fatherName = itemView.findViewById(R.id.fatherName);
            motherName = itemView.findViewById(R.id.motherName);
            gender = itemView.findViewById(R.id.gender);
            standard = itemView.findViewById(R.id.std);
            dob = itemView.findViewById(R.id.dob);
        }
    }
}
