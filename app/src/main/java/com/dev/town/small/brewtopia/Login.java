package com.dev.town.small.brewtopia;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dev.town.small.brewtopia.AppSettings.AppSettingsHelper;
import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.DataClass.*;
import com.dev.town.small.brewtopia.WebAPI.CreateUserRequest;
import com.dev.town.small.brewtopia.WebAPI.JSONUserParser;
import com.dev.town.small.brewtopia.WebAPI.LoginRequest;
import com.dev.town.small.brewtopia.WebAPI.WebController;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;


public class Login extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "Login";
    private static final String VERSION = "v0.1.0.5";

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
                    performLogIn(false,null);
                } else {
                    try{
                        //Parse response into BrewShcema list
                        JSONArray jsonArray = new JSONArray(response);
                        JSONUserParser jsonParser = new JSONUserParser();
                        UserSchema userSchema = jsonParser.ParseUser(jsonArray);

                        //set local pass and update if needed
                        userSchema.setPassword(password.getText().toString());
                        dbManager.updateUser(userSchema);
                        performLogIn(true, userSchema);
                    }
                    catch (JSONException e) {
                        performLogIn(false,null);
                    }
                }
            }
        };

        //If error we will try to login on local DB
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG, error.toString());
                performLogIn(false, null); // try to login from local DB
            }
        };

        LoginRequest loginRequest = new LoginRequest(userName.getText().toString(),password.getText().toString(),ResponseListener,errorListener);
        WebController.getInstance().addToRequestQueue(loginRequest);
    }

    private void performLogIn(boolean isSuccess, UserSchema aUserSchema)
    {
        Log.e(LOG, "Entering: perfromLogin");

        if (isSuccess)
        {
            //Create and intent which will open next activity UserProfile
            Intent intent = new Intent(this, UserProfile.class);
            //set active user
            currentUser.setUser(aUserSchema);
            //load all app setting for user
            appSettingsHelper.LoadMap();
            //start next activity
            startActivity(intent);
            message.setText("");
            currentUser.getUser().writeString();
        }
        else
        {
            // if we have failed login from server try and login from local DB
            long userId =  dbManager.DoesUserLoginExist(userName.getText().toString(),password.getText().toString());
            if(userId > 0){
                performLogIn(true, dbManager.getUser(userId));
                Toast.makeText(this, "Local Login", Toast.LENGTH_SHORT).show();
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

        if(!HasInternet()) {
            message.setText("Internet Connection Needed");
            return;
        }

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
                    try{
                        //Parse response into BrewShcema list
                        JSONArray jsonArray = new JSONArray(response);
                        JSONUserParser jsonParser = new JSONUserParser();
                        UserSchema userSchema = jsonParser.ParseUser(jsonArray);

                        //set local pass and update if needed
                        userSchema.setPassword(password.getText().toString());
                        performCreate(true, userSchema);
                    }
                    catch (JSONException e) {
                        performCreate(false, null);
                    }
                }
            }
        };

        CreateUserRequest createUserRequest = new CreateUserRequest(userName.getText().toString(),password.getText().toString(),ResponseListener,null);
        WebController.getInstance().addToRequestQueue(createUserRequest);
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
            message.setText("New User Create Failed");
        }
        message.invalidate();
    }

    //****************No User Login********************
    public void onNoUserLogin(View aView)
    {
        Log.e(LOG, "Entering: onNoUserLogin");

        //Create and intent which will open next activity UserProfile
        Intent intent = new Intent(this, UserProfile.class);
        //set Temp user
        UserSchema userSchema = new UserSchema();
        userSchema.setUserId(-2);
        userSchema.setTemp(true);
        currentUser.setUser(userSchema);
        //load all app setting for user
        //appSettingsHelper.LoadMap();
        //start next activity
        startActivity(intent);
        message.setText("");
    }

    //****************Version Click********************
    public void onVersionClick(View aView)
    {
        Log.e(LOG, "Entering: onVersionClick");

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://smalltowndev.com/index.php/brewtopia/releaseNotes"));
        startActivity(browserIntent);
    }

    private boolean HasInternet()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
