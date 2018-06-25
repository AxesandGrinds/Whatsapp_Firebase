package com.example.sachi.whatsappmodme.Settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

public class ViewProfilePicFragment extends Fragment {
    ImageView profile_pic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_view_profile_pic,container,false);
        ImageView back = v.findViewById(R.id.back_arrow);
        ImageView change_dp = v.findViewById(R.id.change_dp);
        profile_pic=v.findViewById(R.id.dp);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = getArguments();
                String value=null;
                if(b!=null){
                value = b.getString("navigate");
                if(value.equals("home")) {
                    getActivity().finish();
                }}else
                getActivity().getSupportFragmentManager().popBackStack();
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
        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();
        Query query = myRef.child(getString(R.string.dbname_user_account_settings)).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(UserAccountSettings.class).getProfile_photo_url();
                ImageLoader loader = ImageLoader.getInstance();
                loader.displayImage(url,profile_pic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
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
    }
}}
