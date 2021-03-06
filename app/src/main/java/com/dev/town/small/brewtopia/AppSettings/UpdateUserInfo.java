package com.dev.town.small.brewtopia.AppSettings;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.DataClass.CurrentUser;
import com.dev.town.small.brewtopia.DataClass.UserSchema;
import com.dev.town.small.brewtopia.R;
import com.dev.town.small.brewtopia.WebAPI.JSONUserParser;
import com.dev.town.small.brewtopia.WebAPI.UpdateUserRequest;
import com.dev.town.small.brewtopia.WebAPI.WebController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateUserInfo extends ActionBarActivity {

    private static final String LOG = "UpdateUserInfo";

    private Toolbar toolbar;
    private EditText currentPassword;
    private EditText NewUserName;
    private EditText NewPassword;
    private DataBaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Update User Information");

        dbManager = DataBaseManager.getInstance(this);

        Button submitButton = (Button) findViewById(R.id.SubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubmitClicked();
            }
        });

        currentPassword = (EditText) findViewById(R.id.CPasswordEditText);
        NewUserName = (EditText) findViewById(R.id.NUserNameEditText);
        NewUserName.setText(CurrentUser.getInstance().getUser().getUserName());
        NewPassword = (EditText) findViewById(R.id.NPasswordEditText);
    }

    private void SubmitClicked()
    {
        if(APPUTILS.isLogging) Log.e(LOG, "Entering: validateUserLogin");

        if(!APPUTILS.HasInternet(this)) {
            Toast.makeText(this, "Need Internet To Perform", Toast.LENGTH_SHORT).show();
            return;
        }

        //verify user record exists
        Response.Listener<String> ResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(APPUTILS.isLogging) Log.e(LOG, response);
                try{
                    JSONArray jsonArray = new JSONArray("["+response+"]");
                    JSONObject JSONResponse = (JSONObject) jsonArray.get(0);
                    if (JSONResponse.getString("status").equals("Error"))// no match for user exists
                    {
                        DisplayError(JSONResponse.getString("message"));
                    }else{

                        //Parse response into BrewShcema list
                        JSONUserParser jsonParser = new JSONUserParser();
                        UserSchema userSchema = jsonParser.ParseUser(new JSONArray(JSONResponse.getString("message")));

                        userSchema.setPassword(NewPassword.getText().toString());

                        updateUser(userSchema);
                        CurrentUser.getInstance().setUser(userSchema);
                    }
                }
                catch (JSONException e) {
                    DisplayError("User was not updated");
                }
            }
        };

        //If error we will try to login on local DB
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(APPUTILS.isLogging) Log.e(LOG, error.toString());
            }
        };

        UpdateUserRequest updateRequest = new UpdateUserRequest(CurrentUser.getInstance().getUser(), currentPassword.getText().toString(),
                                                                NewUserName.getText().toString(),NewPassword.getText().toString(),ResponseListener,errorListener);
        WebController.getInstance().addToRequestQueue(updateRequest);
    }

    private void updateUser(UserSchema aUserSchema)
    {

        if(dbManager.DoesUserExist(aUserSchema.getUserId())) {
            dbManager.updateUser(aUserSchema);
            Toast.makeText(this, "User Successfully Updated", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "User was not updated", Toast.LENGTH_SHORT).show();
        }
    }

    private void DisplayError(String errorText)
    {
        Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
