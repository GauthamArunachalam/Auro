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
import com.example.auro.R;

import java.util.List;

public class Attendance_RecyclerAdapter extends RecyclerView.Adapter<Attendance_RecyclerAdapter.ViewHolder> {
    private List<String> list;
    private List<Status> list2;
    private Context context;

    public Attendance_RecyclerAdapter(List<String> list, List<Status> list2, Context context)
    {
        this.context = context;
        this.list = list;
        this.list2 = list2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.projectm_manager_list,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String items = list.get(i);
        viewHolder.projectManagerL.setText(items);

        Status st = list2.get(i);
        int s = st.getStatus();
        if(s==1)
        {
            viewHolder.projectManagerL.setTextColor(Color.parseColor("#4AA247"));
        }
        else
        {
            viewHolder.projectManagerL.setTextColor(Color.parseColor("#D7001B"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView projectManagerL;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            projectManagerL = itemView.findViewById(R.id.projectManagerList);
        }
    }
}
