package com.dev.town.small.brewtopia.Schedule;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import com.dev.town.small.brewtopia.AppSettings.AppSettings;
import com.dev.town.small.brewtopia.Brews.AddEditViewBoilAdditions;
import com.dev.town.small.brewtopia.Brews.AddEditViewBrew;
import com.dev.town.small.brewtopia.Brews.AddEditViewBrewNotes;
import com.dev.town.small.brewtopia.Brews.AddEditViewBrewStyle;
import com.dev.town.small.brewtopia.Brews.BrewActivityData;
import com.dev.town.small.brewtopia.Brews.UserCompletedBrewList;
import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.Inventory.UserInventory;
import com.dev.town.small.brewtopia.R;
import com.dev.town.small.brewtopia.Utilites.SlidingTabLayout;

public class UserSchedule extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "UserBrew";

    private Toolbar toolbar;
    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_schedule);
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onCreate");

        toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Schedule");

        viewPager = (ViewPager)findViewById(R.id.schedulePager);
        PagerAdapter pa = new PagerAdapter(getSupportFragmentManager());
        pa.setContext(getApplicationContext());
        viewPager.setAdapter(pa);

        tabLayout = (SlidingTabLayout)findViewById(R.id.scheduleTabs);
        tabLayout.setCustomTabView(R.layout.custom_tab_view, R.id.tabTextView);
        tabLayout.setDistributeEvenly(true);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.ColorToneL2));
        tabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.ColorToneD1));

        tabLayout.setViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_brews, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_help:
                //showHelp();
                return true;
            case R.id.action_settings:
                showSettings();
                return true;
            default:
                onBackPressed();
                return true;
        }
    }

    private void showSettings()
    {
        //Create and intent which will open next activity AppSettings
        Intent intent = new Intent(this, AppSettings.class);
        startActivity(intent);
    }

    private class PagerAdapter extends FragmentPagerAdapter {


        int[] icons;
        String[] tabNames;
        Context context;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            tabNames = new  String[]{"Schedule","Inventory"};
            icons = new  int[]{R.drawable.schedule,R.drawable.document};
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new AddEditViewBrew();
            if(position == 0)
                fragment = new AddEditViewSchedule();
            else if (position == 1)
                fragment = new UserInventory();

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int possition)
        {

            Drawable drawable = ContextCompat.getDrawable(context,icons[possition]);
            drawable.setBounds(0,0,90,90);
            ImageSpan imageSpan=  new ImageSpan(drawable);
            SpannableString spannableString = new SpannableString(" ");
            spannableString.setSpan(imageSpan,0,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            return spannableString;
        }

        @Override
        public int getCount() {
            return 2;
        }
        public void setContext(Context context) {
            this.context = context;
        }
    }

}



