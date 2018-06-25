package com.example.sachi.whatsappmodme.ChatScreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sachi.whatsappmodme.Models.Message;
import com.example.sachi.whatsappmodme.Models.User;
import com.example.sachi.whatsappmodme.Models.UserAccountSettings;
import com.example.sachi.whatsappmodme.R;
import com.example.sachi.whatsappmodme.Utils.ChatScreenListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatScreenFragment extends Fragment {
    private RelativeLayout back;
    private TextView username,last_seen;
    private CircleImageView profile_pic;
    private ImageView call,options;
    private EditText message;
    private Button send_message;
    String username_current;
    private RecyclerView recyclerview;
    private ChatScreenListAdapter adapter;
    String entered="";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.layout_chatscreen,container,false);
        username_current = getArguments().getString("username_bundle");
        back=(RelativeLayout) v.findViewById(R.id.back_arrow);
        username=(TextView) v.findViewById(R.id.username);
        last_seen=(TextView) v.findViewById(R.id.last_seen);
        profile_pic=(CircleImageView) v.findViewById(R.id.profile_pic);
        call=(ImageView) v.findViewById(R.id.call);
        options = (ImageView) v.findViewById(R.id.clear_chat);
        message=(EditText) v.findViewById(R.id.message);
        send_message=(Button) v.findViewById(R.id.send_message);
        recyclerview=(RecyclerView) v.findViewById(R.id.recyclerview);
        username.setText(username_current);
        setWidgetValues();
        setListeners();
        retrieveOlderMessages();
        return v;
    }
    private void setWidgetValues(){
        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();
        Query query=myRef.child(getString(R.string.dbname_user_account_settings)).orderByChild(getString(R.string.dbname_username)).equalTo(username_current);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for(DataSnapshot ds : dataSnapshot.getChildren()){
                   ImageLoader loader = ImageLoader.getInstance();
                   loader.displayImage(ds.getValue(UserAccountSettings.class).getProfile_photo_url(),profile_pic);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        query=myRef.child(getString(R.string.dbname_users)).orderByChild(getString(R.string.dbname_username)).equalTo(username_current);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds :  dataSnapshot.getChildren()){
                    last_seen.setText(ds.getValue(User.class).getLast_seen());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setListeners(){
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entered= message.getText().toString();
                final DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();
                Query query=myRef.child(getString(R.string.dbname_users)).orderByChild(getString(R.string.dbname_username)).equalTo(username_current);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                           final String UID = ds.getValue(User.class).getUser_id();
                           Message message_model = new Message(entered,getTimeNow(),FirebaseAuth.getInstance().getCurrentUser().getUid());
                           //setting dataModel for sender
                           myRef.child(getString(R.string.dbname_CHATS)).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                   .child(UID).push().setValue(message_model);
                           //sending dataModel for reciever
                            myRef.child(getString(R.string.dbname_CHATS)).child(UID)
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(message_model);
                            //setting sending time for sender
                            myRef.child(getString(R.string.dbname_CHATS)).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(UID).child(getString(R.string.dbname_lastmsgtime)).setValue(getTimeNow());
                            //setting last msg for sender
                            myRef.child(getString(R.string.dbname_CHATS)).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(UID).child(getString(R.string.dbname_lastmsg)).setValue(message_model.getMessage());
                            //setting recieving time for reciever
                            myRef.child(getString(R.string.dbname_CHATS)).child(UID)
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getString(R.string.dbname_lastmsgtime)).setValue(getTimeNow());
                            //setting last msg time for reciever
                            myRef.child(getString(R.string.dbname_CHATS)).child(UID)
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getString(R.string.dbname_lastmsg)).setValue(message_model.getMessage());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                message.setText("");
            }
        });
    }

    private String getTimeNow(){

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(date);
    }
    private void retrieveOlderMessages(){
        final DatabaseReference myRef=FirebaseDatabase.getInstance().getReference();
        Query query = myRef.child(getString(R.string.dbname_users)).orderByChild(getString(R.string.dbname_username)).equalTo(username_current);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String UID = ds.getValue(User.class).getUser_id();
                    Query message_query= myRef.child(getString(R.string.dbname_CHATS)).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(UID);
                    message_query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<Message> message_list = new ArrayList<>();
                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                if(dataSnapshot1.getKey().equals("last_msg")){
                                    Log.d("sachin","getting from last_msg "+dataSnapshot1.getValue());
                                }else
                                if(dataSnapshot1.getKey().equals("last_msg_time")){
                                    Log.d("sachin","getting from last time "+dataSnapshot1.getValue());
                                }else{
                                    message_list.add(dataSnapshot1.getValue(Message.class));
                                }
                             /*   HashMap<String,String> map = new HashMap<>();
                                map.put(dataSnapshot1.getKey(),dataSnapshot1.getValue().toString());
                                Log.d("sachin","the key is :"+dataSnapshot1.getKey());
                    Log.d("sachin","the value is "+dataSnapshot1.getValue().toString());*/
                            }
                            Log.d("sachin","setting up adapter with message list size "+message_list.size());
                            adapter=new ChatScreenListAdapter(getActivity(),message_list);
                            recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerview.setAdapter(adapter);
                            recyclerview.scrollToPosition(message_list.size()-1);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
