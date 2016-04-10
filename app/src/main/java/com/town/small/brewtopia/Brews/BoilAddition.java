package com.town.small.brewtopia.Brews;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.town.small.brewtopia.DataClass.*;
import com.town.small.brewtopia.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 3/7/2015.
 */
public class BoilAddition {

    private BoilAdditionsSchema baSchema;

    private TextView additionNameLabel;
    private EditText additionName;
    private TextView timeLabel;
    private EditText qty;
    private TextView qtyLabel;
    private EditText time;
    private Button deleteButton;
    private Spinner UofMSpinner;
    ArrayAdapter<String> UofMAdapter;

    private View view;

    //event handling
    private EventHandler eventHandler = null;

    public BoilAddition(Context context) {

        baSchema = new BoilAdditionsSchema();

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.custom_row_boil_additions, null, false);

        additionName = (EditText) view.findViewById(R.id.list_item_edit);
        time = (EditText) view.findViewById(R.id.list_item_edit1);
        time.setText("0");
        qty = (EditText) view.findViewById(R.id.list_item_edit2);
        qty.setText("0");

        additionNameLabel = (TextView) view.findViewById(R.id.list_item_label);
        additionNameLabel.setText("Addition:");
        timeLabel = (TextView) view.findViewById(R.id.list_item_label1);
        timeLabel.setText("Min:");
        qtyLabel = (TextView) view.findViewById(R.id.list_item_label2);
        qtyLabel.setText("Qty:");

        UofMSpinner = (Spinner)view.findViewById(R.id.UofMSpinner);
        setColorSpinner(context);

        deleteButton = (Button) view.findViewById(R.id.delete_btn);
        deleteButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(additionName.getText().toString().equals("") || eventHandler == null)
                    return;
                eventHandler.OnDeleteClickListener(additionName.getText().toString());
            }
        });
    }

    public void setSelf()
    {
        int t=0;
        int q=0;

        try
        {
            t  = Integer.parseInt(time.getText().toString());
        }
        catch (Exception e){}
        try
        {
            q = Integer.parseInt(qty.getText().toString());
        }
        catch (Exception e){}

        baSchema.setAdditionName(additionName.getText().toString());
        baSchema.setAdditionTime(t);
        baSchema.setAdditionQty(q);
        baSchema.setUOfM(UofMSpinner.getSelectedItem().toString());
    }

    public void setDisplay()
    {
        additionName.setText(baSchema.getAdditionName());
        time.setText(String.valueOf(baSchema.getAdditionTime()));
        qty.setText(String.valueOf(baSchema.getAdditionQty()));
        try
        {
            UofMSpinner.setSelection(UofMAdapter.getPosition(baSchema.getUOfM()));
        }
        catch (Exception e)
        {
            UofMSpinner.setSelection(0);
        }
    }

    private void setColorSpinner(Context context)
    {
        List<String> UOfMs = new ArrayList<String>();
        UOfMs.add("Cups");
        UOfMs.add("oz");

        UofMAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, UOfMs);
        // Specify the layout to use when the list of choices appears
        UofMAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        UofMSpinner.setAdapter(UofMAdapter);
    }

    public boolean isPopulated()
    {
        String an = additionName.getText().toString();
        String t = time.getText().toString();
        String q = qty.getText().toString();

        if(an.matches("") || t.matches("") || q.matches(""))
            return false;

        return true;
    }

    public void setEditable(boolean isEditable)
    {
        //always false
        additionName.setKeyListener(null);
        additionName.setClickable(false);
        additionName.setEnabled(false);

        time.setKeyListener(null);
        time.setClickable(false);
        time.setEnabled(false);

        qty.setKeyListener(null);
        qty.setClickable(false);
        qty.setEnabled(false);

        deleteButton.setVisibility(View.INVISIBLE);
        UofMSpinner.setClickable(false);
    }

    /**
     * Assign event handler to be passed needed events
     */
    public void setEventHandler(EventHandler eventHandler)
    {
        this.eventHandler = eventHandler;
    }

    public interface EventHandler
    {
        void OnDeleteClickListener(String aAdditionName);
    }

    //getters
    public View getView() {
        return view;
    }
    public BoilAdditionsSchema getBaSchema() {
        return baSchema;
    }

    //setters
    public void setView (View view) {
        this.view = view;
    }
    public void setBaSchema(BoilAdditionsSchema baSchema) {
        this.baSchema = baSchema;
    }




}
