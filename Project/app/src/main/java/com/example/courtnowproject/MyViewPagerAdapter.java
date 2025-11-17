package com.example.courtnowproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class MyViewPagerAdapter extends FragmentPagerAdapter {
    public MyViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                return Step1B.getInstance();
            case 1:
                return Step2B.getInstance();
            case 2:
                return Step3B.getInstance();
            case 3:
                return Step4B.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
