package com.town.small.brewtopia;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

import com.town.small.brewtopia.Brews.UserBrews;
import com.town.small.brewtopia.Calculations.UserCalculations;
import com.town.small.brewtopia.DataClass.CurrentUser;
import com.town.small.brewtopia.Schedule.UserSchedule;


public class UserProfile extends TabActivity {

    // Log cat tag
    private static final String LOG = "UserProfile";

    private String userName;
    private CurrentUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Log.e(LOG, "Entering: onCreate");
        currentUser = CurrentUser.getInstance();
        userName = currentUser.getUser().getUserName();
        // display in the action bar
        //getActionBar().setTitle(userName);
        getActionBar().hide();
        Log.e(LOG, "User profile for ["+ userName +"] ");

        TabHost tabHost = getTabHost();

        Intent intent;
        //Add Brew tab
        intent = new Intent(this, UserBrews.class);
        tabHost.addTab(tabHost.newTabSpec("brews").setIndicator("",getApplicationContext().getResources().getDrawable(R.drawable.beer_dark)).setContent(intent));

        //Add Schedule tab
        intent = new Intent(this, UserSchedule.class);
        tabHost.addTab(tabHost.newTabSpec("schedule").setIndicator("",getApplicationContext().getResources().getDrawable(R.drawable.calendar)).setContent(intent));

        //Add Inventory tab
        intent = new Intent(this, UserInventory.class);
        tabHost.addTab(tabHost.newTabSpec("inventory").setIndicator("",getApplicationContext().getResources().getDrawable(R.drawable.document)).setContent(intent));

        //Add Calculations tab
        intent = new Intent(this, UserCalculations.class);
        tabHost.addTab(tabHost.newTabSpec("calculations").setIndicator("",getApplicationContext().getResources().getDrawable(R.drawable.calculator)).setContent(intent));
    }
}
