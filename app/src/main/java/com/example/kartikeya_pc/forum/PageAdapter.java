package com.example.kartikeya_pc.forum;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class PageAdapter extends FragmentStatePagerAdapter {


    public PageAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        //return fragments.get(position);
        switch (position) {
            case 0:
                return new Tab1();
            case 1:
                return new Tab2();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
