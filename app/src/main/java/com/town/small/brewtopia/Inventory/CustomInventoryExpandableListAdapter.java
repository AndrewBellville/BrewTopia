package com.town.small.brewtopia.Inventory;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.town.small.brewtopia.DataClass.InventorySchema;
import com.town.small.brewtopia.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Andrew on 4/17/2016.
 */
public class CustomInventoryExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<InventorySchema>> _listDataChild;

    //event handling
    private EventHandler eventHandler = null;
    private boolean isEditable = true;

    public CustomInventoryExpandableListAdapter(Context context, List<String> listDataHeader,
                                                HashMap<String, List<InventorySchema>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final InventorySchema hopsSchema = (InventorySchema) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.custom_row_inventory, null);
        }

        TextView inventoryName = (TextView) convertView.findViewById(R.id.inventoryName);
        TextView inventoryQty = (TextView) convertView.findViewById(R.id.inventoryQty);
        TextView inventoryAmount = (TextView) convertView.findViewById(R.id.inventoryAmount);
        TextView inventoryUse = (TextView) convertView.findViewById(R.id.inventoryUse);

        inventoryName.setText(hopsSchema.getInventoryName());
        inventoryQty.setText("Qty: "+ Integer.toString(hopsSchema.getInvetoryQty()));
        inventoryAmount.setText("Amount: "+Double.toString(hopsSchema.getAmount()));
        inventoryUse.setText("");


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.custom_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        TextView addTextView = (TextView) convertView.findViewById(R.id.AddTextView);

        if(!isEditable)
            addTextView.setVisibility(View.INVISIBLE);

        addTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eventHandler != null)
                {
                    eventHandler.OnAddClick(headerTitle);
                }
            }
        });

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
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
        void OnAddClick(String headerString);
    }
}
