package com.town.small.brewtopia;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.town.small.brewtopia.AppSettings.AppSettingsHelper;
import com.town.small.brewtopia.DataBase.DataBaseManager;
import com.town.small.brewtopia.DataClass.*;
import com.town.small.brewtopia.WebAPI.CreateUserRequest;
import com.town.small.brewtopia.WebAPI.LoginRequest;


public class Login extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "Login";
    private static final String VERSION = "v0.1.0.1";

    private EditText userName;
    private EditText password;
    private TextView message;
    private TextView version;
    private TextView noLogin;
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
        noLogin = (TextView)findViewById(R.id.textViewNoLogin);
        version.setText(VERSION);
    }

    //******************Login****************************
    public void onLoginClick(View aView)
    {
        Log.e(LOG, "Entering: onLoginClick");
        validateUserLogin();
    }

    private void validateUserLogin()
    {
        Log.e(LOG, "Entering: validateUserLogin");

        //TODO better verification
        //verify user record exists
        Response.Listener<String> ResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(LOG, response);
                if (response.equals("Error"))// no match for user password
                {
                    performLogIn(false,-1);
                } else {
                    performLogIn(true, Long.parseLong(response));
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(userName.getText().toString(),password.getText().toString(),ResponseListener,null);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(loginRequest);
    }

    private void performLogIn(boolean isSuccess, long aUserId)
    {
        Log.e(LOG, "Entering: perfromLogin");

        if (isSuccess)
        {
            //Create and intent which will open next activity UserProfile
            Intent intent = new Intent(this, UserProfile.class);
            //set active user
            //dbManager.SyncUser();
            currentUser.setUser(dbManager.getUser(aUserId));
            //load all app setting for user
            appSettingsHelper.LoadMap();
            //start next activity
            startActivity(intent);
            message.setText("");
        }
        else
        {
            // if we have failed login from server try and login from local DB
            long userId =  dbManager.DoesUserLoginExist(userName.getText().toString(),password.getText().toString());
            if(userId > 0){
                performLogIn(true, userId);
            }
            else
                message.setText("Failed Login");

        }
        message.invalidate();
    }

    //******************Create****************************
    public void onCreateClick(View aView)
    {
        Log.e(LOG, "Entering: onCreateClick");
        validateUserCreate();
    }

    private void validateUserCreate()
    {
        Log.e(LOG, "Entering: validateUserCreate");

        //TODO better verification

        Response.Listener<String> ResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(LOG, response);
                if (response.equals("Error"))// no match for user exists
                {
                    performCreate(false, null);
                } else {
                    UserSchema user = new UserSchema(userName.getText().toString(),password.getText().toString());
                    try {
                        user.setUserId(Long.parseLong(response));
                        performCreate(true,user);
                    }catch (Exception e) {
                        performCreate(false, null);
                    }
                }
            }
        };

        CreateUserRequest createUserRequest = new CreateUserRequest(userName.getText().toString(),password.getText().toString(),ResponseListener,null);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(createUserRequest);
    }

    private boolean CreateLocalUser(UserSchema aUserSchema)
    {
        Log.e(LOG, "Entering: CreateLocalUser");

        //create user schema from XML field data and create user in DB
        if(dbManager.CreateAUser(aUserSchema)) {
            aUserSchema = dbManager.getUser(aUserSchema.getUserId());
            appSettingsHelper.CreateAppSettings(aUserSchema.getUserId());
            return true;
         }
        return false;
    }

    private void performCreate(boolean isSuccess,UserSchema aUserSchema)
    {
        Log.e(LOG, "Entering: performCreate");

        if (isSuccess)
        {
            if(CreateLocalUser(aUserSchema))
                message.setText("Successfully Created");
            else
                message.setText("Local User Create Failed");
        }
        else {
            message.setText("User Name Already Exists");
        }
        message.invalidate();
    }

    //****************No User Login********************
    public void onNoUserLogin(View aView)
    {
        Log.e(LOG, "Entering: onNoUserLogin");
        //TODO: Need to create dummy user acount for no login
        //Create and intent which will open next activity UserProfile
       // Intent intent = new Intent(this, UserProfile.class);
        //set active user
        //dbManager.SyncUser();
        //currentUser.getUser().setUserId(-1);
        //load all app setting for user
        //appSettingsHelper.LoadMap();
        //start next activity
        //startActivity(intent);
        //message.setText("");
    }

}
