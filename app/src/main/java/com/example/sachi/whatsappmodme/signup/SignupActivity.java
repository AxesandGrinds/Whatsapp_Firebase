package com.example.sachi.whatsappmodme.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sachi.whatsappmodme.R;
import com.example.sachi.whatsappmodme.Utils.FirebaseUtils;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;


public class SignupActivity extends AppCompatActivity {
    MaterialEditText email,username,password;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    DotProgressBar mProgress;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signup);
        mAuth=FirebaseAuth.getInstance();
        myRef= FirebaseDatabase.getInstance().getReference();
        mProgress=(DotProgressBar) findViewById(R.id.progress);
        TextView loginlink = (TextView) findViewById(R.id.loginlink);
        Button signup_btn = (Button) findViewById(R.id.signupbtn);
       email=(MaterialEditText) findViewById(R.id.email);
       username=(MaterialEditText) findViewById(R.id.username);
       password=(MaterialEditText) findViewById(R.id.password);
       signup_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
            if(validated()){
                mProgress.setVisibility(View.VISIBLE);
                FirebaseUtils utils=new FirebaseUtils(SignupActivity.this);
                utils.registerNewUser(email.getText().toString(),username.getText().toString(),password.getText().toString(),mProgress);
            }
           }
       });
        loginlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                finish();
            }
        });
    }
    private boolean validated(){
        if(email.getText().toString().length()>13){
        if(email.getText().toString().substring(email.getText().toString().length()-13).equals("@whatsapp.com")){
            Snackbar.make(getCurrentFocus().getRootView(),"Email should end with @whatsapp.com",Snackbar.LENGTH_SHORT).show();
            return false;
        }}else{
            Snackbar.make(getCurrentFocus().getRootView(),"Email too short!!",Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if(!(username.getText().toString().length()>=4)){
            Snackbar.make(getCurrentFocus().getRootView(),"Username too short!!",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if(!(password.getText().toString().length()>=6)){
            Snackbar.make(getCurrentFocus().getRootView(),"password too short!!",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
