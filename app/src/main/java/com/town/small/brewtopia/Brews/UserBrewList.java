package com.town.small.brewtopia.Brews;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;

import com.town.small.brewtopia.CustomListAdapter;
import com.town.small.brewtopia.R;

import java.util.*;
import com.town.small.brewtopia.DataClass.*;



public class UserBrewList extends Fragment {

    // Log cat tag
    private static final String LOG = "UserBrews";

    //intent message
    //public final static String EXTRA_MESSAGE = "com.town.small.brewtopia.Brews";

    private String userName;
    private ListView BrewListView;
    private DataBaseManager dbManager;
    private CurrentUser currentUser;
    ArrayList<HashMap<String, String>> list;
    private boolean isDelete = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_user_brew_list,container,false);
        Log.e(LOG, "Entering: onCreate");
        currentUser = CurrentUser.getInstance();
        userName = currentUser.getUser().getUserName();

        BrewListView = (ListView)view.findViewById(R.id.BrewList);
        BrewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                HashMap<String,String> selectedRow = list.get(position);
                BrewSelect(selectedRow.get("text1"));
            }
        });
        dbManager = DataBaseManager.getInstance(getActivity());

        Button createButton = (Button) view.findViewById(R.id.AddBrewButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateClick(view);
            }
        });

        RadioButton deleteButon = (RadioButton) view.findViewById(R.id.DeleteBrewRadioButton);
        deleteButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRadioButtonClicked(view);
            }
        });

        LoadBrews();

        return view;
    }

    @Override
    public void onResume(){
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
                temp.put("text2", "Create: " + brew.getCreatedOn() +GetStyleColor(brew.getStyle()));
                list.add(temp);
            }

            //instantiate custom adapter
            CustomListAdapter adapter = new CustomListAdapter(list, getActivity());
            adapter.setDeleteView(isDelete);
            adapter.hasColor(true);
            adapter.setEventHandler(new CustomListAdapter.EventHandler() {
                @Override
                public void OnDeleteClickListener(String aName, String aDate) {
                    dbManager.DeleteBrew(aName, userName);
                }
            });
            BrewListView.setAdapter(adapter);
        }
    }

    private void BrewSelect(String aBrewName)
    {
            Intent intent = new Intent(getActivity(), UserBrew.class);

            // Set the state of display if View brew cannot be null
            BrewActivityData.getInstance().setViewStateAndBrew(BrewActivityData.DisplayMode.VIEW,dbManager.getBrew(aBrewName,userName));

            //start next activity
            startActivity(intent);
    }

    public void onCreateClick(View aView)
    {
        Log.e(LOG, "Entering: onCreateClick");

        Intent intent = new Intent(getActivity(), UserBrew.class);

        // Set the state of display if Add brew can be null
        BrewActivityData.getInstance().setViewStateAndBrew(BrewActivityData.DisplayMode.ADD,null);

        //start next activity
        startActivity(intent);
    }

    private String GetStyleColor(String styleName)
    {
        BrewStyleSchema brewStyleSchema = dbManager.getBrewsStylesByName(styleName);
        return brewStyleSchema.getBrewStyleColor();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        if(isDelete) ((RadioButton) view).setChecked(false);
        isDelete = ((RadioButton) view).isChecked();

        LoadBrews();
    }
}
