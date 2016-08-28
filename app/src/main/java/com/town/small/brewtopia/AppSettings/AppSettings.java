package com.town.small.brewtopia.AppSettings;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.town.small.brewtopia.DataClass.AppSettingsSchema;
import com.town.small.brewtopia.DataClass.CurrentUser;
import com.town.small.brewtopia.DataBase.DataBaseManager;
import com.town.small.brewtopia.R;

import java.util.ArrayList;
import java.util.List;

public class AppSettings extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "AppSettings";

    private Toolbar toolbar;
    private ListView settingsListView;
    private ListView otherSettingsListView;
    private List<AppSettingsSchema> settingsList;
    private List<AppSettingsSchema> otherSettingsList;
    private DataBaseManager dbManager;
    private long userId;
    private AppSettingsHelper appSettingsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);

        toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Settings");

        dbManager = DataBaseManager.getInstance(this);
        appSettingsHelper = AppSettingsHelper.getInstance(this);
        userId = CurrentUser.getInstance().getUser().getUserId();

        settingsList = new ArrayList<>();
        settingsListView = (ListView) findViewById(R.id.appSettingsListView);
        settingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AppSettingsSchema selectedRow = settingsList.get(position);
                SettingSelected(selectedRow);
            }
        });

        otherSettingsList = new ArrayList<>();
        otherSettingsListView = (ListView) findViewById(R.id.OtherSettingsListView);
        otherSettingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AppSettingsSchema selectedRow = otherSettingsList.get(position);
                //SettingSelected(selectedRow);
            }
        });

        LoadSettings();

    }

    private void LoadSettings() {
        Log.e(LOG, "Entering: LoadSettings");

        for (AppSettingsSchema appSettingsSchema : dbManager.getAllAppSettingsByUserId(userId))
        {
            if(appSettingsSchema.getSettingScreen().equals(appSettingsHelper.SCHEDULER))
            {
                settingsList.add(appSettingsSchema);
            }
            else if(appSettingsSchema.getSettingScreen().equals(appSettingsHelper.OTHER))
            {
                otherSettingsList.add(appSettingsSchema);
            }
        }

        if (settingsList.size() > 0) {
            //instantiate custom adapter
            CustomASListAdapter adapter = new CustomASListAdapter(settingsList, this);
            adapter.setEditable(true);
            settingsListView.setAdapter(adapter);
        }

        if (otherSettingsList.size() > 0) {
            //instantiate custom adapter
            CustomASListAdapter adapter = new CustomASListAdapter(otherSettingsList, this);
            adapter.setEditable(false);
            otherSettingsListView.setAdapter(adapter);
        }
    }

    private void SettingSelected(AppSettingsSchema selectedRow)
    {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
