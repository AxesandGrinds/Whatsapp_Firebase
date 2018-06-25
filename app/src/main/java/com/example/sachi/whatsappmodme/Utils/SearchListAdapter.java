package com.example.sachi.whatsappmodme.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sachi.whatsappmodme.ChatScreen.ChatActivity;
import com.example.sachi.whatsappmodme.Models.Chats;
import com.example.sachi.whatsappmodme.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.MyViewHolder> {
    private static Context context;
    private static ArrayList<Chats> chat_list;
    public SearchListAdapter(Context context,ArrayList<Chats> chat_list){
        this.context=context;
        this.chat_list=chat_list;
        Log.d("sachin","searchListAdapter is called");
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.chats_list_row,parent,false);
        return new SearchListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Chats chats = chat_list.get(position);
        holder.message.setText(chats.getMessage());
        holder.timestamp.setText(chats.getTimestamp());
        holder.username.setText(chats.getUsername());
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(chats.getProfile_pic_url(),holder.profile_pic);
        holder.profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Image Click dialog fragment opening code
            }
        });
    }

    @Override
    public int getItemCount() {
        return chat_list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public CircleImageView profile_pic;
        public TextView timestamp,username,message;
        public MyViewHolder(View view){
            super(view);
            profile_pic=(CircleImageView) view.findViewById(R.id.profile_pic);
            timestamp=(TextView) view.findViewById(R.id.timestamp);
            username=(TextView) view.findViewById(R.id.username);
            message=(TextView) view.findViewById(R.id.message);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Click anywhere except image code here and send to other fragment using chat_list.get(getLayoutPosition());
            Intent i = new Intent(context, ChatActivity.class);
            Bundle b = new Bundle();
            Log.d("sachin","sending value "+chat_list.get(getLayoutPosition()).getUsername());
            b.putString("username_bundle",chat_list.get(getLayoutPosition()).getUsername());
            i.putExtra("username_extra",b);
            context.startActivity(i);
        }
    }
}
