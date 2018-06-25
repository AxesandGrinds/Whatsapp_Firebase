package com.example.sachi.whatsappmodme.Settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sachi.whatsappmodme.ChatScreen.ChatScreenFragment;
import com.example.sachi.whatsappmodme.Models.UserAccountSettings;
import com.example.sachi.whatsappmodme.R;
import com.example.sachi.whatsappmodme.Utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragment extends Fragment {
    RelativeLayout chats,notifications,data_usage,contact_developer,terms_privacy,app_info,settings_header;
    CircleImageView profile_pic;
    TextView username,about;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("sachin","settings fragment");
       View v=inflater.inflate(R.layout.fragment_settings,container,false);
        ImageView back = (ImageView) v.findViewById(R.id.back_arrow);
        chats=(RelativeLayout) v.findViewById(R.id.wtf);
        profile_pic=(CircleImageView) v.findViewById(R.id.profile_pic);
        username=(TextView) v.findViewById(R.id.username);
        about=(TextView) v.findViewById(R.id.about);
        notifications=(RelativeLayout) v.findViewById(R.id.das);
        settings_header=(RelativeLayout) v.findViewById(R.id.settings_header);
        data_usage=(RelativeLayout) v.findViewById(R.id.ffg);
        contact_developer=(RelativeLayout) v.findViewById(R.id.contact_dev);
        terms_privacy=(RelativeLayout) v.findViewById(R.id.terms_privacy);
        app_info=(RelativeLayout) v.findViewById(R.id.app_info);
        chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        data_usage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatScreenFragment fragment = new ChatScreenFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.replace,fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        contact_developer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutDeveloperFragment fragment=new AboutDeveloperFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.replace,fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        terms_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermsPrivacyFragment fragment = new TermsPrivacyFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.replace,fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        app_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppInfoFragment fragment=new AppInfoFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.replace,fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        settings_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment fragment = new ProfileFragment();
                FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.replace,fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        setProfileValues();
       return v;
    }
    private void setProfileValues(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        Query query = myRef.child(getString(R.string.dbname_user_account_settings)).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             UserAccountSettings settings = dataSnapshot.getValue(UserAccountSettings.class);
             username.setText(settings.getUsername());
             about.setText(settings.getAbout());
             ImageLoader loader=ImageLoader.getInstance();
             //Log.d("sachin",settings.getProfile_photo_url());
            loader.displayImage(settings.getProfile_photo_url(),profile_pic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
