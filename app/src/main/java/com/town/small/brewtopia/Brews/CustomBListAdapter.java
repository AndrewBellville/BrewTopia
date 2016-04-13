package com.town.small.brewtopia.Brews;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.town.small.brewtopia.DataClass.BrewSchema;
import com.town.small.brewtopia.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Andrew on 2/20/2016.
 */
public class CustomBListAdapter extends BaseAdapter implements ListAdapter  {

    private List<BrewSchema> list = new ArrayList<BrewSchema>();
    private Context context;
    private boolean isDeleteView = false;
    private boolean hasColor = false;

    //event handling
    private EventHandler eventHandler = null;

    public CustomBListAdapter(List<BrewSchema> list, Context context) {
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
            view = inflater.inflate(R.layout.custom_row_brew_view, null);
        }

        BrewSchema brewSchema = list.get(position);

        //If we have a color we need to strip it off text2 and sent color panel height and width
        if(hasColor)
        {
            LinearLayout colorLayout= (LinearLayout)view.findViewById(R.id.ColorlinearLayout);
            colorLayout.setMinimumHeight(250);
            colorLayout.setMinimumWidth(80);
            try{
                colorLayout.setBackgroundColor(Color.parseColor(brewSchema.getStyleSchema().getBrewStyleColor()));
            }
            catch (Exception e)
            {
                colorLayout.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        //Handle TextView and display string from list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(brewSchema.getBrewName());

        TextView listItemText1 = (TextView)view.findViewById(R.id.list_item_string1);
        listItemText1.setText(brewSchema.getStyle() +" - "+brewSchema.getTargetABV()+"%");

        TextView listItemText2 = (TextView)view.findViewById(R.id.list_item_string2);
        String discriptionString;
        if(brewSchema.getDescription().length() >= 25)
            discriptionString = brewSchema.getDescription().substring(0,25)+"...";
        else
            discriptionString = brewSchema.getDescription();

        listItemText2.setText(discriptionString);

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);

        if(!isDeleteView) deleteBtn.setVisibility(View.INVISIBLE);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                if(eventHandler != null)
                {
                    BrewSchema brewSchema = list.get(position);
                    eventHandler.OnDeleteClickListener(brewSchema);
                }

                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });

        return view;
    }

    public void setDeleteView(boolean isDeleteView) {
        this.isDeleteView = isDeleteView;
    }

    public void hasColor(boolean color) {
        this.hasColor = color;
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
    public interface EventHandler
    {
        void OnDeleteClickListener(BrewSchema brewSchema);
    }
}