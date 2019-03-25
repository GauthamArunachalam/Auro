package com.example.auro.RecyclerAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.auro.Adapter.Batch;
import com.example.auro.Adapter.StudentDetails;
import com.example.auro.R;

import java.util.List;

public class ApprovedBatch_RecyclerAdapter extends RecyclerView.Adapter<ApprovedBatch_RecyclerAdapter.ViewHolder> {
    private List<Batch> list;
    private Context context;

    public ApprovedBatch_RecyclerAdapter(List<Batch> list, Context context)
    {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.batch_list_details,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Batch items = list.get(i);
        viewHolder.batchName.setText(items.getBatch_name());
        viewHolder.std.setText(items.getStandard());
        viewHolder.SD.setText(items.getStart_date());
        viewHolder.ED.setText(items.getEnd_date());
        viewHolder.ST.setText(items.getStart_time());
        viewHolder.ET.setText(items.getEnd_time());
        viewHolder.days.setText(items.getDays());
        viewHolder.incharge.setText(items.getIncharge());
        viewHolder.manager.setText(items.getManager());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView batchName,std,SD,ED,ST,ET,days,incharge,manager;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            batchName = itemView.findViewById(R.id.batchName);
            std = itemView.findViewById(R.id.std);
            SD = itemView.findViewById(R.id.sD);
            ED = itemView.findViewById(R.id.eD);
            ST = itemView.findViewById(R.id.sT);
            ET = itemView.findViewById(R.id.eT);
            days = itemView.findViewById(R.id.days);
            incharge = itemView.findViewById(R.id.incharge);
            manager = itemView.findViewById(R.id.manager);
        }
    }
}
