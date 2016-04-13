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
import com.town.small.brewtopia.R;

import java.util.ArrayList;
import java.util.List;

public class AppSettings extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "AppSettings";

    private Toolbar toolbar;
    private ListView settingsListView;
    List<AppSettingsSchema> settingsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);

        toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Settings");

        settingsListView = (ListView) findViewById(R.id.appSettingsListView);
        settingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AppSettingsSchema selectedRow = settingsList.get(position);
                SettingSelected(selectedRow);
            }
        });

        LoadSettings();

    }

    private void LoadSettings() {
        Log.e(LOG, "Entering: LoadSettings");

        settingsList = new ArrayList<AppSettingsSchema>();
        AppSettingsSchema appSettingsSchema = new AppSettingsSchema();
        appSettingsSchema.setSettingName("Brew");
        appSettingsSchema.setSettingValue("0");
        settingsList.add(appSettingsSchema);
        appSettingsSchema = new AppSettingsSchema();
        appSettingsSchema.setSettingName("Schedule");
        appSettingsSchema.setSettingValue("0");
        settingsList.add(appSettingsSchema);
        appSettingsSchema = new AppSettingsSchema();
        appSettingsSchema.setSettingName("Invetory");
        appSettingsSchema.setSettingValue("0");
        settingsList.add(appSettingsSchema);

        if (settingsList.size() > 0) {

            //instantiate custom adapter
            CustomASListAdapter adapter = new CustomASListAdapter(settingsList, this);
            adapter.setEditable(true);
            settingsListView.setAdapter(adapter);
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
