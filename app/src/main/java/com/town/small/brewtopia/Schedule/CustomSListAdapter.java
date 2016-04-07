package com.town.small.brewtopia.Schedule;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.town.small.brewtopia.DataClass.ScheduledBrewSchema;
import com.town.small.brewtopia.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Andrew on 2/20/2016.
 */
public class CustomSListAdapter extends BaseAdapter implements ListAdapter  {

    private ArrayList<ScheduledBrewSchema> list = new ArrayList<ScheduledBrewSchema>();
    private Context context;
    private boolean isDeleteView = false;
    private boolean hasColor = false;

    //event handling
    private EventHandler eventHandler = null;

    public CustomSListAdapter(ArrayList<ScheduledBrewSchema> list, Context context) {
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
            view = inflater.inflate(R.layout.custom_schedule_row_view, null);
        }

       ScheduledBrewSchema sBrew = list.get(position);

        String text1 = sBrew.getBrewName();
        String text2 = sBrew.getStartDate();

        if(hasColor)
        {
            LinearLayout colorLayout= (LinearLayout)view.findViewById(R.id.ColorlinearLayout);
            colorLayout.setMinimumHeight(150);
            colorLayout.setMinimumWidth(70);
            try{
                colorLayout.setBackgroundColor(Color.parseColor(sBrew.getColor()));
            }
            catch (Exception e)
            {
                colorLayout.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        //Handle TextView and display string from list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(text1);

        TextView listItemText1 = (TextView)view.findViewById(R.id.list_item_string1);
        listItemText1.setText(text2);


        TextView CurrentState = (TextView)view.findViewById(R.id.CurrentStateTextView);
        CurrentState.setText(sBrew.getCurrentState());

        TextView NextEventDate = (TextView)view.findViewById(R.id.NextEventTextView);
        NextEventDate.setText("");

        if(sBrew.isShowAsAlert()) {
            ImageView AlertImage = (ImageView)view.findViewById(R.id.ActionImageView);
            AlertImage.setVisibility(View.VISIBLE);
        }


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
        void OnDeleteClickListener(int ScheduleId);
    }
}