package com.example.sachi.whatsappmodme.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sachi.whatsappmodme.Models.Message;
import com.example.sachi.whatsappmodme.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatScreenListAdapter extends RecyclerView.Adapter<ChatScreenListAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Message> message_list;
    public ChatScreenListAdapter(Context context,ArrayList<Message> message_list){
        this.context=context;
        this.message_list=message_list;
        Log.d("sachin","call success");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        Log.d("sachin","sotti onCraetae View Holder");
        View v = inflater.inflate(R.layout.chat_screen_row,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Message message = message_list.get(position);
        Log.d("sachin","trying to setup chatscreen");
        if(message.getSender_user_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            //It is my msg
            holder.mymsg.setText(message.getMessage());
            holder.mymsg_layout.setVisibility(View.VISIBLE);
            holder.hismsg_layout.setVisibility(View.GONE);
            holder.myTime.setText(message.getTime());
            Log.d("sachin","setting up ur msgs");
        }else{
            //It is His msg
            holder.hismsg.setText(message.getMessage());
            holder.hismsg_layout.setVisibility(View.VISIBLE);
            holder.mymsg_layout.setVisibility(View.GONE);
            holder.hisTime.setText(message.getTime());
            Log.d("sachin","setting up his msgs");
        }
    }

    @Override
    public int getItemCount() {
        return message_list.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout mymsg_layout,hismsg_layout;
        public TextView mymsg,hismsg,myTime,hisTime;
        public MyViewHolder(View v){
            super(v);
            mymsg_layout=(LinearLayout) v.findViewById(R.id.chat_right_msg_layout);
            hismsg_layout=(LinearLayout) v.findViewById(R.id.chat_left_msg_layout);
            mymsg=(TextView) v.findViewById(R.id.chat_right_msg_text_view);
            hismsg=(TextView) v.findViewById(R.id.chat_left_msg_text_view);
            myTime=(TextView) v.findViewById(R.id.myTimeStamp);
            hisTime=(TextView) v.findViewById(R.id.hisTimeStamp);
        }
    }
}
