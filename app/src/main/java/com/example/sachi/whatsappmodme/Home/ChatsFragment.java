package com.example.sachi.whatsappmodme.Home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.sachi.whatsappmodme.Models.Chats;
import com.example.sachi.whatsappmodme.Models.User;
import com.example.sachi.whatsappmodme.Models.UserAccountSettings;
import com.example.sachi.whatsappmodme.R;
import com.example.sachi.whatsappmodme.Utils.ChatsListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatsFragment extends Fragment  {

    public RecyclerView recyclerview;
    private ArrayList<Chats> chat_list;
    private ArrayList<String> user_id;
    private DatabaseReference myRef;
    private Chats chats;
    int counter=-1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chats,container,false);
        recyclerview=(RecyclerView) v.findViewById(R.id.recyclerview);
        chat_list=new ArrayList<>();
        user_id=new ArrayList<>();

        myRef=FirebaseDatabase.getInstance().getReference();
           //retrieve the chat List here
      readUserID(new FirebaseCallback1() {
          @Override
          public void onCallback() {
              //got user_id in user_id list
              readUsername(new FirebaseCallback2() {
                  @Override
                  public void afterRead(final String username,final String url) {

                      counter++;
                      readLastmessage(new FirebaseCallback3() {
                          @Override
                          public void afterRead2(String lastmsg, String lasttime) {

                              chats = new Chats(username,lasttime,url,lastmsg);
                              chat_list.add(chats);
                              //setup recyclerview here
                              Log.d("sachin","setting recycler");
                              ChatsListAdapter adapter = new ChatsListAdapter(getActivity(),chat_list);
                              recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                              recyclerview.setAdapter(adapter);
                          }
                      },user_id.get(counter));
                  }
              });
          }
      });

        return v;
    }
    private interface FirebaseCallback1{
        void onCallback();
    }
    private interface FirebaseCallback2{
        void afterRead(String username,String url);
    }
    private interface FirebaseCallback3{
        void afterRead2(String lastmsg,String lasttime);
    }
    private void readUserID(final FirebaseCallback1 firebaseCallback){
        Query query =  myRef.child(getString(R.string.dbname_CHATS)).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for(DataSnapshot ds : dataSnapshot.getChildren()){
                   user_id.add(ds.getKey());

               }
                firebaseCallback.onCallback();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void readUsername(final FirebaseCallback2 firebaseCallback2){
        Iterator it = user_id.iterator();
        while(it.hasNext()){
            Query query = myRef.child(getString(R.string.dbname_user_account_settings)).child(it.next().toString());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    firebaseCallback2.afterRead(dataSnapshot.getValue(UserAccountSettings.class).getUsername(),dataSnapshot.getValue(UserAccountSettings.class).getProfile_photo_url());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    private void readLastmessage(final FirebaseCallback3 firebaseCallback3,String s){

            Query query = myRef.child(getString(R.string.dbname_CHATS)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(s);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String lastmsg="nothing",lasttime="nothing";
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        if(ds.getKey().equals(getString(R.string.dbname_lastmsg)))
                            lastmsg=ds.getValue(String.class);
                        if(ds.getKey().equals(getString(R.string.dbname_lastmsgtime)))
                            lasttime=ds.getValue(String.class);
                    }
                  firebaseCallback3.afterRead2(lastmsg,lasttime);
                   // firebaseCallback3.afterRead2(dataSnapshot.getValue(UserAccountSettings.class).getUsername(),dataSnapshot.getValue(UserAccountSettings.class).getProfile_photo_url());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }
}
