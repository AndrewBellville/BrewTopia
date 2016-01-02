package com.town.small.brewtopia;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.town.small.brewtopia.Brews.UserBrews;
import com.town.small.brewtopia.DataClass.CurrentUser;


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
        //Add image tab
        intent = new Intent(this, UserBrews.class);
        tabHost.addTab(tabHost.newTabSpec("brews").setIndicator("Brews").setContent(intent));

        //Add Reference Object tab
        intent = new Intent(this, UserSchedule.class);
        tabHost.addTab(tabHost.newTabSpec("schedule").setIndicator("Schedule").setContent(intent));

        //Add Inventory tab
        intent = new Intent(this, UserInventory.class);
        tabHost.addTab(tabHost.newTabSpec("inventory").setIndicator("Inventory").setContent(intent));

        //Add Calculations tab
        intent = new Intent(this, UserCalculations.class);
        tabHost.addTab(tabHost.newTabSpec("calculations").setIndicator("Calculations").setContent(intent));
    }
}
