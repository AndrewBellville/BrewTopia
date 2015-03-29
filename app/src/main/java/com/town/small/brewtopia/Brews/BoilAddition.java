package com.town.small.brewtopia.Brews;

import android.app.ActionBar;
import android.content.Context;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.town.small.brewtopia.DataClass.*;

/**
 * Created by Andrew on 3/7/2015.
 */
public class BoilAddition {

    private BoilAdditionsSchema baSchema;

    private LinearLayout layout;
    private TextView additionNameLabel;
    private EditText additionName;
    private TextView timeLabel;
    private EditText time;

    public BoilAddition(Context context) {

        baSchema = new BoilAdditionsSchema();

        layout = new LinearLayout(context);

        additionNameLabel = new TextView(context);
        additionNameLabel.setText("Addition");

        additionName = new EditText(context);
        additionName.setWidth(200);

        timeLabel = new TextView(context);
        timeLabel.setText("Min:");

        time = new EditText(context);
        time.setInputType(InputType.TYPE_CLASS_NUMBER);
        time.setWidth(100);

        layout.addView(additionNameLabel);
        layout.addView(additionName);
        layout.addView(timeLabel);
        layout.addView(time);
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
        if(additionName.getText().equals("") || time.getText().equals(""))
        {
            return false;
        }
        return true;
    }

    public void setEditable(boolean isEditable)
    {
        //always false
        additionName.setKeyListener(null);
        time.setKeyListener(null);
    }

    //getters
    public LinearLayout getLayout() {
        return layout;
    }
    public BoilAdditionsSchema getBaSchema() {
        return baSchema;
    }

    //setters
    public void setLayout(LinearLayout layout) {
        this.layout = layout;
    }
    public void setBaSchema(BoilAdditionsSchema baSchema) {
        this.baSchema = baSchema;
    }




}
