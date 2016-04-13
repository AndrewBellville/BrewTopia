package com.town.small.brewtopia;


import android.app.usage.UsageEvents;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.town.small.brewtopia.AppSettings.AppSettings;
import com.town.small.brewtopia.Brews.UserBrewList;
import com.town.small.brewtopia.Calculations.UserCalculations;
import com.town.small.brewtopia.Schedule.UserSchedule;
import com.town.small.brewtopia.Tabs.SlidingTabLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class UserProfile extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "UserProfile";

    private Toolbar toolbar;
    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Log.e(LOG, "Entering: onCreate");

        toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("BrewTopia");

        viewPager = (ViewPager)findViewById(R.id.profilePager);
        PagerAdapter pa = new PagerAdapter(getSupportFragmentManager());
        pa.setContext(getApplicationContext());
        viewPager.setAdapter(pa);

        tabLayout = (SlidingTabLayout)findViewById(R.id.profileTabs);
        tabLayout.setCustomTabView(R.layout.custom_tab_view, R.id.tabTextView);
        tabLayout.setDistributeEvenly(true);

        tabLayout.setBackgroundColor(getResources().getColor(R.color.ColorToneL2));
        tabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.ColorToneD1));

        tabLayout.setViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_help:
                showHelp();
                return true;
            case R.id.action_settings:
                showSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSettings()
    {
        //Create and intent which will open next activity AppSettings
        Intent intent = new Intent(this, AppSettings.class);
        startActivity(intent);
    }

    private void showHelp()
    {
        Toast.makeText(this, "No Help for you Sucka", Toast.LENGTH_LONG).show();
    }

    private class PagerAdapter extends FragmentPagerAdapter {


        int[] icons;
        String[] tabNames;
        Context context;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            tabNames = new  String[]{"Brews","Schedule","Inveotry","Calculations"};
            icons = new  int[]{R.drawable.beer_dark,R.drawable.calendar,R.drawable.document,R.drawable.calculator};
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new UserBrewList();
            if(position == 0)
                fragment = new UserBrewList();
            else if (position == 1)
                fragment = new UserSchedule();
            else if (position == 2)
                fragment = new UserInventory();
            else if (position == 3)
                fragment = new UserCalculations();

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int possition)
        {

            Drawable drawable = ContextCompat.getDrawable(context,icons[possition]);
            drawable.setBounds(0,0,90,90);
            ImageSpan imageSpan=  new ImageSpan(drawable);
            SpannableString  spannableString = new SpannableString(" ");
            spannableString.setSpan(imageSpan,0,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            return spannableString;
        }

        @Override
        public int getCount() {
            return 4;
        }
        public void setContext(Context context) {
            this.context = context;
        }
    }
}



