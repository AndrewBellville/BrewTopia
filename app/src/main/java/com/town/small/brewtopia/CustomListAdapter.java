package com.town.small.brewtopia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Andrew on 2/20/2016.
 */
public class CustomListAdapter extends BaseAdapter implements ListAdapter  {

    private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    private Context context;
    private boolean isDeleteView = false;

    //event handling
    private EventHandler eventHandler = null;

    public CustomListAdapter(ArrayList<HashMap<String,String>> list, Context context) {
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
            view = inflater.inflate(R.layout.custom_row_view, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position).get("text1"));

        TextView listItemText1 = (TextView)view.findViewById(R.id.list_item_string1);
        listItemText1.setText(list.get(position).get("text2"));

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);

        if(!isDeleteView) deleteBtn.setVisibility(View.INVISIBLE);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                if(eventHandler != null)
                    eventHandler.OnDeleteClickListener(list.get(position).get("text1"),list.get(position).get("text2"));

                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });

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
        void OnDeleteClickListener(String aText1, String aText2);
    }
}