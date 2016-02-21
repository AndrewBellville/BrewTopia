package com.town.small.brewtopia;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

import com.town.small.brewtopia.Calculations.AlcoholByVolume;
import com.town.small.brewtopia.R;

import java.lang.reflect.Array;
import java.util.*;
import com.town.small.brewtopia.DataClass.*;



public class UserCalculations extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "UserCalculations";

    //intent message
    public final static String EXTRA_MESSAGE = "com.town.small.brewtopia.Brews";

    private List<CalculationsSchema> Calculations = new ArrayList<CalculationsSchema>();

    private String userName;
    private ListView CalculationsListView;
    private DataBaseManager dbManager;
    private CurrentUser currentUser;
    ArrayList<HashMap<String, String>> list;
    private boolean isDelete = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_calculations);
        Log.e(LOG, "Entering: onCreate");
        currentUser = CurrentUser.getInstance();
        userName = currentUser.getUser().getUserName();
        CalculationsListView = (ListView)findViewById(R.id.CalculationsListView);
        CalculationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                CalculationSelect(position);
            }
        });
        dbManager = DataBaseManager.getInstance(getApplicationContext());

        GenerateCalculations(); //TODO: remove once DB is set up
        LoadCalculations();
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e(LOG, "Entering: onResume");
        GenerateCalculations();//TODO: remove once DB is set up
        LoadCalculations();
    }

    private void GenerateCalculations()
    {
        Log.e(LOG, "Entering: GenerateCalculations");
        if(Calculations.size() == 0)
        {
            CalculationsSchema temp = new CalculationsSchema();
            temp.setCalculationAbv("ABV");
            temp.setCalculationName("Alcohol by volume");

            Calculations.add(temp);
        }
    }

    private void LoadCalculations() {
        Log.e(LOG, "Entering: LoadCalculations");

        //List<BrewSchema> brewList = dbManager.getAllBrews();
        list = new ArrayList<HashMap<String, String>>();

        if(Calculations.size() == 0 && list.size() > 0) {
            list.clear();
        }
        else if (Calculations.size() > 0) {

            for(CalculationsSchema calculation : Calculations)
            {
                HashMap<String,String> temp = new HashMap<String,String>();
                temp.put("text1",calculation.getCalculationAbv());
                temp.put("text2",calculation.getCalculationName());
                list.add(temp);
            }

            //instantiate custom adapter
            CustomListAdapter adapter = new CustomListAdapter(list, this.getApplicationContext());
            CalculationsListView.setAdapter(adapter);
        }
    }

    private void CalculationSelect(int aPos)
    {
        //Display selected brew
        HashMap<String,String> selectedRow = list.get(aPos);

        Intent intent;
        if(selectedRow.get("text1").equals("ABV")) {
            intent = new Intent(this, AlcoholByVolume.class);
            startActivity(intent);
        }
    }
}
