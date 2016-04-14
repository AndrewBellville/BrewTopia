package com.town.small.brewtopia.Brews;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.town.small.brewtopia.AppSettings.AppSettings;
import com.town.small.brewtopia.R;
import com.town.small.brewtopia.Utilites.SlidingTabLayout;
import com.town.small.brewtopia.UserInventory;

public class UserBrew extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "UserBrew";

    private Toolbar toolbar;
    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_brew);
        Log.e(LOG, "Entering: onCreate");

        toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(BrewActivityData.getInstance().getAddEditViewState() == BrewActivityData.DisplayMode.ADD)
            setTitle("Create");
        else
            setTitle(BrewActivityData.getInstance().getAddEditViewBrew().getBrewName());


        viewPager = (ViewPager)findViewById(R.id.brewPager);
        PagerAdapter pa = new PagerAdapter(getSupportFragmentManager());
        pa.setContext(getApplicationContext());
        viewPager.setAdapter(pa);

        tabLayout = (SlidingTabLayout)findViewById(R.id.brewTabs);
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
            tabNames = new  String[]{"Brews","Additions","Notes","Inventory"};
            icons = new  int[]{R.drawable.beer_dark,R.drawable.plus_icon,R.drawable.notes_icon,R.drawable.document};
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new AddEditViewBrew();
            if(position == 0)
                fragment = new AddEditViewBrew();
            else if (position == 1)
                fragment = new AddEditViewBoilAdditions();
            else if (position == 2)
                fragment = new AddEditViewBrewNotes();
            else if (position == 3)
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
            return 4;
        }
        public void setContext(Context context) {
            this.context = context;
        }
    }

}



