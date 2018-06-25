package com.example.sachi.whatsappmodme.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentArrayList ;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentArrayList=new ArrayList<>();
    }
    public void addFragments(Fragment fragment){
        fragmentArrayList.add(fragment);
    }
    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }
}
