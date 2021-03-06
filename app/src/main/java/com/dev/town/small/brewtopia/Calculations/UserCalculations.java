package com.dev.town.small.brewtopia.Calculations;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dev.town.small.brewtopia.CustomListAdapter;
import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.R;

import java.util.*;
import com.dev.town.small.brewtopia.DataClass.*;



public class UserCalculations extends Fragment {

    // Log cat tag
    private static final String LOG = "UserCalculations";

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

        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onCreate");
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
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onResume");
        LoadCalculations();
    }

    private void LoadCalculations() {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: LoadCalculations");

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
        else if(selectedRow.get("text1").equals("BRIX->SG")){
            intent = new Intent(getActivity(), BrixCalculations.class);
            startActivity(intent);
        }
        else if(selectedRow.get("text1").equals("SG->BRIX")){
            intent = new Intent(getActivity(), SpecificGravity.class);
            startActivity(intent);
        }
        else if(selectedRow.get("text1").equals("LME->DME")){
            intent = new Intent(getActivity(), LMEConversion.class);
            startActivity(intent);
        }
        else if(selectedRow.get("text1").equals("DME->LME")){
            intent = new Intent(getActivity(), DMEConversion.class);
            startActivity(intent);
        }
    }
}
