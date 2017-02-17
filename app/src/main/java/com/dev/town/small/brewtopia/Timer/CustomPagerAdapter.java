package com.dev.town.small.brewtopia.Timer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Andrew on 3/26/2016.
 */
public class CustomPagerAdapter extends FragmentPagerAdapter {


    public CustomPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return new BrewTimer();
            case 1:
                return new TimerBoilAdditions();
            default:
                return new BrewTimer();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
