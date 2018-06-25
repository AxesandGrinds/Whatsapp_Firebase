package com.example.sachi.whatsappmodme.Home;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sachi.whatsappmodme.Dialogs.RateDialog;
import com.example.sachi.whatsappmodme.Models.Chats;
import com.example.sachi.whatsappmodme.Models.User;
import com.example.sachi.whatsappmodme.Models.UserAccountSettings;
import com.example.sachi.whatsappmodme.R;
import com.example.sachi.whatsappmodme.Settings.SettingsActivity;
import com.example.sachi.whatsappmodme.Utils.ChatsListAdapter;
import com.example.sachi.whatsappmodme.Utils.SearchListAdapter;
import com.example.sachi.whatsappmodme.Utils.UniversalImageLoader;
import com.example.sachi.whatsappmodme.Utils.ViewPagerAdapter;
import com.example.sachi.whatsappmodme.signup.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private NavigationView navigation;
    private TabLayout tabLayout;
    private ViewPager viewpager;
    private ImageView search;
    //NavigationView widgets
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private  boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth=FirebaseAuth.getInstance();
        myRef= FirebaseDatabase.getInstance().getReference();
        setupToolbar();
        setupNavigationView();
        InitImageLoader();

    }

    private void setupNavigationView() {
        drawer=(DrawerLayout) findViewById(R.id.drawer);
        toggle=new ActionBarDrawerToggle(HomeActivity.this,drawer,R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.syncState();
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case R.id.about_developer :
                        Intent i = new Intent(HomeActivity.this,SettingsActivity.class);
                        i.putExtra("navigate","about_dev");
                        startActivity(i);
                    break;
                    case R.id.settings : startActivity(new Intent(HomeActivity.this,SettingsActivity.class));
                    break;
                    case R.id.signout :
                        myRef.child(getString(R.string.dbname_users)).child(mAuth.getCurrentUser().getUid()).child(getString(R.string.dbname_last_seen)).setValue(getTimeNow());
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        finish();
                        break;
                    case R.id.share_app : shareApp();break;
                    case R.id.rate_us :
                        RateDialog dialog = new RateDialog();
                        dialog.show(getSupportFragmentManager(),"fm");
                        break;

                }
                return false;
            }
        });
        View v = navigation.getHeaderView(0);
        final CircleImageView profile_pic = v.findViewById(R.id.profile_pic);
        final TextView username=v.findViewById(R.id.username);
        Query query=myRef.child(getString(R.string.dbname_user_account_settings)).child(mAuth.getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserAccountSettings settings = dataSnapshot.getValue(UserAccountSettings.class);
                ImageLoader loader = ImageLoader.getInstance();
                loader.displayImage(settings.getProfile_photo_url(),profile_pic);
                username.setText(settings.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomeActivity.this,SettingsActivity.class);
                i.putExtra("navigate","profile_pic");
                startActivity(i);
            }
        });
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomeActivity.this,SettingsActivity.class);
                i.putExtra("navigate","username");
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        tabLayout=(TabLayout) findViewById(R.id.tab_layout);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        search=(ImageView) findViewById(R.id.search);
        viewpager=(ViewPager) findViewById(R.id.container);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recyclerview);

                tabLayout.setVisibility(View.GONE);
                LayoutInflater inflater= LayoutInflater.from(HomeActivity.this);
                View view = inflater.inflate(R.layout.appbar_search_view,null);
                toolbar.addView(view);
               // toggle.setDrawerIndicatorEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                search.setImageResource(R.drawable.ic_cross);
                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recreate();
                        tabLayout.getTabAt(1).select();
                    }
                });
                EditText search_for = view.findViewById(R.id.search_for);
                search_for.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                       Query query =  myRef.child(getString(R.string.dbname_user_account_settings)).orderByChild(getString(R.string.dbname_username)).equalTo(s.toString());
                       query.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                              if(dataSnapshot.exists()){
                                  for(DataSnapshot ds : dataSnapshot.getChildren()){
                                      //usernames retrieved by using ds.getValue(User.class).getUsernam
                                      ArrayList<Chats> users = new ArrayList<>();
                                      Chats chats = new Chats();
                                      Log.d("sachin","datasnapshot for search found");
                                      chats.setMessage(ds.getValue(UserAccountSettings.class).getAbout());
                                      chats.setProfile_pic_url(ds.getValue(UserAccountSettings.class).getProfile_photo_url());
                                      chats.setTimestamp("");
                                      chats.setUsername(ds.getValue(UserAccountSettings.class).getUsername());
                                      users.add(chats);
                                      RecyclerView.LayoutManager manager = new LinearLayoutManager(HomeActivity.this);
                                      recyclerView.setLayoutManager(manager);
                                      recyclerView.setItemAnimator(new DefaultItemAnimator());
                                      SearchListAdapter adapter = new SearchListAdapter(HomeActivity.this,users);
                                      recyclerView.setAdapter(adapter);
                                  }
                              }
                           }



                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });

                    }
                });
                ;

            }
        });

        navigation=(NavigationView) findViewById(R.id.navigation);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragments(new CallsFragment());
        adapter.addFragments(new ChatsFragment());
        adapter.addFragments(new StatusFragment());
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.getTabAt(0).setText("CALLS");
        tabLayout.getTabAt(1).setText("CHATS");
        tabLayout.getTabAt(2).setText("STATUS");
        tabLayout.getTabAt(1).select();
    }
    private String getTimeNow(){

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("E,dd MMM hh:mm a");
        return sdf.format(date);
    }
    private void InitImageLoader(){
        Log.d("sachin","initializing image loader in home");
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(HomeActivity.this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }
    private void shareApp(){

            ApplicationInfo app = getApplicationContext().getApplicationInfo();
            String filePath = app.sourceDir;

            Intent intent = new Intent(Intent.ACTION_SEND);

            // MIME of .apk is "application/vnd.android.package-archive".
            // but Bluetooth does not accept this. Let's use "*/*" instead.
            intent.setType("*/*");

            // Append file and send Intent
            File originalApk = new File(filePath);

            try {
                //Make new directory in new location
                File tempFile = new File(getExternalCacheDir() + "/ExtractedApk");
                //If directory doesn't exists create new
                if (!tempFile.isDirectory())
                    if (!tempFile.mkdirs())
                        return;
                //Get application's name and convert to lowercase
                tempFile = new File(tempFile.getPath() + "/" + getString(app.labelRes).replace(" ","").toLowerCase() + ".apk");
                //If file doesn't exists create new
                if (!tempFile.exists()) {
                    if (!tempFile.createNewFile()) {
                        return;
                    }
                }
                //Copy file to new location
                InputStream in = new FileInputStream(originalApk);
                OutputStream out = new FileOutputStream(tempFile);

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                System.out.println("File copied.");
                //Open share dialog
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
                startActivity(Intent.createChooser(intent, "Share app via"));

            } catch (IOException e) {
                e.printStackTrace();
            }

    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
             finishAffinity();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
