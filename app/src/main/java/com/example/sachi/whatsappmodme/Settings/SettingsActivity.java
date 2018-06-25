package com.example.sachi.whatsappmodme.Settings;

import android.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.support.v4.app.FragmentTransaction;

import com.example.sachi.whatsappmodme.R;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if(getIntent().hasExtra("navigate")){
            String navigate_to = getIntent().getStringExtra("navigate");
            Log.d("sachin","got extras "+navigate_to);
            switch(navigate_to){
                case "about_dev" : AboutDeveloperFragment fragment = new AboutDeveloperFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.replace,fragment);
                    //transaction.addToBackStack(null);
                    transaction.commit();
                    break;
                case "profile_pic" : ViewProfilePicFragment fragment1=new ViewProfilePicFragment();
                Bundle b = new Bundle();
                b.putString("navigate","home");
                fragment1.setArguments(b);
                FragmentTransaction transaction1=getSupportFragmentManager().beginTransaction();
                transaction1.replace(R.id.replace,fragment1);
                transaction1.commit();
                break;
                case "username" :
                    ProfileFragment fragment2=new ProfileFragment();
                    Bundle b2= new Bundle();
                    b2.putString("navigate","profile_fragment");
                    fragment2.setArguments(b2);
                    FragmentTransaction transaction2=getSupportFragmentManager().beginTransaction();
                    transaction2.replace(R.id.replace,fragment2);
                    transaction2.commit();
                    break;

            }
        }else{
            SettingsFragment fragment=new SettingsFragment();
          FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.replace,fragment);
            //transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
