package com.town.small.brewtopia.Brews;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.town.small.brewtopia.DataClass.BrewNoteSchema;
import com.town.small.brewtopia.DataClass.ScheduledBrewSchema;
import com.town.small.brewtopia.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 2/20/2016.
 */
public class CustomNListAdapter extends BaseAdapter implements ListAdapter  {

    private List<BrewNoteSchema> list = new ArrayList<BrewNoteSchema>();
    private Context context;

    //event handling
    private EventHandler eventHandler = null;

    public CustomNListAdapter(List<BrewNoteSchema> list, Context context) {
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
            view = inflater.inflate(R.layout.custom_row_brew_note, null);
        }

        BrewNoteSchema nBrew = list.get(position);

        String text1 = nBrew.getCreatedOn();
        String text2 = nBrew.getBrewNote();

        //Handle TextView and display string from list
        TextView listItemText = (TextView)view.findViewById(R.id.DateTextView);
        listItemText.setText(text1);

        EditText listItemText1 = (EditText) view.findViewById(R.id.NoteEditText);
        listItemText1.setText(text2);

        Button editbutton = (Button)view.findViewById(R.id.EditButton);

        editbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                if(eventHandler != null)
                {
                    BrewNoteSchema nBrew = list.get(position);
                    eventHandler.OnEditClick(nBrew);
                }

                notifyDataSetChanged();
            }
        });


        listItemText.setFocusable(false);
        listItemText.setClickable(false);
        listItemText.setEnabled(false);

        listItemText1.setFocusable(false);
        listItemText1.setClickable(false);
        listItemText1.setEnabled(false);

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
    public interface EventHandler
    {
        void OnEditClick(BrewNoteSchema aBrewNoteSchema);
    }
}