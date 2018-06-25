package com.example.sachi.whatsappmodme.Settings;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sachi.whatsappmodme.R;

public class AboutDeveloperFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_about_developer,container,false);
        Button place = (Button) v.findViewById(R.id.locatebtn);
        ImageView back = (ImageView) v.findViewById(R.id.back_arrow);
        ImageView email=(ImageView) v.findViewById(R.id.mailme);
        ImageView call = (ImageView) v.findViewById(R.id.callme);
        final TextView visitBlog = (TextView) v.findViewById(R.id.visit_blog);
        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Ektu darao dada eto joldi hye na !!", Toast.LENGTH_SHORT).show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity().getSupportFragmentManager().getBackStackEntryCount()==0){
                    getActivity().finish();
                }else
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                startActivity(Intent.createChooser(i,"Send Using.."));
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri callno = Uri.parse("tel:8981895883");
                intent.setData(callno);
                startActivity(intent);
            }
        });
        visitBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://"+visitBlog.getText().toString()));

                startActivity(browserIntent);
            }
        });
        return v;
    }
}
