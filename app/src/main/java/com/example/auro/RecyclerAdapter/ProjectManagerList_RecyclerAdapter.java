package com.example.auro.RecyclerAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.auro.Adapter.UserDetails;
import com.example.auro.R;

import java.util.List;

public class ProjectManagerList_RecyclerAdapter extends RecyclerView.Adapter<ProjectManagerList_RecyclerAdapter.ViewHolder> {
    private List<UserDetails> list;
    private Context context;

    public ProjectManagerList_RecyclerAdapter(List<UserDetails> list, Context context)
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
        UserDetails items = list.get(i);
        viewHolder.projectManagerL.setText(items.getName());
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
