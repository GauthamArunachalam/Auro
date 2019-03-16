package com.example.auro.RecyclerAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.auro.Adapter.Batch;
import com.example.auro.Adapter.UserDetails;
import com.example.auro.R;

import java.util.List;

public class PendingBatch_RecyclerAdapter extends RecyclerView.Adapter<PendingBatch_RecyclerAdapter.ViewHolder> {
    private List<Batch> list;
    private Context context;

    public PendingBatch_RecyclerAdapter(List<Batch> list, Context context)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.projectm_manager_list,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Batch items = list.get(i);
        viewHolder.projectManagerL.setText(items.getBatch_name());
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
