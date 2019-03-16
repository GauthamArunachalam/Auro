package com.example.auro.RecyclerAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.auro.Adapter.AssignBatches;
import com.example.auro.Adapter.UserDetails;
import com.example.auro.R;

import java.util.List;

public class ProjectManagerLisDetails_RecyclerAdapter extends RecyclerView.Adapter<ProjectManagerLisDetails_RecyclerAdapter.ViewHolder> {
    private List<AssignBatches> list;
    private Context context;

    public ProjectManagerLisDetails_RecyclerAdapter(List<AssignBatches> list, Context context)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.project_manager_details,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AssignBatches items = list.get(i);
        viewHolder.projectManagerName.setText(items.getProjectManager());
        viewHolder.noOfBatches.setText(items.getNoOfBatches());
        viewHolder.standard.setText(items.getStd());
        viewHolder.batchCreated.setText(items.getBatchesCreated());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView projectManagerName;
        public TextView noOfBatches;
        public TextView batchCreated;
        public TextView standard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            projectManagerName = itemView.findViewById(R.id.projectManagerName);
            noOfBatches = itemView.findViewById(R.id.noOfBatch);
            batchCreated = itemView.findViewById(R.id.assignedBatch);
            standard = itemView.findViewById(R.id.stand);
        }
    }
}
