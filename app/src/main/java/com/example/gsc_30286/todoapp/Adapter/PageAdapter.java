package com.example.gsc_30286.todoapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.gsc_30286.todoapp.Fragment.CompletedTask;
import com.example.gsc_30286.todoapp.Fragment.RemainingTask;

public class PageAdapter extends FragmentStatePagerAdapter {

    private int numberOfTabs;

    public PageAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:{
                RemainingTask remainingTask = new RemainingTask();
                return remainingTask;
            }
            case 1:{
                CompletedTask completedTask = new CompletedTask();
                return completedTask;
            }
            default:{
                return null;
            }
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
