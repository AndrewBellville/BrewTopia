package com.dev.town.small.brewtopia;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dev.town.small.brewtopia.AppSettings.AppSettingsHelper;
import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.DataClass.*;
import com.dev.town.small.brewtopia.SharedMemory.InventoryMemory;
import com.dev.town.small.brewtopia.WebAPI.CreateUserRequest;
import com.dev.town.small.brewtopia.WebAPI.GetBrewRequest;
import com.dev.town.small.brewtopia.WebAPI.GetInventoryRequest;
import com.dev.town.small.brewtopia.WebAPI.GetStylesRequest;
import com.dev.town.small.brewtopia.WebAPI.GetUserBrewsRequest;
import com.dev.town.small.brewtopia.WebAPI.JSONBrewParser;
import com.dev.town.small.brewtopia.WebAPI.JSONInventoryParser;
import com.dev.town.small.brewtopia.WebAPI.JSONStyleParser;
import com.dev.town.small.brewtopia.WebAPI.JSONUserParser;
import com.dev.town.small.brewtopia.WebAPI.LoginRequest;
import com.dev.town.small.brewtopia.WebAPI.WebController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class Login extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "Login";
    private static final String VERSION = "v0.1.1.2";

    public static final String PREFS_NAME = "BrewTopiaPref";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASSWORD = "password";

    private EditText userName;
    private EditText password;
    private TextView version;
    private TextView noLogin;
    private CheckBox rememberMe;
    private DataBaseManager dbManager;
    private CurrentUser currentUser;
    private AppSettingsHelper  appSettingsHelper;
    private AlertDialog loadingDialog;
    private TextView loadingText;
    private ProgressBar loadingProgressBar;
    private int loadingProgress=0;
    private boolean localLogin=false;
    private boolean newDevice = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onCreate");
        dbManager = DataBaseManager.getInstance(getApplicationContext());
        currentUser = CurrentUser.getInstance();

        appSettingsHelper = AppSettingsHelper.getInstance(this);

        userName = (EditText)findViewById(R.id.UserNameTextBox);
        password = (EditText)findViewById(R.id.PasswordTextBox);
        version = (TextView)findViewById(R.id.verionNumber);
        noLogin = (TextView)findViewById(R.id.textViewNoLogin);
        rememberMe = (CheckBox)findViewById(R.id.saveLogin);
        version.setText(VERSION);

        //try and load saved user login
        SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String savedUsername = pref.getString(PREF_USERNAME, null);
        String savedPassword = pref.getString(PREF_PASSWORD, null);

        if ((savedUsername != null && savedPassword != null) &&
                !savedUsername.isEmpty() && !savedPassword.isEmpty()) {
            userName.setText(savedUsername);
            password.setText(savedPassword);
        }
    }

    //******************Login****************************
    public void onLoginClick(View aView)
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onLoginClick");

        loading();
        validateUserLogin();
    }

    private void validateUserLogin()
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: validateUserLogin");

        //TODO better verification
        //verify user record exists
        Response.Listener<String> ResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(APPUTILS.isLogging)Log.e(LOG, response);
                try{
                    JSONArray jsonArray = new JSONArray("["+response+"]");
                    JSONObject JSONResponse = (JSONObject) jsonArray.get(0);
                    if (JSONResponse.getString("status").equals("Error"))// no match for user exists
                    {
                        performLogIn(false,null);
                    } else {
                        //Parse response into user list
                        JSONUserParser jsonParser = new JSONUserParser();
                        UserSchema userSchema = jsonParser.ParseUser(new JSONArray(JSONResponse.getString("message")));

                        //set local pass and update if needed
                        userSchema.setPassword(password.getText().toString());
                        performLogIn(true, userSchema);
                    }
                }
                catch (JSONException e) {
                    performLogIn(false,null);
                }
            }
        };

        //If error we will try to login on local DB
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(APPUTILS.isLogging)Log.e(LOG, error.toString());
                performLogIn(false, null); // try to login from local DB
            }
        };

        updateLoading("User Data");
        LoginRequest loginRequest = new LoginRequest(userName.getText().toString(),password.getText().toString(),ResponseListener,errorListener);
        WebController.getInstance().addToRequestQueue(loginRequest);
    }

    private void performLogIn(boolean isSuccess, UserSchema aUserSchema)
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: performLogin");

        if (isSuccess)
        {
            //check for if user exists on this device if yes just update
            //else create a new user on the device / create the app setting
            if(dbManager.DoesUserExist(aUserSchema.getUserId())) {
                dbManager.updateUser(aUserSchema);
            }
            else {
                CreateLocalUser(aUserSchema);
                newDevice = true;
            }

            //set active user
            currentUser.setUser(aUserSchema);
            //load all app setting for user
            appSettingsHelper.LoadMap();

            saveUserLogin(aUserSchema);

            preLoad();
        }
        else
        {
            // if we have failed login from server try and login from local DB
            long userId =  dbManager.DoesUserLoginExist(userName.getText().toString(),password.getText().toString());
            if(userId > 0){
                performLogIn(true, dbManager.getUser(userId));
                localLogin = true;
                Toast.makeText(this, "Local Login", Toast.LENGTH_SHORT).show();
            }
            else
                DisplayError("Failed Login");

        }
    }

    //save user login info
    private void saveUserLogin(UserSchema aUserSchema)
    {
        String username = "";
        String password = "";
        if(rememberMe.isChecked())
        {
            username = aUserSchema.getUserName();
            password = aUserSchema.getPassword();
        }
        //if exists apply new changes
        if(getSharedPreferences(PREFS_NAME,MODE_PRIVATE).contains(PREF_USERNAME))
        {
            getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                    .edit()
                    .putString(PREF_USERNAME, username)
                    .putString(PREF_PASSWORD, password)
                    .apply();
        }
        else
        {
            //else create
            getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                    .edit()
                    .putString(PREF_USERNAME, username)
                    .putString(PREF_PASSWORD, password)
                    .commit();
        }


    }

    //******************Create****************************
    public void onCreateClick(View aView)
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onCreateClick");

        if(!APPUTILS.HasInternet(this)) {
            Toast.makeText(this, "Internet Connection Needed", Toast.LENGTH_SHORT).show();
            return;
        }

        showDisclaimer();
    }

    private void validateUserCreate()
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: validateUserCreate");

        //TODO better verification

        Response.Listener<String> ResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(APPUTILS.isLogging)Log.e(LOG, response);
                try{
                    JSONArray jsonArray = new JSONArray("["+response+"]");
                    JSONObject JSONResponse = (JSONObject) jsonArray.get(0);
                    if (JSONResponse.getString("status").equals("Error"))// no match for user exists
                    {
                        DisplayError(JSONResponse.getString("message"));
                    } else {

                        //Parse response into user list
                        JSONUserParser jsonParser = new JSONUserParser();
                        UserSchema userSchema = jsonParser.ParseUser(new JSONArray(JSONResponse.getString("message")));

                        //set local pass and update if needed
                        userSchema.setPassword(password.getText().toString());
                        performCreate(userSchema);
                    }
                }
                catch (JSONException e) {
                    DisplayError("User Create Failed");
                }
            }
        };

        UserSchema userSchema = new UserSchema(userName.getText().toString(),password.getText().toString());
        userSchema.setAppSettingsSchemas(appSettingsHelper.CreateAppSettings());

        updateLoading("Creating User");
        CreateUserRequest createUserRequest = new CreateUserRequest(userSchema,ResponseListener,null);
        WebController.getInstance().addToRequestQueue(createUserRequest);
    }

    private boolean CreateLocalUser(UserSchema aUserSchema)
    {
        if(APPUTILS.isLogging) Log.e(LOG, "Entering: CreateLocalUser");

        //create user schema from XML field data and create user in DB
        if(dbManager.CreateAUser(aUserSchema))
            return true;

        return false;
    }

    private void performCreate(UserSchema aUserSchema)
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: performCreate");

        if(CreateLocalUser(aUserSchema)) {
            Toast.makeText(this, "User "+ aUserSchema.getUserName() +" Successfully Created", Toast.LENGTH_SHORT).show();
            performLogIn(true,aUserSchema);
        }
        else
            Toast.makeText(this, "Local User Create Failed", Toast.LENGTH_SHORT).show();
    }

    //****************No User Login********************
    public void onNoUserLogin(View aView)
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onNoUserLogin");

        //Create and intent which will open next activity UserProfile
        Intent intent = new Intent(this, UserProfile.class);
        //set Temp user
        UserSchema userSchema = new UserSchema();
        userSchema.setUserId(0);
        userSchema.setRole(UserSchema.UserRoles.Unknown.getValue());
        userSchema.setTemp(true);
        currentUser.setUser(userSchema);
        //load all app setting for user
        //appSettingsHelper.LoadMap();
        //start next activity
        startActivity(intent);
    }

    //****************Version Click********************
    public void onVersionClick(View aView)
    {
        if(APPUTILS.isLogging) Log.e(LOG, "Entering: onVersionClick");

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://smalltowndev.com/index.php/brewtopia/releaseNotes"));
        startActivity(browserIntent);
    }

    private void showDisclaimer()
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        loading();
                        validateUserCreate();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("BrewTopia is provided as is and your use is entirely at your own risk. \n\n" +
                " No warrant is made as to performance or data accuracy. \n\n" +
                " Author is not liable for any issues related to this application. \n\n" +
                " Have Fun and  Brew On!")
                .setTitle("Disclaimer")
                .setPositiveButton("I Accept", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener).show();
    }

    private void preLoad()
    {
        //Load update / load data needed
        //If we are logining in with local aka no internet dont try and load data
        if(!localLogin)
        {
            //if new device try and get any brew data
            if(newDevice)
                getNewDeviceUserBrews(CurrentUser.getInstance().getUser().getUserId());

            if(appSettingsHelper.GetBoolAppSettingsByName(AppSettingsHelper.DATA_UPDATE_ON_LOGIN))
                getStyles();
            else
                OpenApp();
        }
        else
        {
            OpenApp();
        }
    }

    private void OpenApp()
    {
        InventoryMemory.getInstance().load(getApplicationContext());

        //Create and intent which will open next activity UserProfile
        Intent intent = new Intent(this, UserProfile.class);

        //start next activity
        startActivity(intent);

        if(loadingDialog != null) loadingDialog.cancel();
    }

    private void loading()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.loading_dialog, null);
        alertDialogBuilder.setView(dialogView);

        loadingText = (TextView) dialogView.findViewById(R.id.LoadingTextView);
        loadingProgressBar =(ProgressBar) dialogView.findViewById(R.id.LoadingProgressBar);

        loadingDialog = alertDialogBuilder.create();

        loadingDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.show();
    }

    private void getNewDeviceUserBrews(Long aUserId)
    {
        Response.Listener<String> ResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(APPUTILS.isLogging)Log.e(LOG, response);
                //Pase response
                try {
                    JSONArray jsonArray = new JSONArray("["+response+"]");
                    JSONObject JSONResponse = (JSONObject) jsonArray.get(0);
                    if (JSONResponse.getString("status").equals("Error"))// no match for user exists
                    {
                        DisplayError(JSONResponse.getString("message"));
                    } else {

                        //Parse response into BrewShcema list
                        JSONBrewParser jsonParser = new JSONBrewParser(getApplication());
                        List<BrewSchema> brewSchemaList = jsonParser.ParseGlobalBrews(new JSONArray(JSONResponse.getString("message")));


                        for (BrewSchema bs : brewSchemaList) {
                            dbManager.CreateAnExistingBrew(bs);
                        }
                        getStyles();
                    }
                }
                catch (JSONException e) {
                    DisplayError("Brew Data Load Failed");
                }
            }
        };

        updateLoading("Brew Data");
        GetUserBrewsRequest getBrewRequest = new GetUserBrewsRequest(Long.toString(aUserId), ResponseListener,null);
        WebController.getInstance().addToRequestQueue(getBrewRequest);
    }

    private void getStyles()
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: getStyles");

        Response.Listener<String> ResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(APPUTILS.isLogging)Log.e(LOG, response);
                try{
                    JSONArray jsonArray = new JSONArray("["+response+"]");
                    JSONObject JSONResponse = (JSONObject) jsonArray.get(0);
                    if (JSONResponse.getString("status").equals("Error"))// no match for user exists
                    {
                        DisplayError(JSONResponse.getString("message"));
                    } else {
                        //Parse response into style list
                        JSONStyleParser jsonParser = new JSONStyleParser();
                        List<BrewStyleSchema> brewStyleSchemas = jsonParser.ParseStyles(new JSONArray(JSONResponse.getString("message")));
                        //add or update the styles
                        dbManager.updateAllBrewStyles(brewStyleSchemas);

                        getInventory();
                    }
                }
                catch (JSONException e) {
                    DisplayError("Style Loading failed");
                }
            }
        };

        updateLoading("Style Data");
        GetStylesRequest getStylesRequest = new GetStylesRequest(ResponseListener,null);
        WebController.getInstance().addToRequestQueue(getStylesRequest);
    }

    private void getInventory()
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: getInventory");

        Response.Listener<String> ResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(APPUTILS.isLogging)Log.e(LOG, response);
                try{
                    JSONArray jsonArray = new JSONArray("["+response+"]");
                    JSONObject JSONResponse = (JSONObject) jsonArray.get(0);
                    if (JSONResponse.getString("status").equals("Error"))// no match for user exists
                    {
                        DisplayError(JSONResponse.getString("message"));
                    } else {
                        //Parse response into style list
                        JSONInventoryParser jsonParser = new JSONInventoryParser();
                        List<InventorySchema> inventorySchemas = jsonParser.ParseInventory(new JSONArray("["+JSONResponse.getString("message")+"]"));
                        //add or update the inventory
                        dbManager.UpdateInventory(inventorySchemas);

                        OpenApp();
                    }
                }
                catch (JSONException e) {
                    DisplayError("Inventory Loading failed");
                }
            }
        };

        updateLoading("Inventory Data");
        GetInventoryRequest getInventoryRequest = new GetInventoryRequest(ResponseListener,null);
        WebController.getInstance().addToRequestQueue(getInventoryRequest);
    }

    private void updateLoading(String displayText)
    {
        if(loadingText != null && loadingProgressBar != null)
        {
            loadingText.setText("Loading "+displayText);
            loadingProgressBar.setProgress(loadingProgress);
            loadingProgress += 20;
        }
    }

    private void DisplayError(String errorText)
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: DisplayError");

        if(loadingDialog != null) loadingDialog.cancel();

        Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show();
    }
}
