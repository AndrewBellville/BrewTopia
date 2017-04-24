package com.dev.town.small.brewtopia.Utilites;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import com.dev.town.small.brewtopia.DataClass.ScheduledEventSchema;

import java.util.Calendar;

/**
 * Created by Andrew on 4/24/2017.
 */
public class DatePickerFrag extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    EditText editText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    public void setEditText(EditText aEditText)
    {
        editText = aEditText;
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm+1, dd);
    }
    public void populateSetDate(int year, int month, int day) {

        String formMonth = String.format("%02d", month);
        String formDay = String.format("%02d", day);

        editText.setText( Integer.toString(year)+"-"+formMonth+"-"+formDay+" 12:00:00" );
    }
}
