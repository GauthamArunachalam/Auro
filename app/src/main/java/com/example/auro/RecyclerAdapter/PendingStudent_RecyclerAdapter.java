package com.example.auro.RecyclerAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.auro.Adapter.Batch;
import com.example.auro.Adapter.*;
import com.example.auro.R;

import java.util.List;

public class PendingStudent_RecyclerAdapter extends RecyclerView.Adapter<PendingStudent_RecyclerAdapter.ViewHolder> {
    private List<StudentDetails> list;
    private List<Status> list2;
    private Context context;

    public PendingStudent_RecyclerAdapter(List<StudentDetails> list,List<Status> list2, Context context)
    {
        this.context = context;
        this.list = list;
        this.list2 = list2;
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

        Status status = list2.get(i);
        int s = status.getStatus();

        if(s==0){
            viewHolder.stdID.setTextColor(Color.parseColor("#D7001B"));
            viewHolder.stdName.setTextColor(Color.parseColor("#D7001B"));
            viewHolder.fatherName.setTextColor(Color.parseColor("#D7001B"));
            viewHolder.motherName.setTextColor(Color.parseColor("#D7001B"));
            viewHolder.gender.setTextColor(Color.parseColor("#D7001B"));
            viewHolder.standard.setTextColor(Color.parseColor("#D7001B"));
            viewHolder.dob.setTextColor(Color.parseColor("#D7001B"));

        }
        else
        {
            viewHolder.stdID.setTextColor(Color.parseColor("#4AA247"));
            viewHolder.stdName.setTextColor(Color.parseColor("#4AA247"));
            viewHolder.fatherName.setTextColor(Color.parseColor("#4AA247"));
            viewHolder.motherName.setTextColor(Color.parseColor("#4AA247"));
            viewHolder.gender.setTextColor(Color.parseColor("#4AA247"));
            viewHolder.standard.setTextColor(Color.parseColor("#4AA247"));
            viewHolder.dob.setTextColor(Color.parseColor("#4AA247"));
        }
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
