package com.town.small.brewtopia.AppSettings;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.town.small.brewtopia.DataClass.AppSettingsSchema;
import com.town.small.brewtopia.DataClass.BrewSchema;
import com.town.small.brewtopia.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 2/20/2016.
 */
public class CustomASListAdapter extends BaseAdapter implements ListAdapter  {

    private List<AppSettingsSchema> list = new ArrayList<AppSettingsSchema>();
    private Context context;
    private boolean isEditable = false;
    private AppSettingsHelper appSettingsHelper;

    public CustomASListAdapter(List<AppSettingsSchema> list, Context context) {
        this.list = list;
        this.context = context;
        appSettingsHelper = AppSettingsHelper.getInstance(context);
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
            view = inflater.inflate(R.layout.custom_row_settings_view, null);
        }

        AppSettingsSchema appSettingsSchema = list.get(position);


        //Handle TextView and display string from list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(appSettingsSchema.getSettingName());

        //Handle buttons and add onClickListeners
        Switch editSwitch = (Switch)view.findViewById(R.id.edit_switch);

        if(!isEditable) editSwitch.setVisibility(View.INVISIBLE);
        else
        {
            if(appSettingsSchema.getSettingValue().equals(appSettingsHelper.ON))
                editSwitch.setChecked(true);
            else
                editSwitch.setChecked(false);

            editSwitch.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Switch editSwitch = (Switch)v.findViewById(R.id.edit_switch);
                    AppSettingsSchema appSettingsSchema = list.get(position);
                    appSettingsHelper.UpdateAppSetting(appSettingsSchema,editSwitch.isChecked());
                    notifyDataSetChanged();
                }
            });
        }



        return view;
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

}