package com.town.small.brewtopia.Brews;

    import android.content.Context;
    import android.content.Intent;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ArrayAdapter;
    import android.widget.BaseAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ListAdapter;
    import android.widget.Spinner;
    import android.widget.TextView;

    import com.town.small.brewtopia.DataClass.APPUTILS;
    import com.town.small.brewtopia.DataClass.BoilAdditionsSchema;
    import com.town.small.brewtopia.R;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;

/**
 * Created by Andrew on 2/20/2016.
 */
public class CustomBoilAdditionsAdapter extends BaseAdapter implements ListAdapter {

    private List<BoilAdditionsSchema> list = new ArrayList<BoilAdditionsSchema>();
    private Context context;

    //event handling
    private EventHandler eventHandler = null;
    private boolean isEditable = true;

    public CustomBoilAdditionsAdapter(List<BoilAdditionsSchema> list, Context context) {
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
            view = inflater.inflate(R.layout.custom_row_boil_additions, null);
        }

        TextView listItemText = (TextView)view.findViewById(R.id.list_item_label);
        listItemText.setText("Name: ");
        TextView listItemText1 = (TextView)view.findViewById(R.id.list_item_label1);
        listItemText1.setText("Time: ");
        TextView listItemText2 = (TextView)view.findViewById(R.id.list_item_label2);
        listItemText2.setText("Qty: ");

        BoilAdditionsSchema boilAdditionsSchema = list.get(position);

        String text1 = boilAdditionsSchema.getAdditionName();
        String text2 = Integer.toString(boilAdditionsSchema.getAdditionTime());
        String text3 = Double.toString(boilAdditionsSchema.getAdditionQty());

        //Handle TextView and display string from your list
        EditText listItemEditText = (EditText)view.findViewById(R.id.list_item_edit);
        listItemEditText.setText(text1);

        EditText listItemEditText1 = (EditText)view.findViewById(R.id.list_item_edit1);
        listItemEditText1.setText(text2);

        EditText listItemEditText2 = (EditText)view.findViewById(R.id.list_item_edit2);
        listItemEditText2.setText(text3);

        //Handle buttons and add onClickListeners
        Button editButton = (Button)view.findViewById(R.id.edit_btn);
        if(!isEditable) editButton.setVisibility(View.INVISIBLE);


        Spinner UofMSpinner = (Spinner)view.findViewById(R.id.UofMSpinner);
        UofMSpinner.setEnabled(false);
        ArrayAdapter<String> UofMAdapter;
        List<String> UOfMs = APPUTILS.UofM;

        UofMAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, UOfMs);
        // Specify the layout to use when the list of choices appears
        UofMAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        UofMSpinner.setAdapter(UofMAdapter);

        try
        {
            UofMSpinner.setSelection(UofMAdapter.getPosition(boilAdditionsSchema.getUOfM()));
        }
        catch (Exception e)
        {
            UofMSpinner.setSelection(0);
        }

        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(eventHandler != null)
                {
                    BoilAdditionsSchema baSchema = list.get(position);
                    eventHandler.OnEditClick(baSchema);
                }

                notifyDataSetChanged();
            }
        });

        listItemEditText.setFocusable(false);
        listItemEditText.setClickable(false);
        listItemEditText.setEnabled(false);

        listItemEditText1.setFocusable(false);
        listItemEditText1.setClickable(false);
        listItemEditText1.setEnabled(false);

        listItemEditText2.setFocusable(false);
        listItemEditText2.setClickable(false);
        listItemEditText2.setEnabled(false);

        UofMSpinner.setClickable(false);


        return view;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
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
        void OnEditClick(BoilAdditionsSchema aBoilAdditionsSchema);
    }
}
