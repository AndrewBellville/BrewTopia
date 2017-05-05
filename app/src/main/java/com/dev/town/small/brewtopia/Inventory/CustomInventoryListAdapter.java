package com.dev.town.small.brewtopia.Inventory;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dev.town.small.brewtopia.DataClass.BrewSchema;
import com.dev.town.small.brewtopia.DataClass.InventorySchema;
import com.dev.town.small.brewtopia.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 2/20/2016.
 */
public class CustomInventoryListAdapter extends BaseAdapter implements ListAdapter  {

    private List<InventorySchema> list = new ArrayList<InventorySchema>();
    private Context context;
    private boolean isDeleteView = false;
    private boolean hasColor = false;
    private List<Integer> selected = new ArrayList<>();

    //event handling
    private EventHandler eventHandler = null;

    public CustomInventoryListAdapter(List<InventorySchema> list, Context context) {
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
            view = inflater.inflate(R.layout.custom_row_inventory, null);
        }

        int textColor = Color.WHITE;
        if(isSelected(position)) {
            view.setBackgroundColor(context.getResources().getColor(R.color.AccentColor));
            textColor = Color.BLACK;
        }
        else {
            view.setBackgroundColor(Color.TRANSPARENT);
        }

        InventorySchema inventorySchema = list.get(position);

        TextView inventoryName = (TextView) view.findViewById(R.id.inventoryName);
        TextView inventoryQty = (TextView) view.findViewById(R.id.inventoryQty);
        TextView inventoryAmount = (TextView) view.findViewById(R.id.inventoryAmount);
        TextView inventoryUse = (TextView) view.findViewById(R.id.inventoryUse);

        inventoryName.setTextColor(textColor);
        inventoryQty.setTextColor(textColor);
        inventoryAmount.setTextColor(textColor);
        inventoryUse.setTextColor(textColor);

        inventoryName.setText(inventorySchema.getInventoryName());
        inventoryQty.setText("Qty: "+ Integer.toString(inventorySchema.getInvetoryQty()));
        inventoryAmount.setText("Amount: "+Double.toString(inventorySchema.getAmount()));
        inventoryUse.setText("");

        return view;
    }

    private boolean isSelected(int index)
    {
        for (Integer i: selected) {
            if(i==index)
                return true;
        }

        return false;
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
    public void setSelected(List<Integer> selected) {
        this.selected = selected;
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