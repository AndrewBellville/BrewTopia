package com.dev.town.small.brewtopia;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dev.town.small.brewtopia.AppSettings.AppSettingsHelper;
import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.DataClass.*;
import com.dev.town.small.brewtopia.WebAPI.CreateUserRequest;
import com.dev.town.small.brewtopia.WebAPI.GetBrewRequest;
import com.dev.town.small.brewtopia.WebAPI.GetStylesRequest;
import com.dev.town.small.brewtopia.WebAPI.GetUserBrewsRequest;
import com.dev.town.small.brewtopia.WebAPI.JSONBrewParser;
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
    private static final String VERSION = "v0.1.1.1";

    private EditText userName;
    private EditText password;
    private TextView message;
    private TextView version;
    private TextView noLogin;
    private DataBaseManager dbManager;
    private CurrentUser currentUser;
    private AppSettingsHelper  appSettingsHelper;
    private AlertDialog loadingDialog;
    private TextView loadingText;
    private ProgressBar loadingProgressBar;
    private boolean localLogin=false;

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
        message = (TextView)findViewById(R.id.MessageText);
        version = (TextView)findViewById(R.id.verionNumber);
        noLogin = (TextView)findViewById(R.id.textViewNoLogin);
        version.setText(VERSION);
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
                if (response.equals("Error"))// no match for user password
                {
                    performLogIn(false,null);
                } else {
                    try{
                        //Parse response into user list
                        JSONArray jsonArray = new JSONArray(response);
                        JSONUserParser jsonParser = new JSONUserParser();
                        UserSchema userSchema = jsonParser.ParseUser(jsonArray);

                        //set local pass and update if needed
                        userSchema.setPassword(password.getText().toString());
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
                if(APPUTILS.isLogging)Log.e(LOG, error.toString());
                performLogIn(false, null); // try to login from local DB
            }
        };

        updateLoading("User Data",25);
        LoginRequest loginRequest = new LoginRequest(userName.getText().toString(),password.getText().toString(),ResponseListener,errorListener);
        WebController.getInstance().addToRequestQueue(loginRequest);
    }

    private void performLogIn(boolean isSuccess, UserSchema aUserSchema)
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: performLogin");

        if (isSuccess)
        {
            // if  the user exists globally but not local flag it
            boolean newDevice = false;

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
            //clear any message
            message.setText("");

            if(!newDevice)
                OpenApp();
            else
                getGlobalUserInfo(aUserSchema.getUserId());
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
                message.setText("Failed Login");

        }
        message.invalidate();
    }

    private void getGlobalUserInfo(Long aUserId)
    {
            Response.Listener<String> ResponseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(APPUTILS.isLogging)Log.e(LOG, response);
                    if (response.equals("Error"))// no match for user exists
                    {
                        //Toast.makeText(getActivity(), brewSchema.getBrewName() +" Upload Failed", Toast.LENGTH_SHORT).show();
                    } else {
                        //Pase response
                        try {

                            //Parse response into BrewShcema list
                            JSONArray jsonArray = new JSONArray(response);
                            JSONBrewParser jsonParser = new JSONBrewParser(getApplication());
                            List<BrewSchema> brewSchemaList = jsonParser.ParseGlobalBrews(jsonArray);


                            for (BrewSchema bs : brewSchemaList) {
                                dbManager.CreateAnExistingBrew(bs);
                            }
                            OpenApp();
                        }
                        catch (JSONException e) {}
                    }
                }
            };

        updateLoading("Brew Data",50);
        GetUserBrewsRequest getBrewRequest = new GetUserBrewsRequest(Long.toString(aUserId), ResponseListener,null);
        WebController.getInstance().addToRequestQueue(getBrewRequest);
    }

    //******************Create****************************
    public void onCreateClick(View aView)
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onCreateClick");

        if(!APPUTILS.HasInternet(this)) {
            message.setText("Internet Connection Needed");
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
                if (response.equals("Error"))// no match for user exists
                {
                    performCreate(false, null);
                } else {
                    try{
                        //Parse response into user list
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

        UserSchema userSchema = new UserSchema(userName.getText().toString(),password.getText().toString());
        userSchema.setAppSettingsSchemas(appSettingsHelper.CreateAppSettings());

        updateLoading("Creating User", 0);
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

    private void performCreate(boolean isSuccess,UserSchema aUserSchema)
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: performCreate");

        if (isSuccess)
        {
            if(CreateLocalUser(aUserSchema)) {
                Toast.makeText(this, "User "+ aUserSchema.getUserName() +" Successfully Created", Toast.LENGTH_SHORT).show();
                performLogIn(true,aUserSchema);
            }
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
        message.setText("");
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

    private void OpenApp()
    {
        //Load update / load data needed
        //If we are logining in with local aka no internet dont try and load data
        if(!localLogin)
        {
            getStyles();
        }

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

    private void updateLoading(String displayText, int loadingProgress)
    {
        if(loadingText != null && loadingProgressBar != null)
        {
            loadingText.setText("Loading "+displayText);
            loadingProgressBar.setProgress(loadingProgress);
        }
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

                    } else {
                        //Parse response into style list
                        JSONStyleParser jsonParser = new JSONStyleParser();
                        List<BrewStyleSchema> brewStyleSchemas = jsonParser.ParseStyles(new JSONArray(JSONResponse.getString("message")));
                        //add or update the styles
                        dbManager.updateAllBrewStyles(brewStyleSchemas);
                    }
                }
                catch (JSONException e) {

                }
            }
        };

        updateLoading("Style Data",75);
        GetStylesRequest getStylesRequest = new GetStylesRequest(ResponseListener,null);
        WebController.getInstance().addToRequestQueue(getStylesRequest);
    }
}
