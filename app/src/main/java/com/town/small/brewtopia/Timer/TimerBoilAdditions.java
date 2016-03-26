package com.town.small.brewtopia.Timer;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.AdapterView;

import com.town.small.brewtopia.CustomListAdapter;
import com.town.small.brewtopia.DataClass.BoilAdditionsSchema;
import com.town.small.brewtopia.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimerBoilAdditions extends Fragment {

    // Log cat tag
    private static final String LOG = "TimerBoilAdditions";

    private TimerData td;
    private ListView TimerAdditionListView;
    ArrayList<HashMap<String, String>> list;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_timer_boil_additions, container, false);

        td = TimerData.getInstance();
        TimerAdditionListView = (ListView) view.findViewById(R.id.TimerAdditionsListView);
        TimerAdditionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //TimerAdditionSelect(position);
            }
        });

        LoadTimerBoilAdditions();
        return view;
    }

    private void LoadTimerBoilAdditions() {
        Log.e(LOG, "Entering: LoadTimerBoilAdditions");

        //List<BrewSchema> brewList = dbManager.getAllBrews();
        list = new ArrayList<HashMap<String, String>>();
        List<BoilAdditionsSchema> timerAdditionList = td.getAdditionsList();

        if (timerAdditionList.size() > 0) {

            for(BoilAdditionsSchema addition : timerAdditionList)
            {
                HashMap<String,String> temp = new HashMap<String,String>();
                temp.put("text1",addition.getAdditionName());
                temp.put("text2", Integer.toString(addition.getAdditionTime()));
                list.add(temp);
            }

            //instantiate custom adapter
            CustomListAdapter adapter = new CustomListAdapter(list, getActivity());
            TimerAdditionListView.setAdapter(adapter);
        }
    }
}
