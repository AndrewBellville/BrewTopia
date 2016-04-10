package com.town.small.brewtopia.Calculations;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

import com.town.small.brewtopia.Calculations.AlcoholByVolume;
import com.town.small.brewtopia.Calculations.BrixCalculations;
import com.town.small.brewtopia.Calculations.SpecificGravity;
import com.town.small.brewtopia.CustomListAdapter;
import com.town.small.brewtopia.R;

import java.lang.reflect.Array;
import java.util.*;
import com.town.small.brewtopia.DataClass.*;



public class UserCalculations extends Fragment {

    // Log cat tag
    private static final String LOG = "UserCalculations";

    //intent message
    public final static String EXTRA_MESSAGE = "com.town.small.brewtopia.Brews";

    private List<CalculationsSchema> Calculations;

    private String userName;
    private ListView CalculationsListView;
    private DataBaseManager dbManager;
    private CurrentUser currentUser;
    ArrayList<HashMap<String, String>> list;
    private boolean isDelete = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_user_calculations,container,false);

        Log.e(LOG, "Entering: onCreate");
        currentUser = CurrentUser.getInstance();
        userName = currentUser.getUser().getUserName();
        CalculationsListView = (ListView)view.findViewById(R.id.CalculationsListView);
        CalculationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                CalculationSelect(position);
            }
        });
        dbManager = DataBaseManager.getInstance(getActivity());

        Calculations = dbManager.getAllCalculations();
        LoadCalculations();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e(LOG, "Entering: onResume");
        LoadCalculations();
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
            CustomListAdapter adapter = new CustomListAdapter(list, getActivity());
            CalculationsListView.setAdapter(adapter);
        }
    }

    private void CalculationSelect(int aPos)
    {
        //Display selected brew
        HashMap<String,String> selectedRow = list.get(aPos);

        Intent intent;
        if(selectedRow.get("text1").equals("ABV")) {
            intent = new Intent(getActivity(), AlcoholByVolume.class);
            startActivity(intent);
        }
        else if(selectedRow.get("text1").equals("BRIX")){
            intent = new Intent(getActivity(), BrixCalculations.class);
            startActivity(intent);
        }
        else if(selectedRow.get("text1").equals("SG")){
            intent = new Intent(getActivity(), SpecificGravity.class);
            startActivity(intent);
        }
    }
}
