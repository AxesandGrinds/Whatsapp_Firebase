package com.example.sachi.whatsappmodme.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sachi.whatsappmodme.Models.Chats;
import com.example.sachi.whatsappmodme.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsListAdapter extends RecyclerView.Adapter<ChatsListAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Chats> chat_list;
    public ChatsListAdapter(Context context,ArrayList<Chats> chat_list){
        this.context=context;
        this.chat_list=chat_list;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.chats_list_row,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Chats chats = chat_list.get(position);
        holder.message.setText(chats.getMessage());
        holder.timestamp.setText(chats.getTimestamp());
        holder.username.setText(chats.getUsername());
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(chats.getProfile_pic_url(),holder.profile_pic);
        setListener(holder.profile_pic,position);
    }

    private void setListener(CircleImageView profile_pic,final int position) {
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Implement a dialog fragment here..
            }
        });
    }

    @Override
    public int getItemCount() {
        return chat_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView profile_pic;
        public TextView timestamp,username,message;
        public MyViewHolder(View view){
            super(view);
            profile_pic=(CircleImageView) view.findViewById(R.id.profile_pic);
            timestamp=(TextView) view.findViewById(R.id.timestamp);
            username=(TextView) view.findViewById(R.id.username);
            message=(TextView) view.findViewById(R.id.message);
        }

   }
}
