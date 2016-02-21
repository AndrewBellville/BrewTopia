package com.town.small.brewtopia.Brews;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.town.small.brewtopia.DataClass.*;
import com.town.small.brewtopia.R;

/**
 * Created by Andrew on 3/7/2015.
 */
public class BoilAddition {

    private BoilAdditionsSchema baSchema;

    private TextView additionNameLabel;
    private EditText additionName;
    private TextView timeLabel;
    private EditText time;
    private Button deleteButton;

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

        additionNameLabel = (TextView) view.findViewById(R.id.list_item_label);
        additionNameLabel.setText("Addition:");
        timeLabel = (TextView) view.findViewById(R.id.list_item_label1);
        timeLabel.setText("Min:");

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
        baSchema.setAdditionName(additionName.getText().toString());
        baSchema.setAdditionTime(Integer.parseInt(time.getText().toString()));
    }

    public void setDisplay()
    {
        additionName.setText(baSchema.getAdditionName());
        time.setText(String.valueOf(baSchema.getAdditionTime()));
    }

    public boolean isPopulated()
    {
        String an = additionName.getText().toString();
        String t = time.getText().toString();

        if(an.matches("") || t.matches(""))
            return false;

        return true;
    }

    public void setEditable(boolean isEditable)
    {
        //always false
        additionName.setKeyListener(null);
        time.setKeyListener(null);
        deleteButton.setVisibility(View.INVISIBLE);
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
