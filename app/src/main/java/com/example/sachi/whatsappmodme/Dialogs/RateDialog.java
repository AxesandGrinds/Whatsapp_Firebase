package com.example.sachi.whatsappmodme.Dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;

import com.example.sachi.whatsappmodme.R;

public class RateDialog extends DialogFragment {
    RatingBar rb1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialog_rate,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rb1=(RatingBar)view.findViewById(R.id.star5);
        Button b1 = (Button) view.findViewById(R.id.ok);
        Button b2 = (Button) view.findViewById(R.id.dismiss1);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(getActivity().getWindow().getDecorView().getRootView(),"Rated :"+rb1.getRating(),Snackbar.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        });
    }
}
