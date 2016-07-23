package com.town.small.brewtopia.Global;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.town.small.brewtopia.R;

public class UserGlobal extends Fragment {

    // Log cat tag
    private static final String LOG = "UserGlobal";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_user_global,container,false);

        Log.e(LOG, "Entering: onCreate");


        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e(LOG, "Entering: onResume");
    }
}
