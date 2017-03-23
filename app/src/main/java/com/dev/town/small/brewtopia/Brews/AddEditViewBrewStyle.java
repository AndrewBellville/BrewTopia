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
import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.R;


public class AddEditViewBrewStyle extends Fragment {

    // Log cat tag
    private static final String LOG = "AddEditViewBrewStyle";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_add_edit_view_style,container,false);
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onCreate");


        return view;
    }
}
