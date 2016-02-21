package com.town.small.brewtopia.Brews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

import com.town.small.brewtopia.CustomListAdapter;
import com.town.small.brewtopia.R;

import java.util.*;
import com.town.small.brewtopia.DataClass.*;



public class UserBrews extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "UserBrews";

    //intent message
    public final static String EXTRA_MESSAGE = "com.town.small.brewtopia.Brews";

    private String userName;
    private ListView BrewListView;
    private DataBaseManager dbManager;
    private CurrentUser currentUser;
    ArrayList<HashMap<String, String>> list;
    private boolean isDelete = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_brews);
        Log.e(LOG, "Entering: onCreate");
        currentUser = CurrentUser.getInstance();
        userName = currentUser.getUser().getUserName();
        BrewListView = (ListView)findViewById(R.id.BrewList);
        BrewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                HashMap<String,String> selectedRow = list.get(position);
                BrewSelect(selectedRow.get("text1"));
            }
        });
        dbManager = DataBaseManager.getInstance(getApplicationContext());

        LoadBrews();
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e(LOG, "Entering: onResume");
        LoadBrews();
    }

    private void LoadBrews() {
        Log.e(LOG, "Entering: LoadBrews");

        List<BrewSchema> brewList = dbManager.getAllBrews(userName);
        list = new ArrayList<HashMap<String, String>>();

        if(brewList.size() == 0 && list.size() > 0) {
            list.clear();
        }
        else if (brewList.size() > 0) {

            for(BrewSchema brew : brewList)
            {
                HashMap<String,String> temp = new HashMap<String,String>();
                temp.put("text1",brew.getBrewName());
                temp.put("text2", "Create: " + brew.getCreatedOn());
                list.add(temp);
            }

            //instantiate custom adapter
            CustomListAdapter adapter = new CustomListAdapter(list, this.getApplicationContext());
            BrewListView.setAdapter(adapter);
        }
    }

    private void BrewSelect(String aBrewName)
    {
        if(isDelete)
        {
            dbManager.DeleteBrew(aBrewName, userName);
            LoadBrews();
        }
        else{
            Intent intent = new Intent(this, AddEditViewBrew.class);

            // Set the state of display if View brew cannot be null
            BrewActivityData.getInstance().setViewStateAndBrew(BrewActivityData.DisplayMode.EDIT,dbManager.getBrew(aBrewName,userName));

            //start next activity
            startActivity(intent);
        }

    }

    public void onCreateClick(View aView)
    {
        Log.e(LOG, "Entering: onCreateClick");

        Intent intent = new Intent(this, AddEditViewBrew.class);

        // Set the state of display if Add brew can be null
        BrewActivityData.getInstance().setViewStateAndBrew(BrewActivityData.DisplayMode.ADD,null);

        //start next activity
        startActivity(intent);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        if(isDelete) ((RadioButton) view).setChecked(false);
        isDelete = ((RadioButton) view).isChecked();
    }
}
