package com.dev.town.small.brewtopia.Brews;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.DataClass.ScheduledBrewSchema;
import com.dev.town.small.brewtopia.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 2/20/2016.
 */
public class CustomCompListAdapter extends BaseAdapter implements ListAdapter  {

    private List<ScheduledBrewSchema> list = new ArrayList<ScheduledBrewSchema>();
    private Context context;
    private boolean isDeleteView = false;
    private boolean hasColor = false;

    //event handling
    private EventHandler eventHandler = null;

    public CustomCompListAdapter(List<ScheduledBrewSchema> list, Context context) {
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
            view = inflater.inflate(R.layout.custom_row_comp_brew_view, null);
        }

        ScheduledBrewSchema sbrewSchema = list.get(position);

        //If we have a color we wamt to create color layout
        if(hasColor)
        {
            LinearLayout colorLayout= (LinearLayout)view.findViewById(R.id.ColorlinearLayout);
            colorLayout.setMinimumHeight(290);
            colorLayout.setMinimumWidth(80);
            try{
                colorLayout.setBackgroundColor(Color.parseColor(APPUTILS.StyleMap.get(sbrewSchema.getStyleType()).toString()));
            }
            catch (Exception e)
            {
                colorLayout.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        //Handle TextView and display string from list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(sbrewSchema.getBrewName());

        TextView listItemText1 = (TextView)view.findViewById(R.id.list_item_string1);
        listItemText1.setText(sbrewSchema.getEndBrewDate());

        TextView listItemText2 = (TextView)view.findViewById(R.id.list_item_string2);
        listItemText2.setText("ABV: " + Double.toString(APPUTILS.GetTruncatedABVPercent(sbrewSchema.getABV())) + "%");

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
        void OnDeleteClickListener(ScheduledBrewSchema brewSchema);
    }
}