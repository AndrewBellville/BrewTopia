package com.dev.town.small.brewtopia.Schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dev.town.small.brewtopia.DataClass.ScheduledEventSchema;
import com.dev.town.small.brewtopia.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 2/20/2016.
 */
public class CustomSEListAdapter extends BaseAdapter implements ListAdapter  {

    private List<ScheduledEventSchema> list = new ArrayList<ScheduledEventSchema>();
    private Context context;
    private boolean isDeleteView = false;

    //event handling
    private EventHandler eventHandler = null;

    public CustomSEListAdapter(List<ScheduledEventSchema> list, Context context) {
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
            view = inflater.inflate(R.layout.custom_schedule_event_row_view, null);
        }

        ScheduledEventSchema scheduledEventSchemaBrew = list.get(position);

        String text1 = scheduledEventSchemaBrew.getEventText();

        //Handle TextView and display string from list
        TextView eventText = (TextView)view.findViewById(R.id.eventText);
        String truncatedString;
        if(text1.length() >= 15)
            truncatedString = text1.substring(0,15)+"...";
        else
            truncatedString = text1;


        eventText.setText(truncatedString);

        TextView eventDate = (TextView)view.findViewById(R.id.eventDate);
        eventDate.setText(scheduledEventSchemaBrew.getEventDate());

        eventDate.setFocusable(true);
        eventDate.setClickable(true);
        eventDate.setEnabled(true);

        return view;
    }

    public void setDeleteView(boolean isDeleteView) {
        this.isDeleteView = isDeleteView;
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
        void OnEditClick(ScheduledEventSchema aScheduledEventSchema);
    }
}