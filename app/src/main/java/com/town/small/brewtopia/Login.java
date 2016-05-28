package com.town.small.brewtopia;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.town.small.brewtopia.AppSettings.AppSettingsHelper;
import com.town.small.brewtopia.DataClass.*;


public class Login extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "Login";
    private static final String VERSION = "v0.1.0.0";

    private EditText userName;
    private EditText password;
    private TextView message;
    private TextView version;
    private DataBaseManager dbManager;
    private CurrentUser currentUser;
    AppSettingsHelper  appSettingsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.e(LOG, "Entering: onCreate");
        dbManager = DataBaseManager.getInstance(getApplicationContext());
        currentUser = CurrentUser.getInstance();

        appSettingsHelper = AppSettingsHelper.getInstance(this);

        userName = (EditText)findViewById(R.id.UserNameTextBox);
        password = (EditText)findViewById(R.id.PasswordTextBox);
        message = (TextView)findViewById(R.id.MessageText);
        version = (TextView)findViewById(R.id.verionNumber);
        version.setText(VERSION);
    }

    private boolean validateUserLogin()
    {
        Log.e(LOG, "Entering: validateUserLogin");

        //TODO better verification
        //verify user record exists
        return dbManager.DoesUserLoginExist(userName.getText().toString(),password.getText().toString());
    }

    private boolean validateUserCreate()
    {
        Log.e(LOG, "Entering: validateUserCreate");

        //TODO better verification
        //create user schema from XML field data and create user in DB
        UserSchema user = new UserSchema(userName.getText().toString(),password.getText().toString());

        if(dbManager.CreateAUser(user)) {
            user = dbManager.getUser(userName.getText().toString());
            appSettingsHelper.CreateAppSettings(user.getUserId());
            return true;
        }

        return false;
    }

    public void onLoginClick(View aView)
    {
        Log.e(LOG, "Entering: onLoginClick");

        if (validateUserLogin())
        {
            //Create and intent which will open next activity UserProfile
            Intent intent = new Intent(this, UserProfile.class);
            //set active user
            currentUser.setUser(dbManager.getUser(userName.getText().toString()));
            //load all app setting for user
            appSettingsHelper.LoadMap();
            //start next activity
            startActivity(intent);
            message.setText("");
        }
        else
        {
            message.setText("Failed Login");
        }
        message.invalidate();
    }

    public void onCreateClick(View aView)
    {
        Log.e(LOG, "Entering: onCreateClick");

        if (validateUserCreate())
        {
            message.setText("Successfully Created");
        }
        else {
            message.setText("User Name Already Exists");
        }
        message.invalidate();
    }
}
