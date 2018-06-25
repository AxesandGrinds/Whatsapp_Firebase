package com.example.sachi.whatsappmodme.Dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.sachi.whatsappmodme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChangeAboutDialog extends DialogFragment {

    public interface send{
        public void sendAbout(String about);
    }
    send sendabout;
    TextView current_about;
    Button commit,cancel;
    MaterialEditText new_username;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.dialog_change_about,container,false);
        current_about=(TextView) v.findViewById(R.id.current_about);
        commit=(Button) v.findViewById(R.id.commit);
        cancel=(Button) v.findViewById(R.id.cancel);
        new_username=(MaterialEditText) v.findViewById(R.id.change_username);
        current_about.setText("Current About : "+getTag());
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change();
                getDialog().dismiss();
            }
        });
        return v;
    }

    private void change(){
        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();
       // myRef.child(getString(R.string.dbname_users)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getString(R.string.dbname_username)).setValue(new_username.getText().toString());
        myRef.child(getString(R.string.dbname_user_account_settings)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getString(R.string.dbname_about)).setValue(new_username.getText().toString());
        myRef.child(getString(R.string.dbname_user_account_settings)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getString(R.string.dbname_about_date)).setValue(getDateToday()+" "+getTimeNow());
        sendabout.sendAbout(new_username.getText().toString());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sendabout=(send) getTargetFragment();
    }
    private String getDateToday(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);

    }
    private String getTimeNow(){

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(date);
    }
}
