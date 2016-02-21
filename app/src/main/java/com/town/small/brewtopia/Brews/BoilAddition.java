package com.town.small.brewtopia.Brews;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.text.Layout;
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

    private LinearLayout layout;
    private TextView additionNameLabel;
    private EditText additionName;
    private TextView timeLabel;
    private EditText time;
    private Button deleteButton;

    //event handling
    private EventHandler eventHandler = null;

    public BoilAddition(Context context) {

        baSchema = new BoilAdditionsSchema();

        layout = new LinearLayout(context);
        layout.setBackgroundColor(Color.argb(50,153,225,204));
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));


        additionNameLabel = new TextView(context);
        additionNameLabel.setText("Addition");

        additionName = new EditText(context);
        additionName.setWidth(200);

        timeLabel = new TextView(context);
        timeLabel.setText("Min:");

        time = new EditText(context);
        time.setInputType(InputType.TYPE_CLASS_NUMBER);
        time.setWidth(100);

        deleteButton = new Button(context);
        deleteButton.setText("-");
        //deleteButton.setBackgroundResource(R.drawable.minus);
        deleteButton.setMaxHeight(100);
        deleteButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(additionName.getText().toString().equals("") || eventHandler == null)
                    return;
                eventHandler.OnDeleteClickListener(additionName.getText().toString());
            }
        });

        layout.addView(additionNameLabel);
        layout.addView(additionName);
        layout.addView(timeLabel);
        layout.addView(time);
        layout.addView(deleteButton);
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
