package com.dev.town.small.brewtopia.AppSettings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.DataClass.AppSettingsSchema;
import com.dev.town.small.brewtopia.DataClass.CurrentUser;
import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.DataClass.UserSchema;
import com.dev.town.small.brewtopia.Login;
import com.dev.town.small.brewtopia.R;
import com.dev.town.small.brewtopia.WebAPI.DeleteUserRequest;
import com.dev.town.small.brewtopia.WebAPI.JSONUserParser;
import com.dev.town.small.brewtopia.WebAPI.UpdateUserSettingsRequest;
import com.dev.town.small.brewtopia.WebAPI.WebController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppSettings extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "AppSettings";

    private Toolbar toolbar;
    private ListView settingsListView;
    private ListView dataSettingsListView;
    private ListView otherSettingsListView;
    private ListView userSettingsListView;
    private List<AppSettingsSchema> settingsList;
    private List<AppSettingsSchema> dataSettingsList;
    private List<AppSettingsSchema> otherSettingsList;
    private List<AppSettingsSchema> userSettingsList;
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

        dataSettingsList = new ArrayList<>();
        dataSettingsListView = (ListView) findViewById(R.id.DataSettingsListView);
        dataSettingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AppSettingsSchema selectedRow = dataSettingsList.get(position);
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
                SettingSelected(selectedRow);
            }
        });


        userSettingsList = new ArrayList<>();
        userSettingsListView = (ListView) findViewById(R.id.UserSettingsListView);
        userSettingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AppSettingsSchema selectedRow = userSettingsList.get(position);
                SettingSelected(selectedRow);
            }
        });

        LoadSettings();

    }

    private void LoadSettings() {
        if(APPUTILS.isLogging) Log.e(LOG, "Entering: LoadSettings");

        for (AppSettingsSchema appSettingsSchema : dbManager.getAllAppSettingsByUserId(userId))
        {
            if(appSettingsSchema.getSettingScreen().equals(appSettingsHelper.SCHEDULER))
            {
                settingsList.add(appSettingsSchema);
            }
            else if(appSettingsSchema.getSettingScreen().equals(appSettingsHelper.DATA))
            {
                dataSettingsList.add(appSettingsSchema);
            }
            else if(appSettingsSchema.getSettingScreen().equals(appSettingsHelper.OTHER))
            {
                otherSettingsList.add(appSettingsSchema);
            }
            else if(appSettingsSchema.getSettingScreen().equals(appSettingsHelper.USER))
            {
                userSettingsList.add(appSettingsSchema);
            }
        }

        if (settingsList.size() > 0) {
            //instantiate custom adapter
            CustomASListAdapter adapter = new CustomASListAdapter(settingsList, this);
            adapter.setEditable(true);
            settingsListView.setAdapter(adapter);
        }

        if (dataSettingsList.size() > 0) {
            //instantiate custom adapter
            CustomASListAdapter adapter = new CustomASListAdapter(dataSettingsList, this);
            adapter.setEditable(true);
            dataSettingsListView.setAdapter(adapter);
        }

        if (otherSettingsList.size() > 0) {
            //instantiate custom adapter
            CustomASListAdapter adapter = new CustomASListAdapter(otherSettingsList, this);
            adapter.setEditable(true);
            otherSettingsListView.setAdapter(adapter);
        }

        if (userSettingsList.size() > 0) {
            //instantiate custom adapter
            CustomASListAdapter adapter = new CustomASListAdapter(userSettingsList, this);
            adapter.setEditable(false);
            userSettingsListView.setAdapter(adapter);
        }
    }

    private void SettingSelected(AppSettingsSchema selectedRow)
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: SettingSelected " + selectedRow.getSettingName());
        if(selectedRow.getSettingName().equals(AppSettingsHelper.USER_DELETE_USER))
            Verify();
        if(selectedRow.getSettingName().equals(AppSettingsHelper.USER_CHANGE_USER_INFO))
            UpdateUserInfo();
    }


    private void DeleteUser()
    {
        if(APPUTILS.isLogging)Log.i(LOG, "Entering: DeleteUser");

        if(!APPUTILS.HasInternet(this)) {
            Toast.makeText(this, "Need Internet To Perform", Toast.LENGTH_SHORT).show();
            return;
        }

        Response.Listener<String> ResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(APPUTILS.isLogging)Log.e(LOG, response);
                if (!response.equals("Error"))// no match for user exists
                {
                    dbManager.deleteUserById(CurrentUser.getInstance().getUser().getUserId());
                    Toast.makeText(getApplication(),"Delete Successful", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplication(), Login.class);
                    //start next activity
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplication(),"Delete Failed", Toast.LENGTH_SHORT).show();
                }
            }
        };


        DeleteUserRequest deleteUserRequest = new DeleteUserRequest(Long.toString(CurrentUser.getInstance().getUser().getUserId()), ResponseListener,null);
        WebController.getInstance().addToRequestQueue(deleteUserRequest);
    }

    private void Verify()
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        DeleteUser();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    private void UpdateUserInfo() {
        Intent intent = new Intent(getApplication(), UpdateUserInfo.class);
        //start next activity
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        //try and update setting to server
        UpdateUserSettings();
    }

    private void UpdateUserSettings()
    {

        if(!APPUTILS.HasInternet(this)) {
            Toast.makeText(this, "No Internet cannot save to global settings", Toast.LENGTH_SHORT).show();
            return;
        }

        Response.Listener<String> ResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(APPUTILS.isLogging)Log.e(LOG, response);
                try{
                    JSONArray jsonArray = new JSONArray("["+response+"]");
                    JSONObject JSONResponse = (JSONObject) jsonArray.get(0);
                    if (JSONResponse.getString("status").equals("Error"))// no match for user exists
                    {
                        DisplayMessage(JSONResponse.getString("message"));
                    } else {

                        DisplayMessage("User Settings Updated");
                    }
                }
                catch (JSONException e) {
                    DisplayMessage("User Settings Update Failed");
                }
            }
        };

        UpdateUserSettingsRequest createUserRequest = new UpdateUserSettingsRequest(CurrentUser.getInstance().getUser(),ResponseListener,null);
        WebController.getInstance().addToRequestQueue(createUserRequest);
    }

    private void DisplayMessage(String errorText)
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: DisplayMessage");

        Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
