package com.dev.town.small.brewtopia.Timer;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.dev.town.small.brewtopia.Brews.CustomBoilAdditionsAdapter;
import com.dev.town.small.brewtopia.DataClass.BoilAdditionsSchema;
import com.dev.town.small.brewtopia.R;

import java.util.ArrayList;
import java.util.List;

public class TimerBoilAdditions extends Fragment {

    // Log cat tag
    private static final String LOG = "TimerBoilAdditions";

    private TimerData timerData;
    private Button addButton;
    private ListView BrewAdditionsListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_timer_boil_additions,container,false);
        Log.e(LOG, "Entering: onCreate");

        addButton = (Button)view.findViewById(R.id.AddNewButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddClick(view);
            }
        });

        BrewAdditionsListView = (ListView)view.findViewById(R.id.brewAdditonsListView);

        timerData = TimerData.getInstance();

        loadAll();

        return view;
    }

    private void loadAll()
    {
        Log.e(LOG, "Entering: loadAll");

        List<BoilAdditionsSchema> boilAdditionsSchemaList = new ArrayList<BoilAdditionsSchema>();

        boilAdditionsSchemaList.addAll(timerData.getInstance().getAdditionsList());

        //instantiate custom adapter
        CustomBoilAdditionsAdapter adapter = new CustomBoilAdditionsAdapter(boilAdditionsSchemaList, getActivity());
        adapter.setEditable(false);

        BrewAdditionsListView.setAdapter(adapter);
    }


    public void onAddClick(View aView) {
        Log.e(LOG, "Entering: onAddClick");
    }


}
