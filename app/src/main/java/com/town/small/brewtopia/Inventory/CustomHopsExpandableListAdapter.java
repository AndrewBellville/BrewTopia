package com.town.small.brewtopia.Inventory;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.town.small.brewtopia.DataClass.HopsSchema;
import com.town.small.brewtopia.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Andrew on 4/17/2016.
 */
public class CustomHopsExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<HopsSchema>> _listDataChild;

    public CustomHopsExpandableListAdapter(Context context, List<String> listDataHeader,
                                           HashMap<String, List<HopsSchema>> listChildData) {
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

        final HopsSchema hopsSchema = (HopsSchema) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.custom_inventory_hops, null);
        }

        TextView hopsName = (TextView) convertView.findViewById(R.id.hopsName);
        TextView hopsAmount = (TextView) convertView.findViewById(R.id.hopsAmount);
        TextView hopsIBUs = (TextView) convertView.findViewById(R.id.hopsIBU);
        TextView hopsUse = (TextView) convertView.findViewById(R.id.hopsUse);

        hopsName.setText(hopsSchema.getInventoryName());
        hopsAmount.setText("Amount: "+Double.toString(hopsSchema.getAmount())+"oz");
        hopsIBUs.setText("IBU: "+Double.toString(hopsSchema.getIBU()));
        hopsUse.setText("");


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
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.custom_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

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
}