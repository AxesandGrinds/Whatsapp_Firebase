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

public class ChangeUsernameDialog extends DialogFragment {
    public interface send{
        public void sendUsername(String Username);
    }
    send sendUser;
    TextView current_about;
    Button commit,cancel;
    MaterialEditText new_username;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.dialog_change_username,container,false);
        current_about=(TextView) v.findViewById(R.id.current_about);
        commit=(Button) v.findViewById(R.id.commit);
        cancel=(Button) v.findViewById(R.id.cancel);
        new_username=(MaterialEditText) v.findViewById(R.id.change_username);
        current_about.setText("Current Username : "+getTag());
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForPresence();
            }
        });
        return v;
    }
    private void checkForPresence(){

        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();
        Query query = myRef.child(getString(R.string.dbname_users)).orderByChild(getString(R.string.dbname_username)).equalTo(new_username.getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Snackbar.make(getActivity().getWindow().getDecorView().getRootView(),"This Username Already exists!!",Snackbar.LENGTH_SHORT).show();
                    getDialog().dismiss();
                }else{
                    change();
                    getDialog().dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void change(){
        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();
        myRef.child(getString(R.string.dbname_users)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getString(R.string.dbname_username)).setValue(new_username.getText().toString());
        myRef.child(getString(R.string.dbname_user_account_settings)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getString(R.string.dbname_username)).setValue(new_username.getText().toString());
       sendUser.sendUsername(new_username.getText().toString());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sendUser=(send) getTargetFragment();
    }
}
