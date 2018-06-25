package com.example.sachi.whatsappmodme.ChatScreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.sachi.whatsappmodme.R;

public class ChatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        Bundle username_bundle = getIntent().getBundleExtra("username_extra");
        ChatScreenFragment fragment= new ChatScreenFragment();
        fragment.setArguments(username_bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.replace,fragment);
        transaction.commit();
    }
}
