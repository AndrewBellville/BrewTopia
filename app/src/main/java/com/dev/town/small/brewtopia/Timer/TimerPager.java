package com.dev.town.small.brewtopia.Timer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.dev.town.small.brewtopia.R;


public class TimerPager extends FragmentActivity {

    ViewPager timerPager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);
        //getActionBar().hide();

        timerPager = (ViewPager)findViewById(R.id.pager);
        CustomPagerAdapter pagerAdapter  =  new CustomPagerAdapter(getSupportFragmentManager());

        timerPager.setAdapter(pagerAdapter);
    }

}
