package com.example.sachi.whatsappmodme.signup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sachi.whatsappmodme.Home.HomeActivity;
import com.example.sachi.whatsappmodme.R;
import com.example.sachi.whatsappmodme.Utils.FirebaseUtils;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    MaterialEditText email,password;
    DotProgressBar mProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        checkforPermissions();
        mAuth=FirebaseAuth.getInstance();
        email=(MaterialEditText) findViewById(R.id.email);
        mProgress=(DotProgressBar) findViewById(R.id.progress);
        password=(MaterialEditText) findViewById(R.id.password);
        TextView signuplink = (TextView) findViewById(R.id.signuplink);
        Button btn_login = (Button) findViewById(R.id.signinbtn);
        signuplink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
                finish();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setVisibility(View.VISIBLE);
                FirebaseUtils utils = new FirebaseUtils(LoginActivity.this);
                utils.loginUser(email.getText().toString(),password.getText().toString(),mProgress);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("sachin","getting into onStart");
       if(FirebaseAuth.getInstance().getCurrentUser()!=null){
           startActivity(new Intent(LoginActivity.this, HomeActivity.class));
       }
    }
    private void checkforPermissions(){
        if(ContextCompat.checkSelfPermission(LoginActivity.this,Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.INTERNET},1);
        }
        if(ContextCompat.checkSelfPermission(LoginActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        if(ContextCompat.checkSelfPermission(LoginActivity.this,Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
        }
    }
}
