package com.dev.town.small.brewtopia.Brews;

/**
 * Created by Andrew on 10/17/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.DataClass.BrewStyleSchema;
import com.dev.town.small.brewtopia.R;
import com.dev.town.small.brewtopia.Utilites.RangeBar.RangeSeekBar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class AddEditViewBrewStyle extends Fragment {

    // Log cat tag
    private static final String LOG = "AddEditViewBrewStyle";

    private Spinner styleTypeSpinner;
    ArrayAdapter<String> styleTypeAdapter;
    private Spinner styleSpinner;
    ArrayAdapter<String> styleAdapter;

    private DataBaseManager dbManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_add_edit_view_style,container,false);
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onCreate");

        styleTypeSpinner = (Spinner) view.findViewById(R.id.StyleTypeSpinner);
        styleSpinner = (Spinner) view.findViewById(R.id.StyleSpinner);

        dbManager = DataBaseManager.getInstance(getActivity());

        // *************** OG *********************
        RangeSeekBar<Integer> rangeSeekBar = new RangeSeekBar<>(getActivity());
        // Set the range
        rangeSeekBar.setRangeValues(15, 90);
        rangeSeekBar.setSelectedMinValue(50);
        rangeSeekBar.setSelectedMaxValue(88);
        rangeSeekBar.setCurrentValue(20);

        LinearLayout OGlayout = (LinearLayout) view.findViewById(R.id.OGSeekBar);
        OGlayout.addView(rangeSeekBar);

        // *************** FG *********************
        RangeSeekBar<Integer> rangeSeekBar1 = new RangeSeekBar<>(getActivity());
        // Set the range
        rangeSeekBar1.setRangeValues(15, 90);
        rangeSeekBar1.setSelectedMinValue(20);
        rangeSeekBar1.setSelectedMaxValue(88);
        rangeSeekBar1.setCurrentValue(50);

        LinearLayout FGlayout = (LinearLayout) view.findViewById(R.id.FGSeekBar);
        FGlayout.addView(rangeSeekBar1);
        // Add to layout

        // *************** ABV *********************
        RangeSeekBar<Integer> rangeSeekBar2 = new RangeSeekBar<>(getActivity());
        // Set the range
        rangeSeekBar2.setRangeValues(15, 90);
        rangeSeekBar2.setSelectedMinValue(20);
        rangeSeekBar2.setSelectedMaxValue(88);
        rangeSeekBar2.setCurrentValue(50);

        LinearLayout ABVlayout = (LinearLayout) view.findViewById(R.id.ABVSeekBar);
        ABVlayout.addView(rangeSeekBar2);

        // *************** IBU *********************
        RangeSeekBar<Integer> rangeSeekBar3 = new RangeSeekBar<>(getActivity());
        // Set the range
        rangeSeekBar3.setRangeValues(15, 90);
        rangeSeekBar3.setSelectedMinValue(20);
        rangeSeekBar3.setSelectedMaxValue(88);
        rangeSeekBar3.setCurrentValue(50);

        LinearLayout IBUlayout = (LinearLayout) view.findViewById(R.id.IBUSeekBar);
        IBUlayout.addView(rangeSeekBar3);


        // *************** SRM *********************
        RangeSeekBar<Integer> rangeSeekBar4 = new RangeSeekBar<>(getActivity());
        // Set the range
        rangeSeekBar4.setRangeValues(15, 90);
        rangeSeekBar4.setSelectedMinValue(20);
        rangeSeekBar4.setSelectedMaxValue(88);
        rangeSeekBar4.setCurrentValue(50);

        LinearLayout SRMlayout = (LinearLayout) view.findViewById(R.id.SRMSeekBar);
        SRMlayout.addView(rangeSeekBar4);


        setStyleTypeSpinner();
        setStyleSpinner();

        return view;
    }

    private void setStyleTypeSpinner()
    {
        String[] brewStyles = new String[ APPUTILS.StyleMap.size()];

        Iterator it = APPUTILS.StyleMap.entrySet().iterator();
        int i=0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            brewStyles[i++]= pair.getKey().toString();
        }

        styleTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, brewStyles); //selected item will look like a spinner set from XML
        // Specify the layout to use when the list of choices appears
        styleTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        styleTypeSpinner.setAdapter(styleTypeAdapter);
    }

    private void setStyleSpinner()
    {
        List<BrewStyleSchema> brewStyleSchemas = dbManager.getAllBrewsStyles();

        String[] brewStyles = new String[ brewStyleSchemas.size()];

        for(int i=0; i < brewStyleSchemas.size();i++) {
            brewStyles[i]= brewStyleSchemas.get(i).getBrewStyleName();
        }
        styleAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, brewStyles); //selected item will look like a spinner set from XML
        // Specify the layout to use when the list of choices appears
        styleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        styleSpinner.setAdapter(styleAdapter);
    }
}
