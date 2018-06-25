package com.example.sachi.whatsappmodme.Settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sachi.whatsappmodme.Dialogs.ChangeAboutDialog;
import com.example.sachi.whatsappmodme.Dialogs.ChangeUsernameDialog;
import com.example.sachi.whatsappmodme.Models.UserAccountSettings;
import com.example.sachi.whatsappmodme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements ChangeUsernameDialog.send,ChangeAboutDialog.send{
    public void sendUsername(String Username){
        username.setText(Username);
    }
    public void sendAbout(String About){
        about.setText(About);
    }
    CircleImageView profile_pic;
    FloatingActionButton change_dp;
    ImageView change_username,back;
    TextView about,joindate,username;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile,container,false);
        profile_pic=(CircleImageView) v.findViewById(R.id.profile_pic);
        change_dp=(FloatingActionButton) v.findViewById(R.id.change_profile_pic);
        change_username=(ImageView) v.findViewById(R.id.change_username);
        about=(TextView) v.findViewById(R.id.about);
        joindate=(TextView) v.findViewById(R.id.joindate);
        username=(TextView) v.findViewById(R.id.username);
        back=(ImageView) v.findViewById(R.id.back_arrow);

        setProfileValues();
        setListeners();
        return v;
    }
    private void setProfileValues(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();
        final ImageLoader loader = ImageLoader.getInstance();
        Query query = myRef.child(getString(R.string.dbname_user_account_settings)).child(mAuth.getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserAccountSettings settings = dataSnapshot.getValue(UserAccountSettings.class);
                about.setText(settings.getAbout());
                username.setText(settings.getUsername());
                loader.displayImage(settings.getProfile_photo_url(),profile_pic);
                joindate.setText("Joined whatsapp on : "+settings.getJoin_date());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setListeners(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = getArguments();
                if(b!=null){
                    String value = b.getString("navigate");
                    if(value.equals("profile_fragment")){
                        getActivity().finish();
                    }
                }
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        change_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeUsernameDialog dialog = new ChangeUsernameDialog();
                dialog.setTargetFragment(ProfileFragment.this,1);
                dialog.setCancelable(false);
                dialog.show(getActivity().getSupportFragmentManager(),username.getText().toString());
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeAboutDialog dialog = new ChangeAboutDialog();
                dialog.setTargetFragment(ProfileFragment.this,1);
                dialog.setCancelable(false);
                dialog.show(getActivity().getSupportFragmentManager(),about.getText().toString());
            }
        });
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewProfilePicFragment fragment =new ViewProfilePicFragment();
                FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.replace,fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        change_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] options = {"Take Photo","Choose from Gallery","Remove Photo","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose..");
                builder.setCancelable(false);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(options[which].equals("Take Photo")){
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent,1);
                        }else if(options[which].equals("Choose from Gallery")){
                            Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent,2);
                        }else if(options[which].equals("Remove Photo")){
                           //Delete photo from firebase storage
                            StorageReference storage = FirebaseStorage.getInstance().getReference();
                            StorageReference filepath = storage.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_photo");
                            filepath.delete();
                        }else if(options[which].equals("Cancel")){
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                final ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Uploading..");
                dialog.show();
                Bundle extras = data.getExtras();
                Bitmap bmp=(Bitmap) extras.get("data");
                profile_pic.setImageBitmap(bmp);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG,100,baos);
                byte[] byteArray = baos.toByteArray();
                StorageReference storage = FirebaseStorage.getInstance().getReference();
                final StorageReference filepath = storage.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_photo");
                filepath.putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();
                                myRef.child(getString(R.string.dbname_user_account_settings)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getString(R.string.dbname_profile_pic)).setValue(uri.toString());
                                dialog.dismiss();
                            }
                        });
                    }
                });
            }else if(requestCode==2){
                 final ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Uploading..");
                dialog.show();
                Uri selectedImage = data.getData();
                StorageReference storage = FirebaseStorage.getInstance().getReference();
                final StorageReference filepath = storage.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_photo");
                filepath.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();
                                myRef.child(getString(R.string.dbname_user_account_settings)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getString(R.string.dbname_profile_pic)).setValue(uri.toString());
                                ImageLoader loader = ImageLoader.getInstance();
                                loader.displayImage(uri.toString(),profile_pic);
                                dialog.dismiss();
                            }
                        });

                    }
                });

            }
}}}
