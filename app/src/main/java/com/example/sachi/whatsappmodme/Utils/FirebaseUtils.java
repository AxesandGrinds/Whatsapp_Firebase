package com.example.sachi.whatsappmodme.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.sachi.whatsappmodme.Home.HomeActivity;
import com.example.sachi.whatsappmodme.Models.User;
import com.example.sachi.whatsappmodme.Models.UserAccountSettings;
import com.example.sachi.whatsappmodme.R;
import com.example.sachi.whatsappmodme.signup.LoginActivity;
import com.example.sachi.whatsappmodme.signup.SignupActivity;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class FirebaseUtils {
    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    public FirebaseUtils(Context context){
        mAuth= FirebaseAuth.getInstance();
        myRef= FirebaseDatabase.getInstance().getReference();
        this.context=context;
    }
    public void registerNewUser(final String email, final String username, final String password, final DotProgressBar mProgress){

       mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   Snackbar.make(((Activity) context).getCurrentFocus().getRootView(),"Registration done succesfully !!",Snackbar.LENGTH_SHORT).show();
                   context.startActivity(new Intent(context,LoginActivity.class));
                   User user = new User(email,username,password,getDateToday(),"Online",mAuth.getCurrentUser().getUid());
                    UserAccountSettings userAccountSettings= new UserAccountSettings(email,username,password,"Hey There I am using Whatsapp",getDateToday()+" "+getTimeNow(),getDateToday(),"");
                   addUserToDB(user,userAccountSettings);
                   mProgress.setVisibility(View.GONE);
               }else{
                   Snackbar.make(((Activity) context).getCurrentFocus().getRootView(),"Registration failed !!",Snackbar.LENGTH_SHORT).show();
               }
           }
       });
    }

    private void addUserToDB(User user, UserAccountSettings userAccountSettings) {
        myRef.child(context.getString(R.string.dbname_users)).child(mAuth.getCurrentUser().getUid()).setValue(user);
        myRef.child(context.getString(R.string.dbname_user_account_settings)).child(mAuth.getCurrentUser().getUid()).setValue(userAccountSettings);
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
    public void loginUser(String email, String password, final DotProgressBar mProgress){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity)context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           context.startActivity(new Intent(context, HomeActivity.class));
                           myRef.child(context.getString(R.string.dbname_users)).child(mAuth.getCurrentUser().getUid()).child(context.getString(R.string.dbname_last_seen)).setValue("Online");
                           mProgress.setVisibility(View.GONE);
                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar.make(((Activity) context).getCurrentFocus().getRootView(),"Sign In Failed !!",Snackbar.LENGTH_SHORT).show();
                            mProgress.setVisibility(View.GONE);
                        }

                        // ...
                    }
                });
    }
}
