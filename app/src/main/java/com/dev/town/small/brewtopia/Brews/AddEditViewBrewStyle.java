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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.DataClass.BrewStyleSchema;
import com.dev.town.small.brewtopia.R;
import com.dev.town.small.brewtopia.Utilites.RangeBar.RangeSeekBar;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class AddEditViewBrewStyle extends Fragment {

    // Log cat tag
    private static final String LOG = "AddEditViewBrewStyle";

    private Spinner styleTypeSpinner;
    private ArrayAdapter<String> styleTypeAdapter;
    private Spinner styleSpinner;
    private ArrayAdapter<BrewStyleSchema> styleAdapter;

    private DataBaseManager dbManager;

    private RangeSeekBar<Double> rangeSeekBarOG;
    private RangeSeekBar<Double> rangeSeekBarFG;
    private RangeSeekBar<Double> rangeSeekBarABV;
    private RangeSeekBar<Integer> rangeSeekBarIBU;
    private RangeSeekBar<Integer> rangeSeekBarSRM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_add_edit_view_style,container,false);
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onCreate");

        styleTypeSpinner = (Spinner) view.findViewById(R.id.StyleTypeSpinner);
        styleSpinner = (Spinner) view.findViewById(R.id.StyleSpinner);

        dbManager = DataBaseManager.getInstance(getActivity());

        // *************** OG *********************
        rangeSeekBarOG = new RangeSeekBar<>(getActivity());
        // Set the range
        rangeSeekBarOG.setRangeValues(0.9,1.3, 0.01);
        //rangeSeekBarOG.setCurrentValue(1.2);

        LinearLayout OGlayout = (LinearLayout) view.findViewById(R.id.OGSeekBar);
        OGlayout.addView(rangeSeekBarOG);

        // *************** FG *********************
        rangeSeekBarFG = new RangeSeekBar<>(getActivity());
        // Set the range
        rangeSeekBarFG.setRangeValues(0.9,1.3,0.01);
        //rangeSeekBarFG.setCurrentValue(0.9);

        LinearLayout FGlayout = (LinearLayout) view.findViewById(R.id.FGSeekBar);
        FGlayout.addView(rangeSeekBarFG);
        // Add to layout

        // *************** ABV *********************
        rangeSeekBarABV = new RangeSeekBar<>(getActivity());
        // Set the range
        rangeSeekBarABV.setRangeValues(0.0,20.0,0.01);
        //rangeSeekBarABV.setCurrentValue(0.0);

        LinearLayout ABVlayout = (LinearLayout) view.findViewById(R.id.ABVSeekBar);
        ABVlayout.addView(rangeSeekBarABV);

        // *************** IBU *********************
        rangeSeekBarIBU = new RangeSeekBar<>(getActivity());
        // Set the range
        rangeSeekBarIBU.setRangeValues(0, 140);
       // rangeSeekBarIBU.setCurrentValue(0);

        LinearLayout IBUlayout = (LinearLayout) view.findViewById(R.id.IBUSeekBar);
        IBUlayout.addView(rangeSeekBarIBU);


        // *************** SRM *********************
        rangeSeekBarSRM = new RangeSeekBar<>(getActivity());
        // Set the range
        rangeSeekBarSRM.setRangeValues(0, 50);
        //rangeSeekBarSRM.setCurrentValue(0);

        LinearLayout SRMlayout = (LinearLayout) view.findViewById(R.id.SRMSeekBar);
        SRMlayout.addView(rangeSeekBarSRM);


        setStyleTypeSpinner();
        setStyleSpinner();

        updateRanges(null);

        return view;
    }

    private void updateRanges(BrewStyleSchema aBrewStyleSchema) {

        if(aBrewStyleSchema == null) return;

        rangeSeekBarOG.setSelectedMinValue(aBrewStyleSchema.getMinOG());
        rangeSeekBarOG.setSelectedMaxValue(aBrewStyleSchema.getMaxOG());

        rangeSeekBarFG.setSelectedMinValue(aBrewStyleSchema.getMinFG());
        rangeSeekBarFG.setSelectedMaxValue(aBrewStyleSchema.getMaxFG());

        rangeSeekBarABV.setSelectedMinValue(aBrewStyleSchema.getMinABV());
        rangeSeekBarABV.setSelectedMaxValue(aBrewStyleSchema.getMaxABV());

        rangeSeekBarIBU.setSelectedMinValue((int)aBrewStyleSchema.getMinIBU());
        rangeSeekBarIBU.setSelectedMaxValue((int)aBrewStyleSchema.getMaxIBU());

        rangeSeekBarSRM.setSelectedMinValue((int)aBrewStyleSchema.getMinSRM());
        rangeSeekBarSRM.setSelectedMaxValue((int)aBrewStyleSchema.getMaxSRM());

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

        styleTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setStyleSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        try {
            styleTypeSpinner.setSelection(styleTypeAdapter.getPosition(BrewActivityData.getInstance().getAddEditViewBrew().getStyleType()));
        }
        catch (Exception e){
            styleTypeSpinner.setSelection(0);
        }

    }

    private void setStyleSpinner()
    {
        try{
            List<BrewStyleSchema> brewStyleSchemas = dbManager.getAllBrewsStylesByType(styleTypeSpinner.getSelectedItem().toString());

            styleAdapter = new ArrayAdapter<BrewStyleSchema>(getActivity(), android.R.layout.simple_spinner_item, brewStyleSchemas); //selected item will look like a spinner set from XML
            // Specify the layout to use when the list of choices appears
            styleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            styleSpinner.setAdapter(styleAdapter);

            styleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    updateRanges((BrewStyleSchema) adapterView.getItemAtPosition(i));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        catch (Exception e)
        {

        }

    }
}
