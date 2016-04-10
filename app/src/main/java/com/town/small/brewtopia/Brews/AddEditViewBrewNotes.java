package com.town.small.brewtopia.Brews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.town.small.brewtopia.DataClass.DataBaseManager;
import com.town.small.brewtopia.R;

/**
 * Created by Andrew on 4/9/2016.
 */
public class AddEditViewBrewNotes extends Fragment {

    // Log cat tag
    private static final String LOG = "AddEditViewBrewNotes";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_add_edit_view_brew_notes,container,false);
        Log.e(LOG, "Entering: onCreate");


        return view;
    }
}
