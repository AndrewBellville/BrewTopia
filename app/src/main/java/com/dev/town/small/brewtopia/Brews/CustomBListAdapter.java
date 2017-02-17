package com.dev.town.small.brewtopia.Brews;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.DataClass.BrewSchema;
import com.dev.town.small.brewtopia.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrew on 2/20/2016.
 */
public class CustomBListAdapter extends BaseAdapter implements ListAdapter  {

    private List<BrewSchema> list = new ArrayList<BrewSchema>();
    private Context context;
    private boolean isDeleteView = false;
    private boolean hasColor = false;
    private int position;

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
    public View getView(final int pos, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_row_brew_view, null);
        }
        position = pos;

        BrewSchema brewSchema = list.get(position);

        //If we have a color we need to strip it off text2 and sent color panel height and width
        if(hasColor)
        {
            LinearLayout colorLayout= (LinearLayout)view.findViewById(R.id.ColorlinearLayout);
            colorLayout.setMinimumHeight(290);
            colorLayout.setMinimumWidth(80);
            try{
                colorLayout.setBackgroundColor(Color.parseColor(brewSchema.getStyleSchema().getBrewStyleColor()));
            }
            catch (Exception e)
            {
                colorLayout.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        //Set SRM color Gradient
        int[] colors = new int[2];
        colors[0] = Color.TRANSPARENT;
        Iterator it = APPUTILS.SRMMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(brewSchema.getSRM() == 0 ) {
                colors[1] = Color.TRANSPARENT;
                break;
            }
            else if(brewSchema.getSRM() >= (int) pair.getKey() )
                colors[1] = Color.parseColor(pair.getValue().toString());
            else
                break;
        }
        //create a new gradient color
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        view.setBackground(gd);

        //Handle TextView and display string from list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        String truncatedString;
        if(brewSchema.getBrewName().length() >= 25)
            truncatedString = brewSchema.getBrewName().substring(0,25)+"...";
        else
            truncatedString = brewSchema.getBrewName();

        listItemText.setText(truncatedString);

        TextView listItemText1 = (TextView)view.findViewById(R.id.list_item_string1);
        listItemText1.setText(brewSchema.getStyle() +" - "+ APPUTILS.GetTruncatedABVPercent(brewSchema.getTargetABV())+"%");

        TextView listItemText2 = (TextView)view.findViewById(R.id.list_item_string2);
        /*if(brewSchema.getDescription().length() >= 25)
            truncatedString = brewSchema.getDescription().substring(0,25)+"...";
        else
            truncatedString = brewSchema.getDescription();

        listItemText2.setText(truncatedString);*/
        listItemText2.setText("Times Brewed:  "+  brewSchema.getTotalBrewed());

        // Icons
        ImageView favoriteIcon = (ImageView) view.findViewById(R.id.favoriteImage);
        ImageView scheduledIcon = (ImageView) view.findViewById(R.id.scheduledImage);
        ImageView onTapIcon = (ImageView) view.findViewById(R.id.onTapImage);

        if(brewSchema.getBooleanFavorite())
            favoriteIcon.setVisibility(View.VISIBLE);
        else
            favoriteIcon.setVisibility(View.INVISIBLE);

        if(brewSchema.getBooleanScheduled())
            scheduledIcon.setVisibility(View.VISIBLE);
        else
            scheduledIcon.setVisibility(View.INVISIBLE);

        if(brewSchema.getBooleanOnTap())
            onTapIcon.setVisibility(View.VISIBLE);
        else
            onTapIcon.setVisibility(View.INVISIBLE);

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);
        if(!isDeleteView) deleteBtn.setVisibility(View.INVISIBLE);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                Verify();
            }
        });

        return view;
    }

    private void Verify()
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(which == DialogInterface.BUTTON_POSITIVE)
                    onDeleteClick();
                else if(which ==DialogInterface.BUTTON_NEGATIVE)
                    return;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    private void onDeleteClick()
    {
        if(eventHandler != null)
        {
            BrewSchema brewSchema = list.get(position);
            eventHandler.OnDeleteClickListener(brewSchema);
            list.remove(position);
            notifyDataSetChanged();
        }
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