package com.example.auro.RecyclerAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.auro.Adapter.Message;
import com.example.auro.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Chat_RecyclerAdapter extends RecyclerView.Adapter<Chat_RecyclerAdapter.ViewHolder> {
    private List<Message> list;
    private Context context;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public String username;

    public Chat_RecyclerAdapter(List<Message> list, Context context)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.indudival_message,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Message items = list.get(i);
        String to = items.getTo();

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);

        if(!to.equals("BroadCast"))
        {
            if(items.getFrom().equals(username))
            {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                viewHolder.msg.setLayoutParams(params);
                viewHolder.msg.setMaxWidth(750);
                viewHolder.msg.setPadding(50,30,50,30);
                viewHolder.msg.setText(items.getMessage());
            }
            else
            {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                viewHolder.msg.setLayoutParams(params);
                viewHolder.msg.setMaxWidth(750);
                viewHolder.msg.setPadding(50,30,50,30);
                viewHolder.msg.setText(items.getMessage());
            }
        }
        else
        {
            String x = items.getFrom() + " : " + items.getMessage();
            viewHolder.msg.setText(x);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView msg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.chat);
        }
    }
}
