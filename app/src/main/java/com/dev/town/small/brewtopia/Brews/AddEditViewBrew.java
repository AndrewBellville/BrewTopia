package com.dev.town.small.brewtopia.Brews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.R;
import com.dev.town.small.brewtopia.DataClass.*;
import com.dev.town.small.brewtopia.Timer.TimerData;
import com.dev.town.small.brewtopia.Timer.TimerPager;
import com.dev.town.small.brewtopia.WebAPI.CreateBrewRequest;
import com.dev.town.small.brewtopia.WebAPI.GetBrewRequest;
import com.dev.town.small.brewtopia.WebAPI.JSONBrewParser;
import com.dev.town.small.brewtopia.WebAPI.WebController;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AddEditViewBrew extends Fragment {

    // Log cat tag
    private static final String LOG = "AddEditViewBrew";

    private ImageView brewImage;
    private Button startButton;
    private Button editBrewButton;
    private Button uploadButton;
    private EditText brewName;
    private EditText primary;
    private EditText secondary;
    private EditText bottle;
    private EditText description;
    private EditText boilTime;
    private EditText targetOG;
    private EditText targetFG;
    private EditText targetABV;
    private Spinner method;
    private EditText IBU;
    private EditText BatchSize;
    private EditText Efficiency;
    private EditText SRM;

    private RelativeLayout SyncLayout;
    private TextView SyncText;

    private CheckBox favorite;
    private CheckBox onTap;
    private CheckBox scheduled;

    private long  UserId;
    private ScrollView ScrollView;
    private Spinner styleSpinner;
    private ArrayAdapter<String> styleAdapter;
    private ArrayAdapter<String> brewMethodAdapter;

    private KeyListener brewNameListener;
    private KeyListener primaryListener;
    private KeyListener secondaryListener;
    private KeyListener bottleListener;
    private KeyListener descriptionListener;
    private KeyListener boilTimeListener;
    private KeyListener targetOGListener;
    private KeyListener targetFGListener;
    private KeyListener targetABVListener;
    private KeyListener IBUListener;
    private KeyListener BatchSizeListener;
    private KeyListener EfficiencyListener;
    private KeyListener SRMListener;

    private DataBaseManager dbManager;
    private BrewActivityData brewActivityData;
    private BrewSchema brewSchema;
    private boolean CanEdit = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onCreate");
        View returnView = inflater.inflate(R.layout.brew_view,container,false);

        //Add add edit layout default
        View mainView = inflater.inflate(R.layout.activity_add_edit_view_brew, null);

        ScrollView = (ScrollView)returnView.findViewById(R.id.BrewScrollView);
        ScrollView.addView(mainView);

        dbManager = DataBaseManager.getInstance(getActivity());

        brewImage = (ImageView)mainView.findViewById(R.id.BrewImageView);
        brewName = (EditText)mainView.findViewById(R.id.editTextBrewName);
        primary = (EditText)mainView.findViewById(R.id.editTextPrimary);
        secondary = (EditText)mainView.findViewById(R.id.editTextSecondary);
        bottle = (EditText)mainView.findViewById(R.id.editTextBottle);
        description = (EditText)mainView.findViewById(R.id.editTextDescription);
        boilTime = (EditText)mainView.findViewById(R.id.editTextBoilTime);
        targetOG = (EditText)mainView.findViewById(R.id.editTextTargetOG);
        targetFG = (EditText)mainView.findViewById(R.id.editTextTargetFG);
        targetABV = (EditText)mainView.findViewById(R.id.editTextTargetABV);
        IBU = (EditText)mainView.findViewById(R.id.editTextIBU);
        BatchSize = (EditText)mainView.findViewById(R.id.editTextBatchSize);
        Efficiency = (EditText)mainView.findViewById(R.id.editTextEfficiency);
        SRM = (EditText)mainView.findViewById(R.id.editTextSRM);

        SyncLayout = (RelativeLayout) returnView.findViewById(R.id.SyncRelativeLayout);
        SyncText = (TextView) returnView.findViewById(R.id.SyncTextView);

        startButton = (Button)returnView.findViewById(R.id.AddStartBrewButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartButtonClick(view);
            }
        });
        editBrewButton = (Button)returnView.findViewById(R.id.EditBrewButton);
        editBrewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditClick(view);
            }
        });

        uploadButton = (Button)returnView.findViewById(R.id.UploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(brewActivityData.getAddEditViewState() == BrewActivityData.DisplayMode.GLOBAL)
                    PullFromGlobal();
                else
                    SyncToGlobal();
            }
        });

        favorite = (CheckBox)mainView.findViewById(R.id.favoriteCheckBox);
        onTap = (CheckBox)mainView.findViewById(R.id.onTapCheckBox);
        scheduled = (CheckBox)mainView.findViewById(R.id.scheduledCheckBox);

        styleSpinner = (Spinner) returnView.findViewById(R.id.beerStylespinner);

        brewNameListener = brewName.getKeyListener();
        primaryListener = primary.getKeyListener();
        secondaryListener = secondary.getKeyListener();
        bottleListener = bottle.getKeyListener();
        descriptionListener = description.getKeyListener();
        boilTimeListener = boilTime.getKeyListener();
        targetOGListener = targetOG.getKeyListener();
        targetFGListener = targetFG.getKeyListener();
        targetABVListener = targetABV.getKeyListener();
        IBUListener = IBU.getKeyListener();
        BatchSizeListener = BatchSize.getKeyListener();
        EfficiencyListener = Efficiency.getKeyListener();
        SRMListener = SRM.getKeyListener();

        UserId = CurrentUser.getInstance().getUser().getUserId();

        brewActivityData = BrewActivityData.getInstance();
        CanEdit = brewActivityData.CanEdit();

        setBrewStyleSpinner();
        setMethodSpinner();
        method = (Spinner)mainView.findViewById(R.id.MethodSpinner);
        method.setAdapter(brewMethodAdapter);

        if(brewActivityData.getAddEditViewState() == BrewActivityData.DisplayMode.ADD) {
            ifAdd();
        }
         else if(brewActivityData.getAddEditViewState() == BrewActivityData.DisplayMode.EDIT) {
            brewSchema = brewActivityData.getAddEditViewBrew();
            ifEdit();
        }
        else if(brewActivityData.getAddEditViewState() == BrewActivityData.DisplayMode.VIEW) {
            brewSchema = brewActivityData.getAddEditViewBrew();
            ifView();
        }
        else if(brewActivityData.getAddEditViewState() == BrewActivityData.DisplayMode.GLOBAL) {
            brewSchema = brewActivityData.getAddEditViewBrew();
            ifGlobal();
        }

        //Hide Button if We cant edit
        if(!CanEdit) {
            startButton.setVisibility(View.INVISIBLE);
            editBrewButton.setVisibility(View.INVISIBLE);
        }

        if(CurrentUser.getInstance().getUser().getRole() != UserSchema.UserRoles.Admin.getValue()) {
            uploadButton.setVisibility(View.INVISIBLE);
        }

        return returnView;
    }

    public void ifAdd()
    {
        ClearFields();
        startButton.setVisibility(View.INVISIBLE);
        uploadButton.setVisibility(View.INVISIBLE);
        editBrewButton.setText("Submit");
        brewActivityData.setAddEditViewState(BrewActivityData.DisplayMode.ADD);
        ToggleFieldEditable(true);
    }

    public void ifEdit()
    {
        startButton.setVisibility(View.INVISIBLE);
        uploadButton.setVisibility(View.INVISIBLE);

        //get brew and display
        DisplayBrew(brewSchema);
        editBrewButton.setText("Submit");
        brewActivityData.setAddEditViewState(BrewActivityData.DisplayMode.EDIT);

        ToggleFieldEditable(false);
        ToggleFieldEditable(true);
    }

    public void ifView()
    {
        //get brew and display
        DisplayBrew(brewSchema);
        editBrewButton.setText("Edit Brew");
        startButton.setText("Start Brew");
        startButton.setVisibility(View.VISIBLE);
        uploadButton.setVisibility(View.VISIBLE);
        //Set EditText to not editable and hide button
        brewActivityData.setAddEditViewState(BrewActivityData.DisplayMode.VIEW);
        ToggleFieldEditable(false);

    }

    public void ifGlobal()
    {
        //get brew and display
        DisplayBrew(brewSchema);
        startButton.setVisibility(View.INVISIBLE);
        if(CurrentUser.getInstance().getUser().getRole() == UserSchema.UserRoles.Admin.getValue()) {
            uploadButton.setText("DownLoad");
            uploadButton.setVisibility(View.VISIBLE);
        }
        else
            uploadButton.setVisibility(View.INVISIBLE);
        //Set EditText to not editable and hide button
        brewActivityData.setAddEditViewState(BrewActivityData.DisplayMode.GLOBAL);
        ToggleFieldEditable(false);

    }

    private void setBrewStyleSpinner()
    {
        String[] brewStyles = new String[ APPUTILS.StyleMap.size()];

        Iterator it = APPUTILS.StyleMap.entrySet().iterator();
        int i=0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            brewStyles[i++]= pair.getKey().toString();
        }
        styleAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, brewStyles); //selected item will look like a spinner set from XML
        // Specify the layout to use when the list of choices appears
        styleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        styleSpinner.setAdapter(styleAdapter);
    }

    private void setMethodSpinner()
    {
        List<String> brewMethod = APPUTILS.BrewMethod;

        brewMethodAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, brewMethod);
        // Specify the layout to use when the list of choices appears
        brewMethodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void DisplayBrew(BrewSchema aBrewSchema)
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: DisplayBrew");
        //Reset all fields
        if(aBrewSchema.getBrewImageSchemaList().size() > 0) {
            int imageSize = 300;
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(aBrewSchema.getBrewImageSchemaList().get(0).getImage(), imageSize, imageSize, true);
            brewImage.setImageBitmap(scaledBitmap);
        }
        brewName.setText(aBrewSchema.getBrewName());
        primary.setText(Integer.toString(aBrewSchema.getPrimary()));
        secondary.setText(Integer.toString(aBrewSchema.getSecondary()));
        bottle.setText(Integer.toString(aBrewSchema.getBottle()));
        description.setText(aBrewSchema.getDescription());
        boilTime.setText(Integer.toString(aBrewSchema.getBoilTime()));
        targetOG.setText(Double.toString(aBrewSchema.getTargetOG()));
        targetFG.setText(Double.toString(aBrewSchema.getTargetFG()));
        targetABV.setText(Double.toString(APPUTILS.GetTruncatedABVPercent(aBrewSchema.getTargetABV()))+"%");
        IBU.setText(Double.toString(aBrewSchema.getIBU()));
        favorite.setChecked(aBrewSchema.getBooleanFavorite());
        onTap.setChecked(aBrewSchema.getBooleanOnTap());
        scheduled.setChecked(aBrewSchema.getBooleanScheduled());
        BatchSize.setText(Double.toString(aBrewSchema.getBatchSize()));
        Efficiency.setText(Double.toString(aBrewSchema.getEfficiency()));
        SRM.setText(Integer.toString(aBrewSchema.getSRM()));

        if(aBrewSchema.getBooleanIsDirty())
        {
            SyncLayout.setBackgroundColor(Color.RED);
            SyncText.setText("Not Synced");
        }
        else
        {
            SyncLayout.setBackgroundColor(getResources().getColor(R.color.AccentColor));
            SyncText.setText("Synced");
        }

        try
        {
            method.setSelection(brewMethodAdapter.getPosition(aBrewSchema.getMethod()));
        }
        catch (Exception e)
        {
            method.setSelection(0);
        }

        // set to brew Style this might be deleted if its user created
        try
        {
            styleSpinner.setSelection(styleAdapter.getPosition(aBrewSchema.getStyleType()));
        }
        catch (Exception e)
        {
            //if we are here user must have deleted brew style try to set to None
            try
            {
                styleSpinner.setSelection(styleAdapter.getPosition("None"));
            }
            catch (Exception ex)
            {
                // if all else fails set to index 0
                styleSpinner.setSelection(0);
            }
        }
    }


    public void onStartButtonClick(View aView)
    {
        if(brewActivityData.getAddEditViewState() == BrewActivityData.DisplayMode.VIEW)
        {
            //If we have an active timer we dont want to create a new one
            if(!(TimerData.getInstance().isTimerActive()))
            {
                //Set Brew into TimerData
                TimerData.getInstance().setTimerData(dbManager.getBrew(brewSchema.getBrewId()));
            }

            //Create and intent which will open Timer Activity
            Intent intent = new Intent(getActivity(), TimerPager.class);

            //start next activity
            startActivity(intent);
        }
    }

    public void onEditClick(View aView)
    {
        if(brewActivityData.getAddEditViewState() == BrewActivityData.DisplayMode.VIEW )
            ifEdit();
        else
            validateSubmit();
    }

    private void validateSubmit()
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: validateSubmit");

        if(brewName.getText().toString().equals(""))
        {
            Toast.makeText(getActivity(), "Blank Data Field", Toast.LENGTH_LONG).show();
            return;
        }

        int pf=0;
        int sf=0;
        int bc=0;
        int bt=0;
        int srm=0;

        double og=0.0;
        double fg=0.0;
        double abv=0.0;
        double ibu=0.0;
        double bs=0.0;
        double ef=0.0;

        try
        {
            pf  = Integer.parseInt(primary.getText().toString());
        }
        catch (Exception e){}
        try
        {
            sf = Integer.parseInt(secondary.getText().toString());
        }
        catch (Exception e){}
        try
        {
            bc = Integer.parseInt(bottle.getText().toString());
        }
        catch (Exception e){}
        try
        {
            bt = Integer.parseInt(boilTime.getText().toString());
        }
        catch (Exception e){}

        try
        {
            og = Double.parseDouble(targetOG.getText().toString());
        }
        catch (Exception e){}
        try
        {
            fg = Double.parseDouble(targetFG.getText().toString());
        }
        catch (Exception e){}
        try
        {
            abv = Double.parseDouble(targetABV.getText().toString());
        }
        catch (Exception e){}
        try
        {
            ibu = Double.parseDouble(IBU.getText().toString());
        }
        catch (Exception e){}
        try
        {
            bs = Double.parseDouble(BatchSize.getText().toString());
        }
        catch (Exception e){}
        try
        {
            ef = Double.parseDouble(Efficiency.getText().toString());
        }
        catch (Exception e){}
        try
        {
            srm = Integer.parseInt(SRM.getText().toString());
        }
        catch (Exception e){}


        //Create Brew
        BrewSchema brew;
        if(brewSchema == null)
            brew = new BrewSchema();
        else
            brew = brewSchema;

        brew.setBrewName(brewName.getText().toString());
        brew.setUserId(UserId);
        brew.setPrimary(pf);
        brew.setSecondary(sf);
        brew.setBottle(bc);
        brew.setTargetOG(og);
        brew.setTargetFG(fg);
        //brew.setTargetABV(abv);
        brew.setDescription(description.getText().toString());
        brew.setBoilTime(bt);
        brew.setStyleType(styleSpinner.getSelectedItem().toString());
        brew.setMethod(method.getSelectedItem().toString());
        brew.setIBU(ibu);
        brew.setBooleanFavorite(favorite.isChecked());
        brew.setBooleanOnTap(onTap.isChecked());
        brew.setBooleanScheduled(scheduled.isChecked());
        brew.setBatchSize(bs);
        brew.setEfficiency(ef);
        brew.setSRM(srm);

        //Add Boil additions
        brew.setBoilAdditionlist(BrewActivityData.getInstance().getBaArray());
        brew.setBrewNoteSchemaList(BrewActivityData.getInstance().getBrewNoteSchemaList());
        brew.setBrewInventorySchemaList(BrewActivityData.getInstance().getBrewInventorySchemaList());

        if(brewActivityData.getAddEditViewState() == BrewActivityData.DisplayMode.ADD)
        {
            //add user Id for lists
            brew.setListUserId();
            //brewSchema = brew;
            //try and sync to global
            //SyncToGlobal();
            long brewId = dbManager.CreateABrew(brew);
            if( brewId == 0)// 0 brews failed to create
            {
                Toast.makeText(getActivity(), "Duplicate Brew Name", Toast.LENGTH_LONG).show();
                return;
            }

            brewSchema = dbManager.getBrew(brewId);
        }
        else if(brewActivityData.getAddEditViewState() == BrewActivityData.DisplayMode.EDIT)
        {
            dbManager.updateABrew(brew);

            brewSchema = dbManager.getBrew(brew.getBrewId());
        }

        brewActivityData.setAddEditViewBrew(brewSchema);

        ifView();
    }

    private void ClearFields()
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: ClearFields");
        //Reset all fields
        brewName.setText("");
        primary.setText("");
        secondary.setText("");
        bottle.setText("");
        description.setText("");
        boilTime.setText("");
        targetOG.setText("");
        targetFG.setText("");
        targetABV.setText("");
        method.setSelection(0);
        IBU.setText("");
        favorite.setChecked(false);
        onTap.setChecked(false);
        scheduled.setChecked(false);
        BatchSize.setText("");
        Efficiency.setText("");
        SRM.setText("");
        // set to None there should always be a None
        try
        {
            styleSpinner.setSelection(styleAdapter.getPosition("None"));
        }
        catch (Exception e)
        {
            // if for some reason None doesn't exist set to index 0
            styleSpinner.setSelection(0);
        }

    }

    private void ToggleFieldEditable(boolean aEditable)
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: ToggleFieldEditable");
        //Reset all fields
        if(!aEditable) {
            //addEditButton.setVisibility(View.INVISIBLE);
            brewName.setKeyListener(null);
            brewName.setClickable(false);
            brewName.setEnabled(false);
            //brewName.setFocusable(false);

            primary.setKeyListener(null);
            primary.setClickable(false);
            primary.setEnabled(false);
           //primary.setFocusable(false);

            secondary.setKeyListener(null);
            secondary.setClickable(false);
            secondary.setEnabled(false);
            //secondary.setFocusable(false);

            bottle.setKeyListener(null);
            bottle.setClickable(false);
            bottle.setEnabled(false);
            //bottle.setFocusable(false);

            description.setKeyListener(null);
            description.setClickable(false);
            description.setEnabled(false);
            //description.setFocusable(false);

            boilTime.setKeyListener(null);
            boilTime.setClickable(false);
            boilTime.setEnabled(false);
            //boilTime.setFocusable(false);

            targetOG.setKeyListener(null);
            targetOG.setClickable(false);
            targetOG.setEnabled(false);
            //targetOG.setFocusable(false);

            targetFG.setKeyListener(null);
            targetFG.setClickable(false);
            targetFG.setEnabled(false);
            //targetFG.setFocusable(false);

            targetABV.setKeyListener(null);
            targetABV.setClickable(false);
            targetABV.setEnabled(false);
            //targetABV.setFocusable(false);

            method.setClickable(false);
            method.setEnabled(false);
            //method.setFocusable(false);

            IBU.setKeyListener(null);
            IBU.setClickable(false);
            IBU.setEnabled(false);
            //IBU.setFocusable(false);

            BatchSize.setKeyListener(null);
            BatchSize.setClickable(false);
            BatchSize.setEnabled(false);
            //BatchSize.setFocusable(false);

            Efficiency.setKeyListener(null);
            Efficiency.setClickable(false);
            Efficiency.setEnabled(false);
            //Efficiency.setFocusable(false);

            SRM.setKeyListener(null);
            SRM.setClickable(false);
            SRM.setEnabled(false);
            //Efficiency.setFocusable(false);

            styleSpinner.setClickable(false);
            styleSpinner.setEnabled(false);

            favorite.setClickable(false);
            onTap.setClickable(false);
            scheduled.setClickable(false);
        }
        else
        {
            brewName.setKeyListener(brewNameListener);
            brewName.setClickable(true);
            brewName.setEnabled(true);
            brewName.setFocusable(true);

            primary.setKeyListener(primaryListener);
            primary.setClickable(true);
            primary.setEnabled(true);
            primary.setFocusable(true);

            secondary.setKeyListener(secondaryListener);
            secondary.setClickable(true);
            secondary.setEnabled(true);
            secondary.setFocusable(true);

            bottle.setKeyListener(bottleListener);
            bottle.setClickable(true);
            bottle.setEnabled(true);
            bottle.setFocusable(true);

            description.setKeyListener(descriptionListener);
            description.setClickable(true);
            description.setEnabled(true);
            description.setFocusable(true);

            boilTime.setKeyListener(boilTimeListener);
            boilTime.setClickable(true);
            boilTime.setEnabled(true);
            boilTime.setFocusable(true);

            targetOG.setKeyListener(targetOGListener);
            targetOG.setClickable(true);
            targetOG.setEnabled(true);
            //targetOG.setFocusable(true);

            targetFG.setKeyListener(targetFGListener);
            targetFG.setClickable(true);
            targetFG.setEnabled(true);
            //targetFG.setFocusable(true);

            targetABV.setKeyListener(null);
            targetABV.setClickable(false);
            targetABV.setEnabled(false);
            //targetABV.setFocusable(true);

            method.setClickable(true);
            method.setEnabled(true);
            //method.setFocusable(true);

            IBU.setKeyListener(IBUListener);
            IBU.setClickable(true);
            IBU.setEnabled(true);
            //IBU.setFocusable(true);

            BatchSize.setKeyListener(BatchSizeListener);
            BatchSize.setClickable(true);
            BatchSize.setEnabled(true);
            //BatchSize.setFocusable(true);

            Efficiency.setKeyListener(EfficiencyListener);
            Efficiency.setClickable(true);
            Efficiency.setEnabled(true);
            //Efficiency.setFocusable(true);

            SRM.setKeyListener(SRMListener);
            SRM.setClickable(true);
            SRM.setEnabled(true);
            //Efficiency.setFocusable(false);

            styleSpinner.setClickable(true);
            styleSpinner.setEnabled(true);

            favorite.setClickable(true);
            onTap.setClickable(true);
            scheduled.setClickable(true);
        }
    }

    private void SyncToGlobal()
    {

        if(!APPUTILS.HasInternet(getActivity())) {
            Toast.makeText(getActivity(), "Need Internet To Perform", Toast.LENGTH_SHORT).show();
            return;
        }

        Response.Listener<String> ResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(APPUTILS.isLogging)Log.e(LOG, "response: "+response);
                if (response.equals("Error"))// no match for user exists
                {

                } else {
                    //Pase response
                    try {

                        //Parse response into BrewShcema list
                        JSONArray jsonArray = new JSONArray(response);
                        JSONBrewParser jsonParser = new JSONBrewParser(getActivity());
                        List<BrewSchema> brewSchemaList = jsonParser.ParseGlobalBrews(jsonArray);

                        if(brewSchemaList.size() == 1) {
                            dbManager.DeleteBrew(brewSchema.getBrewId(),brewSchema.getUserId());
                            for (BrewSchema bs : brewSchemaList) {
                                dbManager.CreateAnExistingBrew(bs);
                                brewSchema = dbManager.getBrew(bs.getBrewId());
                            }
                            BrewActivityData.getInstance().setAddEditViewBrew(brewSchema);
                            DisplayBrew(brewSchema);

                            Toast.makeText(getActivity(), brewSchema.getBrewName() + " Synced", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e) {}
                }
            }
        };

        CreateBrewRequest createBrewRequest = new CreateBrewRequest(brewSchema, ResponseListener,null);
        WebController.getInstance().addToRequestQueue(createBrewRequest);
    }

    private void PullFromGlobal()
    {
        Response.Listener<String> ResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(APPUTILS.isLogging)Log.e(LOG, response);
                if (response.equals("Error"))// no match for user exists
                {
                    Toast.makeText(getActivity(), brewSchema.getBrewName() +" Upload Failed", Toast.LENGTH_SHORT).show();
                } else {
                    //Pase response
                    try {

                        //Parse response into BrewShcema list
                        JSONArray jsonArray = new JSONArray(response);
                        JSONBrewParser jsonParser = new JSONBrewParser(getActivity());
                        List<BrewSchema> brewSchemaList = jsonParser.ParseGlobalBrews(jsonArray);

                        //For each brew schema up it SHOULD ONLY BE 1
                        if (!(brewSchemaList.size() > 1))
                        {
                            for (BrewSchema bs : brewSchemaList) {
                                //TODO: Need to remove UserId field from all  brew child tables
                                bs.setUserId(UserId);
                                bs.setListUserId();
                                dbManager.CreateABrew(bs);
                            }


                            Toast.makeText(getActivity(), brewSchema.getBrewName() + " Downloaded", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e) {}
                }
            }
        };

        GetBrewRequest getBrewRequest = new GetBrewRequest(Long.toString(brewSchema.getBrewId()), ResponseListener,null);
        WebController.getInstance().addToRequestQueue(getBrewRequest);
    }
}
