package com.dev.town.small.brewtopia.Timer;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dev.town.small.brewtopia.DataClass.BoilAdditionsSchema;
import com.dev.town.small.brewtopia.DataClass.ScheduledBrewSchema;
import com.dev.town.small.brewtopia.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 2/20/2016.
 */
public class CustomAddListAdapter extends BaseAdapter implements ListAdapter  {

    private List<BoilAdditionsSchema> list = new ArrayList<BoilAdditionsSchema>();
    private Context context;

    //event handling
    private EventHandler eventHandler = null;

    public CustomAddListAdapter(List<BoilAdditionsSchema> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        //return list.get(pos).getId();
        //just return 0 if your list items do not have an Id variable.
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_brew_timer_row, null);
        }

        BoilAdditionsSchema boilAdditionsSchema = list.get(position);

        String text1 = boilAdditionsSchema.getAdditionName();

        //Handle TextView and display string from list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        String truncatedString;
        if(text1.length() >= 40)
            truncatedString = text1.substring(0,40)+"...";
        else
            truncatedString = text1;

        listItemText.setText(truncatedString);

        TextView listItemText1 = (TextView)view.findViewById(R.id.list_item_string1);
        listItemText1.setText(boilAdditionsSchema.getAdditionQty() +" "+ boilAdditionsSchema.getUOfM() );


        TextView listItemText3 = (TextView)view.findViewById(R.id.list_item_string3);
        listItemText3.setText(boilAdditionsSchema.getAdditionTime() +  " Mins" );

        return view;
    }

    /**
     * Assign event handler to be passed needed events
     */
    public void setEventHandler(EventHandler eventHandler)
    {
        this.eventHandler = eventHandler;
    }

    /**
     * This interface defines what events to be reported to
     * the outside world
     */
    public interface EventHandler {}
}